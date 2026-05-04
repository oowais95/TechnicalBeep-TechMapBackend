package com.technicalbeep.techeventsmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.technicalbeep.techeventsmap.config")
public class TechEventsMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechEventsMapApplication.class, args);
    }
}
