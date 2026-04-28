package com.technicalbeep.techeventsmap.service.impl;

import com.technicalbeep.techeventsmap.dto.EventRequest;
import com.technicalbeep.techeventsmap.dto.EventResponse;
import com.technicalbeep.techeventsmap.entity.Event;
import com.technicalbeep.techeventsmap.enums.EventCategory;
import com.technicalbeep.techeventsmap.exception.ResourceNotFoundException;
import com.technicalbeep.techeventsmap.mapper.EventMapper;
import com.technicalbeep.techeventsmap.config.FeaturedEventsProperties;
import com.technicalbeep.techeventsmap.repository.EventListCriteria;
import com.technicalbeep.techeventsmap.repository.EventListSpecifications;
import com.technicalbeep.techeventsmap.repository.EventRepository;
import com.technicalbeep.techeventsmap.service.EventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final Clock clock;
    private final FeaturedEventsProperties featuredEventsProperties;

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> findEvents(
            String search,
            String q,
            EventCategory category,
            boolean listAll,
            Pageable pageable
    ) {
        String searchText = firstNonBlank(search, q);
        Instant nowUtc = clock.instant();
        EventListCriteria criteria = new EventListCriteria(searchText, category, listAll, nowUtc);
        Specification<Event> spec = EventListSpecifications.forList(criteria);

        return eventRepository.findAll(spec, pageable).map(eventMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> findFeaturedEvents() {
        Instant nowUtc = clock.instant();
        int cap = featuredEventsProperties.getLimit();
        Pageable top = PageRequest.of(0, cap, Sort.by(Sort.Direction.ASC, "dateTime"));
        log.debug("findFeaturedEvents now={} limit={}", nowUtc, cap);
        return eventRepository.findFeaturedPublicUpcoming(nowUtc, top).stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public EventResponse create(EventRequest request) {
        Event entity = eventMapper.toEntity(request);
        Event saved = eventRepository.save(entity);
        log.info("Created event id={} title={}", saved.getId(), saved.getTitle());
        return eventMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public EventResponse update(UUID id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));
        eventMapper.update(event, request);
        log.info("Updated event id={} title={}", id, event.getTitle());
        return eventMapper.toResponse(event);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found: " + id);
        }
        eventRepository.deleteById(id);
        log.info("Deleted event id={}", id);
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) {
            return a.strip();
        }
        if (b != null && !b.isBlank()) {
            return b.strip();
        }
        return null;
    }
}
