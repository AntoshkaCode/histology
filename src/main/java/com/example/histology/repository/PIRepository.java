package com.example.histology.repository;

import com.example.histology.model.PI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PIRepository extends JpaRepository<PI, Long> {
    boolean existsByEmail(String email);
    Optional<PI> findByEmail(String email);

}
