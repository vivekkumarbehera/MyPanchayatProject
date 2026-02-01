package com.mypanchayat.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // 1. For Public Search (Partial Match)
    @Query("SELECT c FROM Complaint c WHERE LOWER(c.panchayat.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Complaint> searchByPanchayatName(@Param("name") String name);

    // 2. For User "My Applications"
    List<Complaint> findByUserId(Long userId);

    // 3. For Sarpanch Dashboard (Strict Filter by ID)
    List<Complaint> findByPanchayatId(Long panchayatId);

    // ✅ FIXED: MANUAL QUERY for Strict Name Search
    // We use @Query here to prevent the "Unable to ignore case" error.
    @Query("SELECT c FROM Complaint c WHERE LOWER(c.panchayat.name) = LOWER(:panchayatName)")
    List<Complaint> findComplaintsByVillage(@Param("panchayatName") String panchayatName);
}