package com.technicalbeep.techeventsmap.repository;

import com.technicalbeep.techeventsmap.enums.EventCategory;

import java.time.Instant;

/**
 * Resolved inputs for the paginated {@code GET /events} query (after merging {@code search} / {@code q}).
 */
public record EventListCriteria(
        String searchText,
        EventCategory category,
        boolean listAll,
        Instant nowUtc
) {
}
