package com.example.histology.controller;

import com.example.histology.dto.UserRegistrationDto;
import com.example.histology.model.User;
import com.example.histology.model.UserType;
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

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/users")
    public String createUser(
            @ModelAttribute("user") @Valid UserRegistrationDto registrationDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "user-signup";
        }
        
        try {
            userService.registerNewUser(registrationDto);
            redirectAttributes.addFlashAttribute("success", "User registered successfully!");
            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            result.rejectValue("username", "user.exists", e.getMessage());
            return "user-signup";
        }
    }

    @GetMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
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
