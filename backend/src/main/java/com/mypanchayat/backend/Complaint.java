package com.mypanchayat.backend;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String citizenName;
    private String mobileNumber;

    private String category;

    @Column(length = 2000)
    private String description;

    private String ward;
    private Long userId;

    private LocalDate createdDate;
    private LocalDate deadlineDate; // <-- ADDED BACK SAFELY

    private String status;
    private Integer upvoteCount = 0;

    // --- NEW: GPS LOCATION FIELDS ---
    private Double latitude;
    private Double longitude;

    // --- NEW: AI & GOVERNANCE FIELDS ---
    private Integer reportCount = 1; // How many duplicate reports merged into this one
    private Double urgencyScore = 0.0; // AI calculated score (0 to 100)

    // Media URLs
    private String photoUrl;
    private String videoUrl;
    private String audioUrl;

    @ManyToOne
    @JoinColumn(name = "panchayat_id")
    private Panchayat panchayat;

    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

    public LocalDate getDeadlineDate() { return deadlineDate; } // <-- GETTER ADDED
    public void setDeadlineDate(LocalDate deadlineDate) { this.deadlineDate = deadlineDate; } // <-- SETTER ADDED

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getUpvoteCount() { return upvoteCount; }
    public void setUpvoteCount(Integer upvoteCount) { this.upvoteCount = upvoteCount; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }

    public Panchayat getPanchayat() { return panchayat; }
    public void setPanchayat(Panchayat panchayat) { this.panchayat = panchayat; }

    // --- NEW GETTERS & SETTERS ---
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Integer getReportCount() { return reportCount; }
    public void setReportCount(Integer reportCount) { this.reportCount = reportCount; }
    public void incrementReportCount() { this.reportCount++; } // Helper method for AI

    public Double getUrgencyScore() { return urgencyScore; }
    public void setUrgencyScore(Double urgencyScore) { this.urgencyScore = urgencyScore; }
}