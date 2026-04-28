package com.technicalbeep.techeventsmap.repository;

import com.technicalbeep.techeventsmap.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID>, JpaSpecificationExecutor<Event> {

    /**
     * Featured, public, on-map events from {@code now} onward, nearest upcoming first.
     * {@link Pageable} should use page 0 and the desired page size as the cap (e.g. top 10).
     */
    @Query("""
            SELECT e FROM Event e
            WHERE e.featured = true
              AND e.published = true
              AND e.showOnMap = true
              AND e.dateTime >= :now
            ORDER BY e.dateTime ASC
            """)
    List<Event> findFeaturedPublicUpcoming(@Param("now") Instant now, Pageable pageable);
}
