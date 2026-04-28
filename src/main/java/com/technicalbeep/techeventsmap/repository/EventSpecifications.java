package com.technicalbeep.techeventsmap.repository;

import com.technicalbeep.techeventsmap.entity.Event;
import com.technicalbeep.techeventsmap.enums.EventCategory;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

/**
 * Atomic JPA {@link Specification} building blocks for {@link Event} queries.
 * <p>
 * List composition is handled by {@link EventListSpecifications}. Using Specifications keeps
 * optional filters and visibility rules type-safe and composable without QueryDSL codegen.
 */
public final class EventSpecifications {

    private EventSpecifications() {
    }

    /**
     * Public map feed: {@code dateTime >= now} in UTC instant semantics, {@code published},
     * {@code showOnMap}. Matches {@code TIMESTAMPTZ} + Hibernate {@code hibernate.jdbc.time_zone=UTC}.
     */
    public static Specification<Event> isPublicAt(Instant nowUtc) {
        return (root, query, cb) -> cb.and(
                cb.greaterThanOrEqualTo(root.get("dateTime"), nowUtc),
                cb.isTrue(root.get("published")),
                cb.isTrue(root.get("showOnMap"))
        );
    }

    public static Specification<Event> isFeatured() {
        return (root, query, cb) -> cb.isTrue(root.get("featured"));
    }

    public static Specification<Event> hasCategory(EventCategory category) {
        if (category == null) {
            return unconstrained();
        }
        return (root, query, cb) -> cb.equal(root.get("category"), category);
    }

    /**
     * Case-insensitive partial match on {@code title} OR {@code city} (for {@code search} / {@code q}).
     */
    public static Specification<Event> titleOrCityContainsIgnoreCase(String text) {
        if (text == null || text.isBlank()) {
            return unconstrained();
        }
        String pattern = "%" + text.strip().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), pattern),
                cb.like(cb.lower(root.get("city")), pattern)
        );
    }

    private static Specification<Event> unconstrained() {
        return (root, query, cb) -> cb.conjunction();
    }
}
