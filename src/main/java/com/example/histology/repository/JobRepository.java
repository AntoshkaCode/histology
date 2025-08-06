package com.example.histology.repository;

import com.example.histology.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    org.springframework.data.domain.Page<Job> findByPrincipalInvestigator(String principalInvestigator, org.springframework.data.domain.Pageable pageable);

}
