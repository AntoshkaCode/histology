package com.example.histology.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final com.example.histology.repository.JobRepository jobRepository;
    private final com.example.histology.repository.PIRepository piRepository;
    private final com.example.histology.service.SampleService sampleService;
    private final com.example.histology.repository.AppUserRepository appUserRepository;

    @org.springframework.beans.factory.annotation.Autowired
    public HomeController(
        com.example.histology.repository.JobRepository jobRepository,
        com.example.histology.repository.PIRepository piRepository,
        com.example.histology.service.SampleService sampleService,
        com.example.histology.repository.AppUserRepository appUserRepository
    ) {

        this.jobRepository = jobRepository;
        this.piRepository = piRepository;
        this.sampleService = sampleService;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/")
    public String home(
            org.springframework.ui.Model model,
            @org.springframework.web.bind.annotation.RequestParam(value = "jobPage", defaultValue = "0") int jobPage,
            @org.springframework.web.bind.annotation.RequestParam(value = "samplePage", defaultValue = "0") int samplePage,
            @org.springframework.web.bind.annotation.RequestParam(value = "jobFilter", defaultValue = "all") String jobFilter,
            @org.springframework.web.bind.annotation.RequestParam(value = "sort", required = false) String sort
    ) {
        // Add user position (role) for navbar display
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = authentication != null ? authentication.getName() : null;
        com.example.histology.model.UserType userType = null;
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            java.util.Optional<com.example.histology.model.AppUser> userOpt = appUserRepository.findByEmail(email);
            if (userOpt.isPresent() && userOpt.get().getUserType() != null) {
                model.addAttribute("position", userOpt.get().getUserType().name());
                userType = userOpt.get().getUserType();
            }
        }

        org.springframework.data.domain.Pageable jobPageable = org.springframework.data.domain.PageRequest.of(jobPage, 10, org.springframework.data.domain.Sort.by("id"));
        org.springframework.data.domain.Pageable samplePageable = org.springframework.data.domain.PageRequest.of(samplePage, 10, org.springframework.data.domain.Sort.by("id"));

        // Default: show nothing
        boolean showJobs = false;
        boolean showSamples = false;
        boolean showUsers = false;

        // Determine user type
        if (userType != null) {
            switch (userType) {
                case ADMIN:
                    showJobs = true;
                    showSamples = true;
                    showUsers = true;
                    break;
                case PRINCIPAL_INVESTIGATOR:
                    showJobs = true;
                    showSamples = true;
                    break;
                case TECHNICIAN:
                    showSamples = true;
                    break;
                default:
                    break;
            }
        }
        if (showJobs) {
            org.springframework.data.domain.Page<com.example.histology.model.Job> jobsPage;
            // If PI, allow filtering by 'all' or 'mine'
            if (userType == com.example.histology.model.UserType.PRINCIPAL_INVESTIGATOR) {
                String piEmail = null;
                if (email != null) {
                    java.util.Optional<com.example.histology.model.PI> piOpt = piRepository.findByEmail(email);
                    if (piOpt.isPresent()) {
                        piEmail = piOpt.get().getEmail();
                    } else {
                        System.out.println("[DEBUG] No PI found for email: " + email);
                    }
                }
                if ("mine".equals(jobFilter)) {
                    if (piEmail != null) {
                        jobsPage = jobRepository.findByPrincipalInvestigator(piEmail, jobPageable);
                    } else {
                        // No PI found for this user, show empty list and feedback
                        jobsPage = org.springframework.data.domain.Page.empty(jobPageable);
                        model.addAttribute("piEmailNotFound", true);
                    }
                } else {
                    jobsPage = jobRepository.findAll(jobPageable);
                }
                model.addAttribute("jobFilter", jobFilter);
            } else {
                jobsPage = jobRepository.findAll(jobPageable);
            }
            model.addAttribute("jobsPage", jobsPage);
            // Always set jobFilter in model for template
            if (!model.containsAttribute("jobFilter")) {
                model.addAttribute("jobFilter", jobFilter);
            }
        }
        if (showSamples) {
            org.springframework.data.domain.Page<com.example.histology.model.Sample> samplesPage = sampleService.findAllSamples(samplePageable);
            model.addAttribute("samplesPage", samplesPage);
        }
        if (showUsers) {
            java.util.List<com.example.histology.model.AppUser> users;
            if ("position".equals(sort)) {
                users = appUserRepository.findAll(org.springframework.data.domain.Sort.by("userType"));
            } else {
                users = appUserRepository.findAll();
            }
            model.addAttribute("users", users);
        }
        model.addAttribute("showJobs", showJobs);
        model.addAttribute("showSamples", showSamples);
        model.addAttribute("showUsers", showUsers);
        return "home";
    }
    
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";
    }
}
