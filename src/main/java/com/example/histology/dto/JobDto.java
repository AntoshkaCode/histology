package com.example.histology.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JobDto {
    @NotEmpty(message = "Job name is required")
    private String jobName;
    private String sampleConditions;
    @NotEmpty(message = "Principal Investigator is required")
    private String principalInvestigator;

    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }
    public String getSampleConditions() { return sampleConditions; }
    public void setSampleConditions(String sampleConditions) { this.sampleConditions = sampleConditions; }
    public String getPrincipalInvestigator() { return principalInvestigator; }
    public void setPrincipalInvestigator(String principalInvestigator) { this.principalInvestigator = principalInvestigator; }
}
