package com.mypanchayat.backend;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class MarketListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;       // e.g., "Tractor for Rent"
    private String type;        // "OFFER" (I have) or "REQUEST" (I need)
    private String category;    // "Agriculture", "Labor", "Goods"
    private double price;       // 0 if negotiable
    private String contactPhone;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User postedBy;      // Who posted this?

    private LocalDate datePosted;

    public MarketListing() {
        this.datePosted = LocalDate.now();
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public User getPostedBy() { return postedBy; }
    public void setPostedBy(User postedBy) { this.postedBy = postedBy; }
    public LocalDate getDatePosted() { return datePosted; }
    public void setDatePosted(LocalDate datePosted) { this.datePosted = datePosted; }
}