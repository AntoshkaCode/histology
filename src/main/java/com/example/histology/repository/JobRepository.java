package com.example.histology.repository;

import com.example.histology.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT j FROM Job j WHERE " +
            "CAST(j.id AS string) LIKE %:search% OR " +
            "LOWER(j.jobName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(j.principalInvestigator) LIKE LOWER(CONCAT('%', :search, '%'))")
    java.util.List<Job> searchJobs(@org.springframework.data.repository.query.Param("search") String search);

    org.springframework.data.domain.Page<Job> findByPrincipalInvestigator(String principalInvestigator, org.springframework.data.domain.Pageable pageable);

}
