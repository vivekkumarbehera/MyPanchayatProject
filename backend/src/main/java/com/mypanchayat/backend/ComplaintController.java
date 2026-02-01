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
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*") // Allows requests from all computers on the network
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private PanchayatRepository panchayatRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private BlockRepository blockRepository;

    // Folder for uploads
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    // --- 1. SUBMIT COMPLAINT ---
    @PostMapping
    public ResponseEntity<?> createComplaint(
            @RequestParam("citizenName") String citizenName,
            @RequestParam("mobileNumber") String mobileNumber,
            @RequestParam("description") String description,
            @RequestParam("ward") String ward,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "panchayatId", required = false) Long panchayatId,
            @RequestParam(value = "panchayatName", required = false) String panchayatName,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "video", required = false) MultipartFile video,
            @RequestParam(value = "audio", required = false) MultipartFile audio
    ) {
        try {
            System.out.println("📥 Received Complaint Submission from: " + citizenName); // DEBUG LOG

            Panchayat panchayat = null;

            // 1. Try finding by ID
            if (panchayatId != null) {
                panchayat = panchayatRepository.findById(panchayatId).orElse(null);
            }

            // 2. Try finding by Name (Case Insensitive & Trimmed)
            if (panchayat == null && panchayatName != null) {
                String cleanName = panchayatName.trim();
                panchayat = panchayatRepository.findByNameIgnoreCase(cleanName).orElse(null);
                System.out.println("🔍 Searching for Panchayat: '" + cleanName + "' -> Found: " + (panchayat != null));
            }

            // 3. Auto-Create if missing (Fallback for manual entries)
            if (panchayat == null && panchayatName != null) {
                System.out.println("⚠️ Panchayat not found. Creating new entry: " + panchayatName);
                Panchayat newP = new Panchayat();
                newP.setName(panchayatName.trim());

                // Assign dummy district/block to prevent null errors
                District dummyDist = districtRepository.findAll().stream().findFirst()
                        .orElseGet(() -> districtRepository.save(new District("Unknown District")));
                newP.setDistrict(dummyDist);

                Block dummyBlock = blockRepository.findAll().stream().findFirst()
                        .orElseGet(() -> blockRepository.save(new Block("Unknown Block", dummyDist)));
                newP.setBlock(dummyBlock);

                newP.setSarpanchName("Pending Assignment");
                panchayat = panchayatRepository.save(newP);
            }

            if (panchayat == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Panchayat Name is missing or invalid."));
            }

            // --- SAVE COMPLAINT ---
            Complaint c = new Complaint();
            c.setCitizenName(citizenName);
            c.setMobileNumber(mobileNumber);
            c.setDescription(description);
            c.setPanchayat(panchayat); // ✅ Links complaint to specific Panchayat
            c.setUserId(userId);
            c.setWard(ward);
            c.setCreatedDate(java.time.LocalDate.now());
            c.setStatus("Pending");
            c.setUpvoteCount(0);

            // SAVE FILES
            if (photo != null && !photo.isEmpty()) c.setPhotoUrl(saveFile(photo, "image"));
            if (video != null && !video.isEmpty()) c.setVideoUrl(saveFile(video, "video"));
            if (audio != null && !audio.isEmpty()) c.setAudioUrl(saveFile(audio, "audio"));

            complaintRepository.save(c);
            System.out.println("✅ Complaint Saved Successfully!");

            return ResponseEntity.ok(Map.of("message", "Complaint Submitted Successfully!"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Server Error: " + e.getMessage()));
        }
    }

    // --- 2. GET ALL COMPLAINTS (THE FILTER LOGIC) ---
    @GetMapping
    public List<Complaint> getAllComplaints(
            @RequestParam(required = false) String panchayatName,
            @RequestParam(required = false) Long panchayatId,
            @RequestParam(required = false) Long userId
    ) {
        // CASE A: Citizen viewing their own history
        if (userId != null) {
            return complaintRepository.findByUserId(userId);
        }

        // CASE B: Sarpanch viewing specific Panchayat (by ID)
        if (panchayatId != null) {
            System.out.println("📡 Fetching complaints for Panchayat ID: " + panchayatId);
            return complaintRepository.findByPanchayatId(panchayatId);
        }

        // CASE C: Sarpanch viewing specific Panchayat (by Name) -> THIS IS THE CRITICAL FIX
        if (panchayatName != null && !panchayatName.trim().isEmpty()) {
            String cleanName = panchayatName.trim();
            System.out.println("📡 Fetching complaints for Panchayat Name: " + cleanName);
            return complaintRepository.findComplaintsByVillage(cleanName);
        }

        // CASE D: No Filter? This is the "Data Leak" cause.
        // We log a warning so you can see it in the console.
        System.out.println("⚠️ WARNING: A request came without filters! Returning ALL complaints (Global View).");
        return complaintRepository.findAll();
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

    // --- 5. HELPER: SAVE FILE ---
    private String saveFile(MultipartFile file, String type) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String originalName = file.getOriginalFilename();
        if(originalName == null) originalName = "file";

        // Sanitize
        originalName = originalName.replaceAll("[^a-zA-Z0-9\\.]", "_");

        // Fix Audio Extension if missing
        if (type.equals("audio") && !originalName.contains(".")) {
            originalName += ".webm";
        }

        String fileName = System.currentTimeMillis() + "_" + originalName;
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + fileName;
    }
}