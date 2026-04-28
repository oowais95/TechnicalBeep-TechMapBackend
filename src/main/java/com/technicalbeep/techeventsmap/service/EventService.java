package com.technicalbeep.techeventsmap.service;

import com.technicalbeep.techeventsmap.dto.EventRequest;
import com.technicalbeep.techeventsmap.dto.EventResponse;
import com.technicalbeep.techeventsmap.enums.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface EventService {

    Page<EventResponse> findEvents(String search, String q, EventCategory category, boolean listAll, Pageable pageable);

    List<EventResponse> findFeaturedEvents();

    EventResponse create(EventRequest request);

    EventResponse update(UUID id, EventRequest request);

    void delete(UUID id);
}
