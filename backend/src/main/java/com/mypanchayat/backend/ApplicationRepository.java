package com.mypanchayat.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // Find applications by the User's ID (Standard)
    List<Application> findByUserId(Long userId);

    // --- FIX IS HERE ---
    // Change "PanchayatId" to just "Panchayat" so it matches the String field in User
    List<Application> findByUserPanchayat(String panchayatName);
}