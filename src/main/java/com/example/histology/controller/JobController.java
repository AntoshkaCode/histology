package com.example.histology.controller;

import com.example.histology.model.JobApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JobController {

    private final com.example.histology.repository.JobRepository jobRepository;
    private final com.example.histology.repository.PIRepository piRepository;

    public JobController(com.example.histology.repository.JobRepository jobRepository, com.example.histology.repository.PIRepository piRepository) {
        this.jobRepository = jobRepository;
        this.piRepository = piRepository;
    }

    @GetMapping("/admin/jobs")
public String listJobs(@org.springframework.web.bind.annotation.RequestParam(value = "search", required = false) String search, Model model) {
    java.util.List<com.example.histology.model.Job> jobs;
    if (search != null && !search.trim().isEmpty()) {
        jobs = jobRepository.searchJobs(search.trim());
    } else {
        jobs = jobRepository.findAll();
    }
    model.addAttribute("jobs", jobs);
    model.addAttribute("search", search);
    return "admin/jobs";
}

    @GetMapping("/admin/jobs/add")
    public String showAddJobForm(Model model) {
        model.addAttribute("job", new com.example.histology.dto.JobDto());
        // Fetch PI names for dropdown
        java.util.List<String> piNames = new java.util.ArrayList<>();
        for (var pi : piRepository.findAll()) {
            piNames.add(pi.getFirstName() + " " + pi.getLastName());
        }
        model.addAttribute("piNames", piNames);
        return "admin/job-add";
    }

    @PostMapping("/admin/jobs/add")
    public String addJob(@org.springframework.web.bind.annotation.ModelAttribute("job") com.example.histology.dto.JobDto jobDto,
                        org.springframework.validation.BindingResult result,
                        org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/job-add";
        }
        com.example.histology.model.Job job = new com.example.histology.model.Job();
        job.setJobName(jobDto.getJobName());
        job.setSampleConditions(jobDto.getSampleConditions());
        job.setPrincipalInvestigator(jobDto.getPrincipalInvestigator());
        jobRepository.save(job);
        redirectAttributes.addFlashAttribute("successMessage", "Job added successfully!");
        return "redirect:/admin/jobs";
    }

    @PostMapping("/admin/jobs/delete/{id}")
    public String deleteJob(@org.springframework.web.bind.annotation.PathVariable Long id,
                            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        jobRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Job deleted successfully!");
        return "redirect:/admin/jobs";
    }

    @GetMapping("/admin/jobs/edit/{id}")
    public String showEditJobForm(@org.springframework.web.bind.annotation.PathVariable Long id, Model model, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        var jobOpt = jobRepository.findById(id);
        if (jobOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Job not found with ID: " + id);
            return "redirect:/admin/jobs";
        }
        model.addAttribute("job", jobOpt.get());
        return "admin/job-edit";
    }

    @PostMapping("/admin/jobs/edit/{id}")
    public String updateJob(@org.springframework.web.bind.annotation.PathVariable Long id,
                            @org.springframework.web.bind.annotation.ModelAttribute("job") com.example.histology.model.Job job,
                            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        var existingOpt = jobRepository.findById(id);
        if (existingOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Job not found with ID: " + id);
            return "redirect:/admin/jobs";
        }
        var existing = existingOpt.get();
        existing.setJobName(job.getJobName());
        existing.setSampleConditions(job.getSampleConditions());
        existing.setPrincipalInvestigator(job.getPrincipalInvestigator());
        jobRepository.save(existing);
        redirectAttributes.addFlashAttribute("successMessage", "Job updated successfully!");
        return "redirect:/admin/jobs";
    }

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
