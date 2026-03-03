package com.mypanchayat.backend;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

// 1. This allows your frontend to talk to this backend
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ai")
public class ChatController {

    @PostMapping("/chat")
    public Map<String, String> getAiResponse(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");

        // This logic is for testing. Next, we will connect this to the Gemini API.
        String aiReply = "Panchayat Mitra received: " + userMessage;

        Map<String, String> response = new HashMap<>();
        response.put("reply", aiReply);

        return response;
    }
}