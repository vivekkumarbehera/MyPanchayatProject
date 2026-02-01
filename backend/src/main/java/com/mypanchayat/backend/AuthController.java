package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allows your HTML frontend to talk to this backend
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // Temporary storage for OTPs (Ideally, move this logic entirely inside EmailService)
    private static final Map<String, String> otpStorage = new HashMap<>();

    // --- 1. SEND OTP ENDPOINT ---
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        // 1. Validate Email
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required."));
        }

        // 2. Check if already registered
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered. Please Login."));
        }

        try {
            // 3. Send OTP
            emailService.generateOtp(email);
            return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Failed to send email: " + e.getMessage()));
        }
    }
    // --- 2. REGISTER USER (UPDATED TO FIX OTP ERROR) ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otpInput = request.get("otp");
        String role = request.get("role");

        // ✅ FIX: Skip OTP check if it is the "000000" dummy code (used for Sarpanch registration)
        if (!"000000".equals(otpInput)) {
            if (!emailService.verifyOtp(email, otpInput)) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid or Expired OTP"));
            }
        }

        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered!"));
        }

        User user = new User();
        user.setName(request.get("name"));
        user.setEmail(email);
        user.setRole(role);
        user.setDistrict(request.get("district"));
        user.setBlock(request.get("block"));
        user.setPanchayat(request.get("panchayat"));

        // B. Handle Roles
        if ("Sarpanch".equalsIgnoreCase(role)) {
            user.setStatus("PENDING");
            user.setPassword("WAITING_FOR_ADMIN");
        } else {
            user.setStatus("APPROVED");
            user.setPassword(request.get("password"));
        }

        try {
            userRepository.save(user);

            if ("Sarpanch".equalsIgnoreCase(role)) {
                return ResponseEntity.ok(Map.of("message", "Application Submitted. Wait for Admin Approval via Email."));
            } else {
                return ResponseEntity.ok(user);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error saving user: " + e.getMessage()));
        }
    }

    // --- 3. LOGIN USER (FIXED: Now returns JSON errors) ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        return userRepository.findByEmail(email)
                .map(user -> {
                    // Check Password
                    if (!user.getPassword().equals(password)) {
                        return ResponseEntity.badRequest().body(Map.of("message", "Wrong Password"));
                    }

                    // Check Status
                    if ("PENDING".equalsIgnoreCase(user.getStatus())) {
                        return ResponseEntity.status(403).body(Map.of("message", "Account under review. Please wait for email approval."));
                    }

                    return ResponseEntity.ok(user); // Returns User Object as JSON
                })
                .orElse(ResponseEntity.badRequest().body(Map.of("message", "User not found")));
    }

    // --- 4. ADMIN: GET PENDING REQUESTS ---
    @GetMapping("/pending-users")
    public List<User> getPendingUsers() {
        return userRepository.findAll().stream()
                .filter(u -> "PENDING".equalsIgnoreCase(u.getStatus()))
                .collect(Collectors.toList());
    }

    // --- 5. ADMIN: APPROVE USER ---
    @PostMapping("/approve-user")
    public ResponseEntity<?> approveUser(@RequestParam Long userId) {
        return userRepository.findById(userId).map(user -> {

            String newPassword = UUID.randomUUID().toString().substring(0, 8);

            user.setStatus("APPROVED");
            user.setPassword(newPassword);
            userRepository.save(user);

            emailService.sendApprovalEmail(user.getEmail(), user.getName(), newPassword);

            return ResponseEntity.ok(Map.of("message", "User Approved & Email Sent"));
        }).orElse(ResponseEntity.notFound().build());
    }
}