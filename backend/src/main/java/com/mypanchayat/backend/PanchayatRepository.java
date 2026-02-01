package com.mypanchayat.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // ✅ Add this import

@Repository
public interface PanchayatRepository extends JpaRepository<Panchayat, Long> {

    // ✅ Add this method to find by name
    Optional<Panchayat> findByName(String name);

    Optional<Panchayat> findByNameIgnoreCase(String name);
}