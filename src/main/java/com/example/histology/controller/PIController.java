package com.example.histology.controller;

import com.example.histology.dto.PIApplicationDto;
import com.example.histology.model.PI;
import com.example.histology.repository.PIRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pi")
public class PIController {

    private final PIRepository piRepository;

    public PIController(PIRepository piRepository) {
        this.piRepository = piRepository;
    }

    @GetMapping("/apply")
    public String showCreateForm(Model model) {
        try {
            if (!model.containsAttribute("piApplication") || model.getAttribute("piApplication") == null) {
                model.addAttribute("piApplication", new PIApplicationDto());
            }
        } catch (Exception e) {
            model.addAttribute("piApplication", new PIApplicationDto());
        }
        return "pi/application";
    }
    
    @GetMapping("/apply/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return piRepository.findById(id)
                .map(pi -> {
                    PIApplicationDto dto = new PIApplicationDto();
                    dto.setId(pi.getId());
                    dto.setFirstName(pi.getFirstName());
                    dto.setLastName(pi.getLastName());
                    dto.setDepartment(pi.getDepartment());
                    dto.setEmail(pi.getEmail());
                    model.addAttribute("piApplication", dto);
                    return "pi/application";
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid PI Id:" + id));
    }

    @PostMapping("/apply")
    public String createPI(@Valid @ModelAttribute("piApplication") PIApplicationDto piApplication,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("piApplication", piApplication);
            return "pi/application";
        }
        try {
            PI pi = new PI();
            pi.setFirstName(piApplication.getFirstName());
            pi.setLastName(piApplication.getLastName());
            pi.setDepartment(piApplication.getDepartment());
            pi.setEmail(piApplication.getEmail());
            pi.setCreatedAt(java.time.LocalDateTime.now());
            piRepository.save(pi);
            redirectAttributes.addFlashAttribute("successMessage", "PI has been created successfully!");
            return "redirect:/admin/pis";
        } catch (Exception e) {
            model.addAttribute("piApplication", piApplication);
            model.addAttribute("errorMessage", "Error saving PI: " + e.getMessage());
            return "pi/application";
        }
    }
    
    @PostMapping("/apply/{id}")
    public String updatePI(@PathVariable Long id,
                         @Valid @ModelAttribute("piApplication") PIApplicationDto piApplication,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        piApplication.setId(id);
        return savePI(piApplication, result, redirectAttributes);
    }
    
    private String savePI(PIApplicationDto piApplication,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "pi/application";
        }

        // Check if email already exists for a different PI
        if (piApplication.getId() == null) {
            // Creating new PI
            if (piRepository.existsByEmail(piApplication.getEmail())) {
                result.rejectValue("email", "email.exists", "A PI with this email already exists");
                return "pi/application";
            }
        } else {
            // Updating existing PI
            piRepository.findByEmail(piApplication.getEmail())
                .ifPresent(pi -> {
                    if (!pi.getId().equals(piApplication.getId())) {
                        result.rejectValue("email", "email.exists", "A PI with this email already exists");
                    }
                });
                
            if (result.hasErrors()) {
                return "pi/application";
            }
        }

        // Save or update the PI
        PI pi = piApplication.getId() == null ? new PI() : piRepository.findById(piApplication.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid PI Id:" + piApplication.getId()));
                
        pi.setFirstName(piApplication.getFirstName());
        pi.setLastName(piApplication.getLastName());
        pi.setDepartment(piApplication.getDepartment());
        pi.setEmail(piApplication.getEmail());
        
        piRepository.save(pi);
        
        String message = piApplication.getId() == null ? 
            "PI has been created successfully!" : 
            "PI has been updated successfully!";
            
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/admin/pis";
    }
}
