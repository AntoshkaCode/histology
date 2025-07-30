package com.example.histology.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final com.example.histology.service.UserService userService;
    private final com.example.histology.repository.JobRepository jobRepository;
    private final com.example.histology.repository.PIRepository piRepository;
    private final com.example.histology.service.SampleService sampleService;

    @org.springframework.beans.factory.annotation.Autowired
    public HomeController(
        com.example.histology.service.UserService userService,
        com.example.histology.repository.JobRepository jobRepository,
        com.example.histology.repository.PIRepository piRepository,
        com.example.histology.service.SampleService sampleService
    ) {
        this.userService = userService;
        this.jobRepository = jobRepository;
        this.piRepository = piRepository;
        this.sampleService = sampleService;
    }

    @GetMapping("/")
    public String home(
            org.springframework.ui.Model model,
            @org.springframework.web.bind.annotation.RequestParam(value = "jobPage", defaultValue = "0") int jobPage,
            @org.springframework.web.bind.annotation.RequestParam(value = "samplePage", defaultValue = "0") int samplePage
    ) {
        org.springframework.data.domain.Pageable jobPageable = org.springframework.data.domain.PageRequest.of(jobPage, 10, org.springframework.data.domain.Sort.by("id"));
        org.springframework.data.domain.Pageable samplePageable = org.springframework.data.domain.PageRequest.of(samplePage, 10, org.springframework.data.domain.Sort.by("id"));
        org.springframework.data.domain.Page<com.example.histology.model.Job> jobsPage = jobRepository.findAll(jobPageable);
        org.springframework.data.domain.Page<com.example.histology.model.Sample> samplesPage = sampleService.findAllSamples(samplePageable);
        model.addAttribute("jobsPage", jobsPage);
        model.addAttribute("samplesPage", samplesPage);
        return "home";
    }
    
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";
    }
}
