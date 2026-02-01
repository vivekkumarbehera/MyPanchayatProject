package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VillageProjectService {

    @Autowired
    private VillageProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Get Projects for the Wiki
    public List<VillageProject> getProjectsByPanchayat(Long panchayatId) {
        return projectRepository.findByPanchayatId(panchayatId);
    }

    // 2. ADMIN: Create a Project
    public VillageProject createProject(VillageProject project) {
        return projectRepository.save(project);
    }

    // 3. CITIZEN: Social Audit (Verify a Project)
    public VillageProject verifyProject(Long projectId, Long citizenId, String photoUrl) {
        VillageProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User citizen = userRepository.findById(citizenId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Logic: Mark Verified and Save Proof
        project.setVerifiedByCitizens(true);
        project.setCitizenPhotoUrl(photoUrl);

        // --- GAMIFICATION: AWARD POINTS ---
        int currentPoints = citizen.getKarmaPoints();
        citizen.setKarmaPoints(currentPoints + 10); // +10 Points for Verification!

        // Check for Badges
        if (citizen.getKarmaPoints() >= 50) {
            citizen.setBadges("Model Citizen 🏅");
        } else if (citizen.getKarmaPoints() >= 20) {
            citizen.setBadges("Active Verifier 🛡️");
        }

        userRepository.save(citizen); // Save points
        return projectRepository.save(project);
    }
}