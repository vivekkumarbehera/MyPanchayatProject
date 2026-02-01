package com.mypanchayat.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This maps the URL "http://localhost:8080/uploads/..."
        // to the physical folder "uploads" in your project directory.

        String projectDir = System.getProperty("user.dir"); // Gets current project folder

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + projectDir + "/uploads/");
    }
}