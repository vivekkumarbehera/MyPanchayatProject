package com.mypanchayat.backend;

import jakarta.persistence.*;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "Concrete Road Ward 4"
    private double budget;
    private double amountSpent;
    private String status; // "Planned", "In-Progress", "Completed"
    private int completionPercentage; // 0 to 100

    // Getters and Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }
    public double getAmountSpent() { return amountSpent; }
    public void setAmountSpent(double amountSpent) { this.amountSpent = amountSpent; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(int completionPercentage) { this.completionPercentage = completionPercentage; }
}