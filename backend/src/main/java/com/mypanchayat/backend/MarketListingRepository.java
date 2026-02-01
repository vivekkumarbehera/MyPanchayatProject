package com.mypanchayat.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarketListingRepository extends JpaRepository<MarketListing, Long> {
    // Find everything someone is "Selling" or "Requesting"
    List<MarketListing> findByType(String type);

    // Find by Category (e.g., "Agriculture", "Labor")
    List<MarketListing> findByCategory(String category);
}