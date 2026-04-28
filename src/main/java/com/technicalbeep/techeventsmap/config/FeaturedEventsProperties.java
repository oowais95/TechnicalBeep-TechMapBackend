package com.technicalbeep.techeventsmap.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration for {@code GET /events/featured} (max rows returned).
 */
@ConfigurationProperties(prefix = "app.events.featured")
@Validated
public class FeaturedEventsProperties {

    /**
     * Maximum number of featured events to return (nearest upcoming first).
     */
    @Min(1)
    @Max(100)
    private int limit = 10;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
