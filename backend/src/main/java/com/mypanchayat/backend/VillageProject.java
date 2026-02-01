package com.mypanchayat.backend;

import jakarta.persistence.*;

@Entity
public class VillageProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;   // e.g., "Concrete Road Ward 4"
    private String status;        // "Planned", "In-Progress", "Completed"
    private double budgetAllocated;
    private double moneySpent;

    // --- Social Audit Features ---
    private boolean isVerifiedByCitizens = false;
    private String sarpanchPhotoUrl; // Photo uploaded by Official
    private String citizenPhotoUrl;  // Photo uploaded by Citizen (Audit)

    @ManyToOne
    @JoinColumn(name = "panchayat_id")
    private Panchayat panchayat;

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getBudgetAllocated() { return budgetAllocated; }
    public void setBudgetAllocated(double budgetAllocated) { this.budgetAllocated = budgetAllocated; }
    public double getMoneySpent() { return moneySpent; }
    public void setMoneySpent(double moneySpent) { this.moneySpent = moneySpent; }
    public boolean isVerifiedByCitizens() { return isVerifiedByCitizens; }
    public void setVerifiedByCitizens(boolean verifiedByCitizens) { isVerifiedByCitizens = verifiedByCitizens; }
    public String getSarpanchPhotoUrl() { return sarpanchPhotoUrl; }
    public void setSarpanchPhotoUrl(String sarpanchPhotoUrl) { this.sarpanchPhotoUrl = sarpanchPhotoUrl; }
    public String getCitizenPhotoUrl() { return citizenPhotoUrl; }
    public void setCitizenPhotoUrl(String citizenPhotoUrl) { this.citizenPhotoUrl = citizenPhotoUrl; }
    public Panchayat getPanchayat() { return panchayat; }
    public void setPanchayat(Panchayat panchayat) { this.panchayat = panchayat; }
}