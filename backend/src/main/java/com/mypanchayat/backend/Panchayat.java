package com.mypanchayat.backend;

import jakarta.persistence.*;

@Entity
@Table(name = "panchayats")
public class Panchayat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // The Name of the Panchayat

    // --- RELATIONSHIPS (This fixes the setBlock error) ---
    @ManyToOne
    @JoinColumn(name = "block_id")
    private Block block;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    // --- SARPANCH DETAILS (Kept from previous fix) ---
    private String sarpanchName;
    private String sarpanchContact;
    private String sarpanchPhotoUrl;

    // --- GETTERS AND SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // ✅ These methods fix the "cannot find symbol method setBlock"
    public Block getBlock() { return block; }
    public void setBlock(Block block) { this.block = block; }

    public District getDistrict() { return district; }
    public void setDistrict(District district) { this.district = district; }

    // ✅ Sarpanch Getters
    public String getSarpanchName() { return sarpanchName; }
    public void setSarpanchName(String sarpanchName) { this.sarpanchName = sarpanchName; }

    public String getSarpanchContact() { return sarpanchContact; }
    public void setSarpanchContact(String sarpanchContact) { this.sarpanchContact = sarpanchContact; }

    public String getSarpanchPhotoUrl() { return sarpanchPhotoUrl; }
    public void setSarpanchPhotoUrl(String sarpanchPhotoUrl) { this.sarpanchPhotoUrl = sarpanchPhotoUrl; }
}