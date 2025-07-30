package com.example.histology.controller;

import com.example.histology.dto.SampleDto;
import com.example.histology.model.Sample;
import com.example.histology.repository.SampleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/samples")
public class SampleController {
    @Autowired
    private SampleRepository sampleRepository;

    @GetMapping("/add")
    public String showAddSampleForm(Model model) {
        model.addAttribute("sample", new SampleDto());
        return "admin/sample-add";
    }

    @PostMapping("/add")
    public String addSample(@ModelAttribute("sample") @Valid SampleDto sampleDto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/sample-add";
        }
        Sample sample = new Sample();
        sample.setName(sampleDto.getName());
        sample.setDescription(sampleDto.getDescription());
        sample.setEmbedded(sampleDto.isEmbedded());
        sample.setCut(sampleDto.isCut());
        sample.setStain(sampleDto.isStain());
        sample.setCompleted(sampleDto.isCompleted());
        sample.setNotes(sampleDto.getNotes());
        sampleRepository.save(sample);
        redirectAttributes.addFlashAttribute("successMessage", "Sample added successfully!");
        return "redirect:/admin/samples";
    }
}
