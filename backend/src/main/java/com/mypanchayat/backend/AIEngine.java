package com.mypanchayat.backend;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AIEngine {

    // --- 1. CALCULATE GPS DISTANCE (Haversine Formula) ---
    // Returns distance in METERS between two coordinates
    public static double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return 999999.0; // Return a huge distance if GPS is missing
        }

        final int R = 6371; // Earth Radius in Kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 1000; // Convert to meters
    }

    // --- 2. CALCULATE TEXT SIMILARITY ---
    // Returns a score between 0.0 (completely different) and 1.0 (exact match)
    public static double calculateSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) return 0.0;

        // Clean up text: lowercase and remove special characters
        String cleanText1 = text1.toLowerCase().replaceAll("[^a-z0-9\\s]", "");
        String cleanText2 = text2.toLowerCase().replaceAll("[^a-z0-9\\s]", "");

        Set<String> set1 = new HashSet<>(Arrays.asList(cleanText1.split("\\s+")));
        Set<String> set2 = new HashSet<>(Arrays.asList(cleanText2.split("\\s+")));

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2); // Keep only matching words

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2); // Combine all unique words

        if (union.isEmpty()) return 0.0;
        return (double) intersection.size() / union.size();
    }

    // --- 3. CALCULATE URGENCY SCORE ---
    // Scans text for critical keywords to prioritize the issue
    public static double calculateUrgency(String description) {
        if (description == null) return 0.0;
        String text = description.toLowerCase();
        double score = 0.0;

        // CRITICAL (+50 points)
        String[] criticalWords = {"fire", "accident", "blood", "death", "wire", "electrocution", "snake", "collapse"};
        for (String word : criticalWords) {
            if (text.contains(word)) score += 50.0;
        }

        // HIGH (+20 points)
        String[] highWords = {"water", "pipe", "flooding", "broken", "theft", "stolen", "block", "hospital"};
        for (String word : highWords) {
            if (text.contains(word)) score += 20.0;
        }

        // MEDIUM (+5 points)
        String[] mediumWords = {"pothole", "garbage", "smell", "street light", "dark", "road"};
        for (String word : mediumWords) {
            if (text.contains(word)) score += 5.0;
        }

        // Cap the max score at 100.0
        return Math.min(score, 100.0);
    }
}