package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class VillageProjectController {

    @Autowired
    private VillageProjectService service;

    @Autowired
    private PanchayatRepository panchayatRepository;

    // 1. Get Projects for a specific Panchayat (The Wiki)
    @GetMapping("/panchayat/{panchayatId}")
    public List<VillageProject> getProjects(@PathVariable Long panchayatId) {
        return service.getProjectsByPanchayat(panchayatId);
    }

    // 2. ADMIN: Create a new Project (Sarpanch does this)
    @PostMapping
    public VillageProject createProject(@RequestBody VillageProject project, @RequestParam Long panchayatId) {
        Panchayat p = panchayatRepository.findById(panchayatId)
                .orElseThrow(() -> new RuntimeException("Panchayat not found"));
        project.setPanchayat(p);
        return service.createProject(project);
    }

    // 3. CITIZEN: Verify a Project (Social Audit)
    // URL: PUT /api/projects/1/verify?userId=5
    @PutMapping("/{id}/verify")
    public VillageProject verifyProject(@PathVariable Long id,
                                        @RequestParam Long userId,
                                        @RequestBody(required = false) String photoUrl) {
        // If no photo URL is sent, we just use a placeholder
        String proof = (photoUrl != null) ? photoUrl : "http://placeholder-image.com/proof.jpg";

        return service.verifyProject(id, userId, proof);
    }
}