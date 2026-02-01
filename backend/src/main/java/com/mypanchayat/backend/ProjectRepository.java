package com.mypanchayat.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Find projects by status: "Planned", "In-Progress", "Completed"
    List<Project> findByStatus(String status);
}