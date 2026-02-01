package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/market")
@CrossOrigin(origins = "*")
public class MarketplaceController {

    @Autowired
    private MarketplaceService service;

    // 1. Get All Listings (e.g., "Tractor for Rent")
    @GetMapping
    public List<MarketListing> getAllListings() {
        return service.getAllListings();
    }

    // 2. Filter by Type (e.g., /api/market/type/OFFER or /api/market/type/REQUEST)
    @GetMapping("/type/{type}")
    public List<MarketListing> getByType(@PathVariable String type) {
        return service.getByType(type);
    }

    // 3. Post a New Listing
    // URL: POST /api/market?userId=1
    @PostMapping
    public MarketListing createListing(@RequestBody MarketListing listing, @RequestParam Long userId) {
        return service.createListing(listing, userId);
    }
}