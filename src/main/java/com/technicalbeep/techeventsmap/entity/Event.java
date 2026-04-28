package com.technicalbeep.techeventsmap.entity;

import com.technicalbeep.techeventsmap.enums.EventCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Indexes support common queries: by time window, category, city, and map bounds.
 * <p>
 * Suggested PostgreSQL indexes (mirrored in {@link Table#indexes()}):
 * <ul>
 *   <li>{@code date_time} — list upcoming / past events, sort by time</li>
 *   <li>{@code category} — filter by track</li>
 *   <li>{@code city} — filter by location name</li>
 *   <li>{@code (latitude, longitude)} — optional composite for bounding-box / proximity (consider PostGIS for heavy geo workloads)</li>
 *   <li>Flyway {@code V2__events_listing_indexes.sql} adds partial indexes for published + on-map list queries</li>
 * </ul>
 */
@Entity
@Table(
        name = "events",
        indexes = {
                @Index(name = "idx_events_date_time", columnList = "date_time"),
                @Index(name = "idx_events_category", columnList = "category"),
                @Index(name = "idx_events_city", columnList = "city"),
                @Index(name = "idx_events_lat_lon", columnList = "latitude,longitude")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "date_time", nullable = false)
    private Instant dateTime;

    @Column(nullable = false, length = 500)
    private String venue;

    @Column(nullable = false, length = 200)
    private String city;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private EventCategory category;

    @Column(nullable = false)
    private boolean featured;

    @Column(nullable = false)
    private boolean published;

    @Column(name = "show_on_map", nullable = false)
    private boolean showOnMap;

    @Column(name = "external_url", length = 2048)
    private String externalUrl;
}
