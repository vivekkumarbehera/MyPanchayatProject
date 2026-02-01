package com.mypanchayat.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class MyPanchayatApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyPanchayatApplication.class, args);
    }

    @Transactional
    public void loadDataUncached(DistrictRepository districtRepo,
                                 BlockRepository blockRepo,
                                 PanchayatRepository panchayatRepo) {

        // Safety Check: Don't load if data exists
        if (districtRepo.count() > 0) {
            System.out.println("✅ Database already has data. Skipping CSV load.");
            return;
        }

        System.out.println("📂 Reading from 'odisha_locations.csv'...");
        long startTime = System.currentTimeMillis();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new ClassPathResource("odisha_locations.csv").getInputStream()))) {

            String line;
            boolean isHeader = true;

            // --- 1. PRE-LOAD CACHES ---
            Map<String, District> districtCache = districtRepo.findAll().stream()
                    .collect(Collectors.toMap(d -> d.getName().toLowerCase(), d -> d));

            Map<String, Block> blockCache = blockRepo.findAll().stream()
                    .collect(Collectors.toMap(
                            b -> b.getDistrict().getName().toLowerCase() + "_" + b.getName().toLowerCase(),
                            b -> b
                    ));

            List<Panchayat> panchayatsBatch = new ArrayList<>();

            // --- 2. PROCESS CSV ---
            while ((line = br.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }

                String[] data = line.split(",");
                if (data.length < 3) continue;

                String districtName = data[0].trim();
                String blockName = data[1].trim();
                String panchayatName = data[2].trim();

                // --- Handle District ---
                String distKey = districtName.toLowerCase();
                District district = districtCache.get(distKey);

                if (district == null) {
                    district = new District();
                    district.setName(districtName);
                    district = districtRepo.save(district);
                    districtCache.put(distKey, district);
                }

                // --- Handle Block ---
                String blockKey = distKey + "_" + blockName.toLowerCase();
                Block block = blockCache.get(blockKey);

                if (block == null) {
                    block = new Block();
                    block.setName(blockName);
                    block.setDistrict(district);
                    block = blockRepo.save(block);
                    blockCache.put(blockKey, block);
                }

                // --- Handle Panchayat ---
                Panchayat panchayat = new Panchayat();
                panchayat.setName(panchayatName);
                panchayat.setBlock(block);
                panchayatsBatch.add(panchayat);
            }

            // --- 3. BULK INSERT ---
            if (!panchayatsBatch.isEmpty()) {
                System.out.println("⚡ Batch inserting " + panchayatsBatch.size() + " panchayats...");
                panchayatRepo.saveAll(panchayatsBatch);
            }

            long endTime = System.currentTimeMillis();
            System.out.println("✅ SUCCESS: CSV Imported in " + (endTime - startTime) + "ms!");

        } catch (Exception e) {
            System.out.println("❌ Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}