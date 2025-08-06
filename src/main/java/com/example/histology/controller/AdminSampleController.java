package com.example.histology.controller;

import com.example.histology.model.Sample;
import com.example.histology.service.SampleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/samples")
public class AdminSampleController {
    private final SampleService sampleService;

    public AdminSampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping
    public String listSamples(Model model, @RequestParam(value = "search", required = false) String searchTerm) {
        List<Sample> samples;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            samples = sampleService.searchSamplesByName(searchTerm.trim());
            model.addAttribute("searchTerm", searchTerm);
        } else {
            samples = sampleService.findAllSamples();
        }
        model.addAttribute("samples", samples);
        return "admin/samples";
    }
}
