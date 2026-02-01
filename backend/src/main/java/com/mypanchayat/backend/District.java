package com.mypanchayat.backend; // Keeping your package

import jakarta.persistence.*;
import java.util.List;

@Entity
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Optional: Useful if you want to get all blocks for a district later
    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    private List<Block> blocks;

    // --- CONSTRUCTORS ---
    public District() {}

    public District(String name) {
        this.name = name;
    }

    // --- GETTERS AND SETTERS (Fixes the errors) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Block> getBlocks() { return blocks; }
    public void setBlocks(List<Block> blocks) { this.blocks = blocks; }
}