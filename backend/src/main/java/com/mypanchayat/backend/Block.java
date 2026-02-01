package com.mypanchayat.backend;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore; // <--- Import this

@Entity
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "district_id")
    @JsonIgnore  // <--- ADD THIS LINE HERE
    private District district;

    public Block() {}

    public Block(String name, District district) {
        this.name = name;
        this.district = district;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public District getDistrict() { return district; }
    public void setDistrict(District district) { this.district = district; }
}