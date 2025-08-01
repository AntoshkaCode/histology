package com.example.histology.controller;

import com.example.histology.dto.UserRegistrationDto;
import com.example.histology.model.UserType;
import com.example.histology.service.UserService;
import com.example.histology.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class UserController {
    private final UserService userService;
    private final AppUserService appUserService;

    public UserController(UserService userService, AppUserService appUserService) {
        this.userService = userService;
        this.appUserService = appUserService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        System.out.println("[DEBUG] UserController: showRegistrationForm() called");
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUserType(UserType.USER); // Default to regular user
        model.addAttribute("user", registrationDto);
        return "register";
    }

    @PostMapping
    public String registerUserAccount(
            @ModelAttribute("user") @Valid UserRegistrationDto registrationDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "register";
        }

        try {
            appUserService.registerNewAppUser(registrationDto);
            redirectAttributes.addFlashAttribute("success", true);
            return "redirect:/register?success";
        } catch (Exception e) {
            result.rejectValue("email", "user.exists", "An account already exists with this email.");
            return "register";
        }
    }
}
