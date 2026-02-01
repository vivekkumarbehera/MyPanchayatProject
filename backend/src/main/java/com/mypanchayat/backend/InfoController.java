package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/info")
@CrossOrigin(origins = "*")
public class InfoController {

    @Autowired
    private InfoService infoService;

    @GetMapping("/weather")
    public Map<String, String> getWeather(@RequestParam String district) {
        return infoService.getWeather(district);
    }

    @GetMapping("/mandi")
    public Map<String, String> getMandiRates() {
        return infoService.getMandiPrices();
    }
}