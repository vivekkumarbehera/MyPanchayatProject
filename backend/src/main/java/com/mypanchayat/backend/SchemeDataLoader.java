package com.mypanchayat.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class SchemeDataLoader {

    @Bean
    CommandLineRunner initDatabase(SchemeRepository repository) {
        return args -> {
            // Check if data already exists so we don't duplicate it
            if (repository.count() == 0) {

                // SCHEME 1: PM Awas Yojana
                Scheme s1 = new Scheme();
                s1.setName("Pradhan Mantri Awas Yojana (Gramin)");
                s1.setDescription("Provides financial assistance to rural poor for constructing their own pucca house. The scheme aims to provide a pucca house with basic amenities to all houseless households.");
                s1.setEligibility("Families without shelter, destitute, manual scavengers, primitive tribal groups, and legally released bonded labourers.");
                s1.setBenefitAmount(120000.00);
                s1.setAnnouncedDate(LocalDate.of(2016, 4, 1));
                repository.save(s1);

                // SCHEME 2: PM Kisan Samman Nidhi
                Scheme s2 = new Scheme();
                s2.setName("PM Kisan Samman Nidhi");
                s2.setDescription("An income support scheme for all landholding farmers' families in the country. 100% funding from the Government of India.");
                s2.setEligibility("All landholding farmer families having cultivable landholding in their names.");
                s2.setBenefitAmount(6000.00); // Per year
                s2.setAnnouncedDate(LocalDate.of(2019, 2, 24));
                repository.save(s2);

                // SCHEME 3: Ayushman Bharat (Health)
                Scheme s3 = new Scheme();
                s3.setName("Ayushman Bharat - PMJAY");
                s3.setDescription("World's largest health insurance scheme fully financed by the government. Provides a cover of Rs. 5 lakhs per family per year for secondary and tertiary care hospitalization.");
                s3.setEligibility("Households included in the SECC 2011 database based on deprivation criteria.");
                s3.setBenefitAmount(500000.00);
                s3.setAnnouncedDate(LocalDate.of(2018, 9, 23));
                repository.save(s3);

                // SCHEME 4: MGNREGA
                Scheme s4 = new Scheme();
                s4.setName("MGNREGA (Employment Guarantee)");
                s4.setDescription("Guarantees 100 days of wage employment in a financial year to a rural household whose adult members volunteer to do unskilled manual work.");
                s4.setEligibility("Any rural household willing to do unskilled manual work.");
                s4.setBenefitAmount(22000.00); // Approx annual wages
                s4.setAnnouncedDate(LocalDate.of(2005, 8, 25));
                repository.save(s4);

                // SCHEME 5: Sukanya Samriddhi Yojana
                Scheme s5 = new Scheme();
                s5.setName("Sukanya Samriddhi Yojana");
                s5.setDescription("A small deposit scheme for the girl child launched as a part of the 'Beti Bachao Beti Padhao' campaign.");
                s5.setEligibility("Parents or legal guardians of a girl child below the age of 10 years.");
                s5.setBenefitAmount(0.00); // Varies by interest
                s5.setAnnouncedDate(LocalDate.of(2015, 1, 22));
                repository.save(s5);

                System.out.println("✅ REAL GOVERNMENT DATA LOADED SUCCESSFULLY!");
            }
        };
    }
}