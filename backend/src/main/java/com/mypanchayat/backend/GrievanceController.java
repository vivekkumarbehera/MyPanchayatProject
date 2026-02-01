package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grievances")
@CrossOrigin(origins = "*") // Allows Frontend to connect
public class GrievanceController {

    @Autowired
    private GrievanceRepository grievanceRepository;

    @Autowired
    private UserRepository userRepository;

    // --- 1. SUBMIT GRIEVANCE (Citizen) ---
    // URL: http://localhost:8080/api/grievances/submit
    @PostMapping("/submit")
    public Grievance submitGrievance(@RequestBody Map<String, Object> request) {

        // 1. Find the User who is submitting
        Long userId = Long.valueOf(String.valueOf(request.get("userId")));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Create the Grievance Object
        Grievance g = new Grievance();
        g.setWardNumber((String) request.get("wardNumber"));
        g.setDescription((String) request.get("description"));
        g.setMediaType((String) request.get("mediaType"));

        // In a real app, this is where you'd handle file upload logic.
        // For now, we store the file name sent from frontend.
        g.setMediaUrl((String) request.get("fileName"));

        g.setUser(user); // Link grievance to the User

        return grievanceRepository.save(g);
    }

    // --- 2. GET GRIEVANCES FOR SARPANCH ---
    // URL: http://localhost:8080/api/grievances/panchayat/{panchayatName}
    @GetMapping("/panchayat/{panchayatName}")
    public List<Grievance> getGrievancesForSarpanch(@PathVariable String panchayatName) {
        System.out.println("Sarpanch requested grievances for: " + panchayatName);
        return grievanceRepository.findByUserPanchayat(panchayatName);
    }
    // --- 3. SARPANCH ACTION: UPDATE STATUS ---
    @PutMapping("/{id}/update")
    public Grievance updateStatus(@PathVariable Long id, @RequestParam String status) {
        return grievanceRepository.findById(id).map(g -> {
            g.setStatus(status);
            return grievanceRepository.save(g);
        }).orElseThrow(() -> new RuntimeException("Grievance not found"));
    }
}