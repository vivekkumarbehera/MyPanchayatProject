package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*") // Allows your HTML to access this
public class LocationController {

    @Autowired
    private LocationService locationService;

    // This single endpoint returns the entire District -> Block -> Panchayat tree
    @GetMapping
    public Map<String, Map<String, List<String>>> getLocations() {
        return locationService.getAllLocations();
    }
}