package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MarketplaceService {

    @Autowired
    private MarketListingRepository repository;

    @Autowired
    private UserRepository userRepository;

    // Post a new Listing
    public MarketListing createListing(MarketListing listing, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        listing.setPostedBy(user);
        return repository.save(listing);
    }

    public List<MarketListing> getAllListings() {
        return repository.findAll();
    }

    public List<MarketListing> getByType(String type) {
        return repository.findByType(type);
    }
}