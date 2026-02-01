package com.mypanchayat.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VillageProjectRepository extends JpaRepository<VillageProject, Long> {
    // Find projects for a specific Panchayat
    List<VillageProject> findByPanchayatId(Long panchayatId);

    // Find projects by status (e.g., "In-Progress")
    List<VillageProject> findByStatus(String status);
}