package com.technicalbeep.techeventsmap.dto;

import com.technicalbeep.techeventsmap.enums.EventCategory;

import java.time.Instant;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String title,
        String description,
        Instant dateTime,
        String venue,
        String city,
        Double latitude,
        Double longitude,
        EventCategory category,
        boolean featured,
        boolean published,
        boolean showOnMap,
        String externalUrl
) {
}
