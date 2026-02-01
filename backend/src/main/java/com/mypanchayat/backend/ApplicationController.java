package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    // --- 1. APPLY (Citizen submits a form) ---
    @PostMapping("/apply")
    public Application applyForCertificate(@RequestBody Map<String, Object> request) {

        // 1. Get the User ID safely
        Long userId = Long.valueOf(String.valueOf(request.get("userId")));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Create the Application
        Application app = new Application();
        app.setType((String) request.get("type"));
        app.setDescription((String) request.get("description"));
        app.setUser(user);

        // Status "PENDING" is set automatically by your Application.java constructor
        return applicationRepository.save(app);
    }

    // --- 2. HISTORY (Citizen sees their own applications) ---
    @GetMapping("/user/{userId}")
    public List<Application> getUserApplications(@PathVariable Long userId) {
        return applicationRepository.findByUserId(userId);
    }

    // --- 3. ADMIN: Get ALL applications for a specific Panchayat ---
    // --- CRITICAL FIX IS HERE ---
    // OLD (Broken): @GetMapping("/panchayat/{panchayatId}") -> findByUser_Panchayat_Id
    // NEW (Fixed):  Takes String Name to match the User Entity change
    @GetMapping("/panchayat/{panchayatName}")
    public List<Application> getPanchayatApplications(@PathVariable String panchayatName) {
        System.out.println("Fetching applications for Panchayat: " + panchayatName);
        return applicationRepository.findByUserPanchayat(panchayatName);
    }

    // --- 4. ADMIN: Approve or Reject Application ---
    @PostMapping("/update-status")
    public Application updateStatus(@RequestBody Map<String, Object> request) {
        Long appId = Long.valueOf(String.valueOf(request.get("appId")));
        String newStatus = (String) request.get("status");

        Application app = applicationRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(newStatus);
        return applicationRepository.save(app);
    }
}