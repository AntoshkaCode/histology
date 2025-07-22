package com.example.histology.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobName;
    private String sampleConditions;
    private String principalInvestigator;

    public Job(String jobName, String sampleConditions, String principalInvestigator) {
        this.jobName = jobName;
        this.sampleConditions = sampleConditions;
        this.principalInvestigator = principalInvestigator;
    }
}
