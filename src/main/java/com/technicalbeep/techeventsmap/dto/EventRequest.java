package com.technicalbeep.techeventsmap.dto;

import com.technicalbeep.techeventsmap.enums.EventCategory;
import com.technicalbeep.techeventsmap.validation.OptionalUrl;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record EventRequest(
        @NotBlank @Size(max = 500) String title,
        @Size(max = 10000) String description,
        @NotNull(message = "dateTime is required") Instant dateTime,
        @NotBlank @Size(max = 500) String venue,
        @NotBlank @Size(max = 200) String city,
        @NotNull @DecimalMin(value = "-90.0", inclusive = true, message = "latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", inclusive = true, message = "latitude must be between -90 and 90")
        Double latitude,
        @NotNull @DecimalMin(value = "-180.0", inclusive = true, message = "longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", inclusive = true, message = "longitude must be between -180 and 180")
        Double longitude,
        @NotNull(message = "category is required") EventCategory category,
        boolean featured,
        boolean published,
        boolean showOnMap,
        @OptionalUrl @Size(max = 2048) String externalUrl
) {
}
