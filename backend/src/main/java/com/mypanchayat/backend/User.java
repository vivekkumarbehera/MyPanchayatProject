package com.mypanchayat.backend;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Approval Fields
    private String status;   // "PENDING", "APPROVED"
    private String proofUrl; // Link to uploaded ID proof

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    private String role;

    // OTP Fields
    private String otp;
    private Long otpExpiryTime;

    // Location Fields
    @Column(name = "district_name")
    private String district;

    @Column(name = "block_name")
    private String block;

    @Column(name = "panchayat_name")
    private String panchayat;

    // Gamification Fields
    private int karmaPoints = 0;
    private String badges;

    public User() {}

    // --- GETTERS AND SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // ✅ ADDED THESE MISSING METHODS
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getProofUrl() { return proofUrl; }
    public void setProofUrl(String proofUrl) { this.proofUrl = proofUrl; }
    // --------------------------------

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public Long getOtpExpiryTime() { return otpExpiryTime; }
    public void setOtpExpiryTime(Long otpExpiryTime) { this.otpExpiryTime = otpExpiryTime; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getBlock() { return block; }
    public void setBlock(String block) { this.block = block; }

    public String getPanchayat() { return panchayat; }
    public void setPanchayat(String panchayat) { this.panchayat = panchayat; }

    public int getKarmaPoints() { return karmaPoints; }
    public void setKarmaPoints(int karmaPoints) { this.karmaPoints = karmaPoints; }

    public String getBadges() { return badges; }
    public void setBadges(String badges) { this.badges = badges; }
}