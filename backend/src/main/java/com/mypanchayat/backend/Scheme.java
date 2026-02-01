package com.mypanchayat.backend;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "schemes")
public class Scheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "Pradhan Mantri Awas Yojana"

    @Column(length = 1000)
    private String description; // Simple explanation of benefits

    private String eligibility; // e.g., "Income below 1 Lakh"

    private double benefitAmount; // e.g., 120000.00

    private LocalDate announcedDate;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEligibility() { return eligibility; }
    public void setEligibility(String eligibility) { this.eligibility = eligibility; }
    public double getBenefitAmount() { return benefitAmount; }
    public void setBenefitAmount(double benefitAmount) { this.benefitAmount = benefitAmount; }
    public LocalDate getAnnouncedDate() { return announcedDate; }
    public void setAnnouncedDate(LocalDate announcedDate) { this.announcedDate = announcedDate; }
}