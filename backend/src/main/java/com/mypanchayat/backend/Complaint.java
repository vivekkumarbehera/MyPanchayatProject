package com.mypanchayat.backend;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ ADDED: This links the complaint to the specific user who posted it
    private Long userId;

    private String mobileNumber;
    private String citizenName;

    @Column(length = 1000)
    private String description;

    private String ward;

    @ManyToOne
    @JoinColumn(name = "panchayat_id", nullable = false)
    private Panchayat panchayat;

    private String status = "Pending";
    private LocalDate createdDate;

    // --- OPTIONAL FIELDS ---
    private String category;
    private LocalDate deadlineDate;

    // --- MEDIA FIELDS ---
    private String photoUrl;
    private String videoUrl;
    private String audioUrl;

    // --- UPVOTE FIELDS ---
    @ElementCollection
    private Set<Long> upvoterIds = new HashSet<>();
    private int upvoteCount = 0;

    // --- GETTERS AND SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // ✅ ADDED: Getters and Setters for userId
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public Panchayat getPanchayat() { return panchayat; }
    public void setPanchayat(Panchayat panchayat) { this.panchayat = panchayat; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getDeadlineDate() { return deadlineDate; }
    public void setDeadlineDate(LocalDate deadlineDate) { this.deadlineDate = deadlineDate; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }

    public Set<Long> getUpvoterIds() { return upvoterIds; }
    public void setUpvoterIds(Set<Long> upvoterIds) { this.upvoterIds = upvoterIds; }

    public int getUpvoteCount() { return upvoteCount; }
    public void setUpvoteCount(int upvoteCount) { this.upvoteCount = upvoteCount; }
}