package com.mypanchayat.backend;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // E.g., "Income", "Caste", "Residence"
    private String description; // Reason for application
    private String status; // "PENDING", "APPROVED", "REJECTED"

    private LocalDateTime submissionDate;

    // Link this application to a specific Citizen
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // --- CONSTRUCTOR ---
    public Application() {
        this.status = "PENDING"; // Default status is always PENDING
        this.submissionDate = LocalDateTime.now(); // Auto-set the time
    }

    // --- GETTERS AND SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}