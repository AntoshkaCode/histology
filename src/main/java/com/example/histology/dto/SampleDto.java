package com.example.histology.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SampleDto {
    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Description is required")
    private String description;
    private boolean embedded;
    private boolean cut;
    private boolean stain;
    private boolean completed;
    private String notes;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isEmbedded() { return embedded; }
    public void setEmbedded(boolean embedded) { this.embedded = embedded; }
    public boolean isCut() { return cut; }
    public void setCut(boolean cut) { this.cut = cut; }
    public boolean isStain() { return stain; }
    public void setStain(boolean stain) { this.stain = stain; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
