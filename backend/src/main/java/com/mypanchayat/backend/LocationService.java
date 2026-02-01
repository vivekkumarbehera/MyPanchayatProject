package com.mypanchayat.backend;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class LocationService {

    private Map<String, Map<String, List<String>>> locationData = new TreeMap<>();

    @PostConstruct
    public void loadCsvData() {
        System.out.println("🔥 SYSTEM START: Attempting to load CSV...");

        // 1. TRY LOADING FROM FILE
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("odisha_locations.csv");

            if (is == null) {
                System.err.println("❌ ERROR: 'odisha_locations.csv' NOT FOUND in resources!");
                // FALLBACK: Load a fake location so the login page isn't empty
                addManualLocation("Demo District", "Demo Block", "Demo Panchayat");
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String line;
                boolean isHeader = true;

                while ((line = br.readLine()) != null) {
                    if (isHeader) { isHeader = false; continue; }
                    String[] data = line.split(",");
                    if (data.length >= 3) {
                        // Add to map
                        locationData
                                .computeIfAbsent(data[0].trim(), k -> new TreeMap<>())
                                .computeIfAbsent(data[1].trim(), k -> new ArrayList<>())
                                .add(data[2].trim());
                    }
                }
                System.out.println("✅ SUCCESS: CSV Loaded. Found " + locationData.size() + " districts.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper to add data manually
    private void addManualLocation(String dist, String block, String gp) {
        locationData
                .computeIfAbsent(dist, k -> new TreeMap<>())
                .computeIfAbsent(block, k -> new ArrayList<>())
                .add(gp);
    }

    public Map<String, Map<String, List<String>>> getAllLocations() {
        return locationData;
    }
}