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
}
