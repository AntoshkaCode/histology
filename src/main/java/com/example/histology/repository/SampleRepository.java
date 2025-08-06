package com.example.histology.repository;

import com.example.histology.model.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, Long> {
    java.util.List<Sample> findByNameContainingIgnoreCase(String name);
}
