package com.example.histology.controller;

import com.example.histology.model.JobApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @GetMapping("/apply")
    public String showJobForm(Model model) {
        model.addAttribute("jobApplication", new JobApplication());
        return "job-application";
    }

    @PostMapping("/apply")
    public String submitJobForm(@ModelAttribute JobApplication jobApplication, Model model) {
        // Here you would typically save the application to a database
        System.out.println("Received job application: " + jobApplication);
        model.addAttribute("success", true);
        model.addAttribute("jobApplication", new JobApplication()); // Reset the form
        return "job-application";
    }
}
