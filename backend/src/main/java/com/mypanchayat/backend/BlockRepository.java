package com.mypanchayat.backend;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    // Find all blocks inside a specific District
    List<Block> findByDistrictId(Long districtId);
}