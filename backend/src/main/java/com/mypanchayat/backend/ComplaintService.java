package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository repository;

    // The "Knowledge Base"
    private static final Map<String, String> RULES = new HashMap<>();

    static {
        // Water
        RULES.put("leak", "Water Dept");
        RULES.put("water", "Water Dept");
        RULES.put("pipe", "Water Dept");
        RULES.put("pani", "Water Dept"); // Odia

        // Roads
        RULES.put("road", "Roads & Building");
        RULES.put("pothole", "Roads & Building");
        RULES.put("rasta", "Roads & Building"); // Odia

        // Electricity
        RULES.put("light", "Electricity Dept");
        RULES.put("pole", "Electricity Dept");
        RULES.put("bijuli", "Electricity Dept"); // Odia
    }

    public Complaint registerComplaint(Complaint complaint) {
        // 1. AI Logic: Auto-assign Department
        String text = complaint.getDescription().toLowerCase();
        String assignedCategory = "General Admin"; // Default

        for (String keyword : RULES.keySet()) {
            if (text.contains(keyword)) {
                assignedCategory = RULES.get(keyword);
                break; // Found a match, stop looking
            }
        }

        complaint.setCategory(assignedCategory);

        // 2. 5T Logic: Set Auto-Deadline (Today + 3 Days)
        complaint.setCreatedDate(LocalDate.now());
        complaint.setDeadlineDate(LocalDate.now().plusDays(3));
        complaint.setStatus("OPEN");

        // 3. Save to Database
        return repository.save(complaint);
    }
}// Daily Update Check