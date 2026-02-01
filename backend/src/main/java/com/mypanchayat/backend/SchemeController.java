package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/schemes")
@CrossOrigin(origins = "*")
public class SchemeController {

    @Autowired
    private SchemeRepository schemeRepository;

    // 1. PUBLIC: Get all schemes (Anyone can see this!)
    @GetMapping
    public List<Scheme> getAllSchemes() {
        return schemeRepository.findAll();
    }

    // 2. ADMIN: Add a new scheme
    @PostMapping("/add")
    public Scheme addScheme(@RequestBody Scheme scheme) {
        return schemeRepository.save(scheme);
    }
}