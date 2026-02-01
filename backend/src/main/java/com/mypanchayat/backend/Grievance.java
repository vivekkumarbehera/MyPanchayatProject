package com.mypanchayat.backend;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grievances")
public class Grievance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(name = "ward_number")
    private String wardNumber;

    @Column(name = "media_type")
    private String mediaType; // "PHOTO", "VIDEO", or "TEXT"

    @Column(name = "media_url")
    private String mediaUrl;  // We will store the filename here

    private String status;    // "PENDING", "RESOLVED"

    private LocalDateTime submittedAt;

    // --- LINK TO USER ---
    // This connects the grievance to the Citizen who submitted it.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Grievance() {
        this.submittedAt = LocalDateTime.now();
        this.status = "PENDING"; // Default status
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWardNumber() { return wardNumber; }
    public void setWardNumber(String wardNumber) { this.wardNumber = wardNumber; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}