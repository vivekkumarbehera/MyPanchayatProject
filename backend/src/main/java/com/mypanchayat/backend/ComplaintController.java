package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping({"/api/complaints", "/api/grievances"}) // Works for both URLs!
@CrossOrigin(origins = "*")
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private PanchayatRepository panchayatRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private BlockRepository blockRepository;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    // --- 1. SUBMIT COMPLAINT (NOW WITH AI & GPS) ---
    @PostMapping
    public ResponseEntity<?> createComplaint(
            @RequestParam("citizenName") String citizenName,
            @RequestParam(value = "mobileNumber", required = false) String mobileNumber,
            @RequestParam("description") String description,
            @RequestParam(value = "ward", required = false) String ward,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "panchayatId", required = false) Long panchayatId,
            @RequestParam(value = "panchayatName", required = false) String panchayatName,
            @RequestParam(value = "latitude", required = false) Double latitude,   // NEW GPS
            @RequestParam(value = "longitude", required = false) Double longitude, // NEW GPS
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "video", required = false) MultipartFile video,
            @RequestParam(value = "audio", required = false) MultipartFile audio
    ) {
        try {
            System.out.println("📥 Received Complaint Submission from: " + citizenName);

            // --- FIND PANCHAYAT ---
            Panchayat panchayat = null;
            if (panchayatId != null) panchayat = panchayatRepository.findById(panchayatId).orElse(null);
            if (panchayat == null && panchayatName != null) panchayat = panchayatRepository.findByNameIgnoreCase(panchayatName.trim()).orElse(null);

            // Auto-Create Panchayat if missing
            if (panchayat == null && panchayatName != null) {
                Panchayat newP = new Panchayat();
                newP.setName(panchayatName.trim());
                District dummyDist = districtRepository.findAll().stream().findFirst().orElseGet(() -> districtRepository.save(new District("Unknown District")));
                newP.setDistrict(dummyDist);
                Block dummyBlock = blockRepository.findAll().stream().findFirst().orElseGet(() -> blockRepository.save(new Block("Unknown Block", dummyDist)));
                newP.setBlock(dummyBlock);
                newP.setSarpanchName("Pending Assignment");
                panchayat = panchayatRepository.save(newP);
            }

            if (panchayat == null) return ResponseEntity.badRequest().body(Map.of("error", "Panchayat missing."));

            // ==========================================
            // --- THE AI CLUSTERING ENGINE ---
            // ==========================================
            List<Complaint> activeIssues = complaintRepository.findByPanchayatIdAndStatus(panchayat.getId(), "Pending");

            for (Complaint existing : activeIssues) {
                boolean isNear = false;

                // 1. Check Distance (if GPS exists)
                if (latitude != null && longitude != null && existing.getLatitude() != null && existing.getLongitude() != null) {
                    double distance = AIEngine.calculateDistance(latitude, longitude, existing.getLatitude(), existing.getLongitude());
                    isNear = (distance <= 50.0); // Within 50 meters!
                } else {
                    // Fallback: If no GPS, check if they are in the exact same ward
                    isNear = (ward != null && ward.equalsIgnoreCase(existing.getWard()));
                }

                // 2. Check Text Similarity
                if (isNear) {
                    double similarity = AIEngine.calculateSimilarity(description, existing.getDescription());
                    System.out.println("🤖 AI Check - Distance Matched. Text Similarity: " + (similarity * 100) + "%");

                    if (similarity >= 0.50) { // 50% Match
                        System.out.println("🛑 SPAM/DUPLICATE PREVENTED! Merging into existing issue ID: " + existing.getId());

                        existing.incrementReportCount(); // Add +1 to the report count!

                        // Boost urgency because multiple people are complaining!
                        double newUrgency = existing.getUrgencyScore() + 5.0;
                        existing.setUrgencyScore(Math.min(newUrgency, 100.0));

                        complaintRepository.save(existing);
                        return ResponseEntity.ok(Map.of("message", "Merged with an existing issue nearby. Thank you!"));
                    }
                }
            }

            // ==========================================
            // --- IF BRAND NEW ISSUE, SAVE IT ---
            // ==========================================
            Complaint c = new Complaint();
            c.setCitizenName(citizenName);
            c.setMobileNumber(mobileNumber);
            c.setDescription(description);
            c.setPanchayat(panchayat);
            c.setUserId(userId);
            c.setWard(ward);
            c.setLatitude(latitude);   // Save GPS
            c.setLongitude(longitude); // Save GPS
            c.setCreatedDate(java.time.LocalDate.now());
            c.setStatus("Pending");
            c.setUpvoteCount(0);

            // Set AI Data
            c.setReportCount(1);
            c.setUrgencyScore(AIEngine.calculateUrgency(description));

            // Save Files
            if (photo != null && !photo.isEmpty()) c.setPhotoUrl(saveFile(photo, "image"));
            if (video != null && !video.isEmpty()) c.setVideoUrl(saveFile(video, "video"));
            if (audio != null && !audio.isEmpty()) c.setAudioUrl(saveFile(audio, "audio"));

            complaintRepository.save(c);
            System.out.println("✅ New Smart Complaint Saved Successfully!");

            return ResponseEntity.ok(Map.of("message", "Complaint Submitted Successfully!"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Server Error: " + e.getMessage()));
        }
    }

    // --- 2. GET DASHBOARD DATA ---
    @GetMapping
    public List<Complaint> getAllComplaints(
            @RequestParam(required = false) String panchayatName,
            @RequestParam(required = false) Long panchayatId,
            @RequestParam(required = false) Long userId
    ) {
        if (userId != null) return complaintRepository.findByUserId(userId);
        if (panchayatId != null) return complaintRepository.findByPanchayatId(panchayatId);
        if (panchayatName != null && !panchayatName.trim().isEmpty()) return complaintRepository.findComplaintsByVillage(panchayatName.trim());
        return complaintRepository.findAll();
    }

    @GetMapping("/panchayat/{name}")
    public List<Complaint> getByPanchayatName(@PathVariable String name) {
        return complaintRepository.findComplaintsByVillage(name.trim());
    }

    // --- 3. UPVOTE ---
    @PostMapping("/{id}/upvote")
    public ResponseEntity<?> upvoteComplaint(@PathVariable Long id, @RequestParam Long userId) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(id);
        if (complaintOpt.isPresent()) {
            Complaint c = complaintOpt.get();
            c.setUpvoteCount(c.getUpvoteCount() + 1);
            complaintRepository.save(c);
            return ResponseEntity.ok(Map.of("message", "Upvoted!", "newCount", c.getUpvoteCount()));
        }
        return ResponseEntity.notFound().build();
    }

    // --- 4. STATUS UPDATE ---
    @PutMapping("/{id}/update")
    public ResponseEntity<Complaint> updateComplaintStatus(@PathVariable Long id, @RequestParam String status) {
        return complaintRepository.findById(id).map(c -> {
            c.setStatus(status);
            return ResponseEntity.ok(complaintRepository.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- 5. FILE SAVING HELPER ---
    private String saveFile(MultipartFile file, String type) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String originalName = file.getOriginalFilename();
        if(originalName == null) originalName = "file";
        originalName = originalName.replaceAll("[^a-zA-Z0-9\\.]", "_");

        if (type.equals("audio") && !originalName.contains(".")) {
            originalName += ".webm";
        }

        String fileName = System.currentTimeMillis() + "_" + originalName;
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + fileName;
    }
}