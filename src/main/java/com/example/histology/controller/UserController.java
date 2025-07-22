package com.example.histology.controller;

import com.example.histology.dto.UserRegistrationDto;
import com.example.histology.model.UserType;
import com.example.histology.service.UserService;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
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
            userService.registerNewUser(registrationDto);
            redirectAttributes.addFlashAttribute("success", true);
            return "redirect:/register?success";
        } catch (Exception e) {
            result.rejectValue("username", "user.exists", "An account already exists with this username.");
            return "register";
        }
    }
}
