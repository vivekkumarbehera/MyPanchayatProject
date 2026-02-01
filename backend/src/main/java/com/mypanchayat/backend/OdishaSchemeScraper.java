package com.mypanchayat.backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
@EnableScheduling // This turns on the "Automatic Timer"
public class OdishaSchemeScraper {

    @Autowired
    private SchemeRepository schemeRepository;

    // Run this every 24 hours (86400000 milliseconds)
    // Or simpler: cron = "0 0 12 * * ?" (Runs at 12 PM daily)
    @Scheduled(fixedRate = 86400000)
    public void scrapeOdishaSchemes() {
        System.out.println("🤖 SCRAPER STARTED: Looking for new Odisha Government Schemes...");

        try {
            // TARGET: We are scraping a generic Govt Schemes news page
            // (Note: For a real project, you would use the specific Odisha portal URL)
            String url = "https://myscheme.gov.in/search";

            // Connect to the website
            Document doc = Jsoup.connect(url).get();

            // Find elements (This depends on the website's HTML structure)
            // Example: Finding all headers with class "scheme-card"
            // NOTE: You have to inspect the real website to find the actual class names!
            Elements schemeCards = doc.select(".scheme-card"); // This is a guess/example class

            for (Element card : schemeCards) {
                String title = card.select("h3").text();
                String description = card.select("p").text();

                // Check if we already have this scheme
                if (schemeRepository.findAll().stream().noneMatch(s -> s.getName().equals(title))) {

                    Scheme newScheme = new Scheme();
                    newScheme.setName(title);
                    newScheme.setDescription(description);
                    newScheme.setEligibility("See official website for details");
                    newScheme.setBenefitAmount(0.0); // Default
                    newScheme.setAnnouncedDate(LocalDate.now());

                    schemeRepository.save(newScheme);
                    System.out.println("✨ NEW SCHEME FOUND: " + title);
                }
            }

        } catch (IOException e) {
            System.out.println("❌ SCRAPER ERROR: Could not connect to internet or website changed.");
        }

        System.out.println("🤖 SCRAPER FINISHED.");
    }
}