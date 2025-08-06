package com.example.histology.service;

import com.example.histology.model.Sample;
import com.example.histology.repository.SampleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleService {
    private final SampleRepository sampleRepository;

    public SampleService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    public List<Sample> findAllSamples() {
        return sampleRepository.findAll();
    }

    public List<Sample> searchSamplesByName(String name) {
        return sampleRepository.findByNameContainingIgnoreCase(name);
    }

    public org.springframework.data.domain.Page<Sample> findAllSamples(org.springframework.data.domain.Pageable pageable) {
        return sampleRepository.findAll(pageable);
    }

    public Sample saveSample(Sample sample) {
        if (sample.getCreatedAt() == null) {
            sample.setCreatedAt(java.time.LocalDateTime.now());
        }
        return sampleRepository.save(sample);
    }
}
