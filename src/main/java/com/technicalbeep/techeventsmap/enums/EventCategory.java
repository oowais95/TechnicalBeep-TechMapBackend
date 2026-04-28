package com.technicalbeep.techeventsmap.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Persisted as enum {@link #name()} (e.g. WEB). JSON uses the labels from the product spec.
 */
public enum EventCategory {
    @JsonProperty("AI")
    AI,
    @JsonProperty("Web")
    WEB,
    @JsonProperty("Cloud")
    CLOUD,
    @JsonProperty("Mobile")
    MOBILE,
    @JsonProperty("Security")
    SECURITY,
    @JsonProperty("Data")
    DATA
}
