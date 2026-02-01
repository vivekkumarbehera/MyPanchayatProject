package com.mypanchayat.backend;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class InfoService {

    // Simulating Real-time Weather for Odisha Districts
    public Map<String, String> getWeather(String district) {
        Map<String, String> weather = new HashMap<>();
        // In real life, call OpenWeatherMap API here
        weather.put("temp", "32°C");
        weather.put("condition", "Partly Cloudy");
        weather.put("humidity", "78%");
        weather.put("alert", "None");
        return weather;
    }

    // Simulating Mandi Prices (RMC)
    public Map<String, String> getMandiPrices() {
        Map<String, String> prices = new HashMap<>();
        prices.put("Paddy (Common)", "₹2040 / qtl");
        prices.put("Moong (Green Gram)", "₹7200 / qtl");
        prices.put("Groundnut", "₹5850 / qtl");
        return prices;
    }
}