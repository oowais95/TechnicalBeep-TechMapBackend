package com.technicalbeep.techeventsmap.repository;

import com.technicalbeep.techeventsmap.entity.Event;
import org.springframework.data.jpa.domain.Specification;

/**
 * Composes {@link Specification}s for the main events list. Atomic predicates live in
 * {@link EventSpecifications} so this class stays focused on list-only rules.
 */
public final class EventListSpecifications {

    private EventListSpecifications() {
    }

    /**
     * Dynamic list: optional title/city search and category; {@code listAll} drops the public window.
     * {@code nowUtc} must come from the same {@link java.time.Clock} used app-wide (UTC).
     */
    public static Specification<Event> forList(EventListCriteria criteria) {
        Specification<Event> spec = Specification
                .where(EventSpecifications.titleOrCityContainsIgnoreCase(criteria.searchText()))
                .and(EventSpecifications.hasCategory(criteria.category()));

        if (!criteria.listAll()) {
            spec = spec.and(EventSpecifications.isPublicAt(criteria.nowUtc()));
        }
        return spec;
    }
}
