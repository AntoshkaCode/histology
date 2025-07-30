package com.example.histology.controller;

import com.example.histology.dto.UserRegistrationDto;
import com.example.histology.model.PI;
import com.example.histology.model.User;
import com.example.histology.model.UserType;
import com.example.histology.repository.PIRepository;
import com.example.histology.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PIRepository piRepository;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncoder, PIRepository piRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.piRepository = piRepository;
    }

    @GetMapping("/users")
    public String listUsers(Model model, 
                          @RequestParam(value = "search", required = false) String searchTerm) {
        List<User> users = java.util.Collections.emptyList();
        try {
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                users = userService.searchUsers(searchTerm);
                model.addAttribute("searchTerm", searchTerm);
            } else {
                users = userService.findAllUsers();
            }
            if (users == null) users = java.util.Collections.emptyList();
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Could not load users: " + e.getMessage());
            users = java.util.Collections.emptyList();
        }
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/users")
    public String createUser(@Valid @ModelAttribute("user") User user, 
                           BindingResult result, 
                           Model model,
                           RedirectAttributes redirectAttributes) {
        
        // Check if username is already taken for new users
        if (user.getId() == null && userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "user.exists", "Username is already taken");
        }
        
        // Only validate password if it's a new user or password is being changed
        if (user.getId() == null || (user.getPassword() != null && !user.getPassword().isEmpty())) {
            if (user.getPassword() == null || user.getPassword().length() < 8) {
                result.rejectValue("password", "password.length", "Password must be at least 8 characters long");
            }
        }
        
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "admin/user-form";
        }
        
        try {
            // Encode password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // Save the user
            userService.saveUser(user);
            redirectAttributes.addAttribute("success", "User created successfully");
            return "redirect:/admin/users";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error creating user: " + e.getMessage());
            model.addAttribute("isEdit", false);
            return "admin/user-form";
        }
    }
    
    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id,
                           @Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        
        // Get existing user to preserve password if not changed
        User existingUser = userService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        
        // Check if username is already taken by another user
        if (userService.existsByUsernameAndIdNot(user.getUsername(), id)) {
            result.rejectValue("username", "user.exists", "Username is already taken");
        }
        
        // Only validate password if it's being changed
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            if (user.getPassword().length() < 8) {
                result.rejectValue("password", "password.length", "Password must be at least 8 characters long");
            }
        } else {
            // Keep the existing password if not changed
            user.setPassword(existingUser.getPassword());
        }
        
        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "admin/user-form";
        }
        
        try {
            // Encode password if it was changed
            if (user.getPassword() != null && !user.getPassword().isEmpty() && 
                !user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            
            // Preserve creation date and other fields
            user.setCreatedAt(existingUser.getCreatedAt());
            
            // Update the user
            userService.saveUser(user);
            redirectAttributes.addAttribute("success", "User updated successfully");
            return "redirect:/admin/users";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error updating user: " + e.getMessage());
            model.addAttribute("isEdit", true);
            return "admin/user-form";
        }
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return userService.findById(id)
            .map(user -> {
                // Clear password so it's not displayed in the form
                user.setPassword("");
                model.addAttribute("user", user);
                model.addAttribute("isEdit", true);
                return "admin/user-form";
            })
            .orElseGet(() -> {
                redirectAttributes.addAttribute("error", "User not found with ID: " + id);
                return "redirect:/admin/users";
            });
    }

    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        User user = new User();
        user.setEnabled(true); // Default to enabled
        user.setUserType(UserType.USER); // Default user type
        model.addAttribute("user", user);
        model.addAttribute("isEdit", false);
        return "admin/user-form";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // Prevent deleting the currently logged-in user
            userService.findByUsername(principal.getName()).ifPresentOrElse(
                currentUser -> {
                    if (!currentUser.getId().equals(id)) {
                        userService.deleteUser(id);
                        redirectAttributes.addAttribute("success", "User deleted successfully");
                    } else {
                        redirectAttributes.addAttribute("error", "You cannot delete your own account");
                    }
                },
                () -> redirectAttributes.addAttribute("error", "User not found")
            );
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
    
    @GetMapping("/pis")
    public String listPIs(Model model) {
        List<PI> pis = null;
        try {
            pis = piRepository.findAllByOrderByCreatedAtDesc();
        } catch (Exception e) {
            pis = java.util.Collections.emptyList();
            model.addAttribute("errorMessage", "Could not load PIs.");
        }
        if (pis == null) pis = java.util.Collections.emptyList();
        model.addAttribute("pis", pis);
        return "admin/pis";
    }
    
    @PostMapping("/pis/delete/{id}")
    public String deletePI(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            piRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "PI deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting PI: " + e.getMessage());
        }
        return "redirect:/admin/pis";
    }

    @GetMapping("/signup")
    public String showHomeSignUpPage(Model model) {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUserType(UserType.USER); // Default to regular user
        model.addAttribute("user", registrationDto);
        return "user-signup";
    }

    @GetMapping
    public String adminDashboard() {
        return "admin/dashboard";
    }


}
