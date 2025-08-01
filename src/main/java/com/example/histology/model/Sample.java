package com.example.histology.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "samples")
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private boolean embedded;

    @Column(nullable = false)
    private boolean cut;

    @Column(nullable = false)
    private boolean stain;

    @Column(nullable = false)
    private boolean completed;

    @Column(length = 500)
    private String notes;



    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Sample() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
