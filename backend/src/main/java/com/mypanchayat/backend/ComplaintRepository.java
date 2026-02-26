package com.mypanchayat.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // --- YOUR EXISTING METHODS (Kept safe so dashboard works) ---
    List<Complaint> findByUserId(Long userId);

    List<Complaint> findByPanchayatId(Long panchayatId);

    @Query("SELECT c FROM Complaint c WHERE LOWER(c.panchayat.name) = LOWER(:village)")
    List<Complaint> findComplaintsByVillage(@Param("village") String village);

    // ==========================================
    // --- NEW METHOD FOR AI CLUSTERING ---
    // ==========================================
    // This grabs only unresolved complaints in the same Panchayat.
    // The AI will use this list to check if someone already reported the same issue nearby!
    List<Complaint> findByPanchayatIdAndStatus(Long panchayatId, String status);
}