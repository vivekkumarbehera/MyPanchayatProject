package com.mypanchayat.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GrievanceRepository extends JpaRepository<Grievance, Long> {

    // This method automatically writes the SQL query for us.
    // It looks at the "User" inside the Grievance,
    // then checks that User's "panchayat" field.
    List<Grievance> findByUserPanchayat(String panchayatName);
}