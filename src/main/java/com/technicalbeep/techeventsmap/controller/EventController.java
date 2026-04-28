package com.technicalbeep.techeventsmap.controller;

import com.technicalbeep.techeventsmap.dto.EventRequest;
import com.technicalbeep.techeventsmap.dto.EventResponse;
import com.technicalbeep.techeventsmap.enums.EventCategory;
import com.technicalbeep.techeventsmap.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<EventResponse> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) EventCategory category,
            @RequestParam(defaultValue = "false") boolean listAll,
            @PageableDefault(size = 20, sort = "dateTime", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        log.debug("GET /events listAll={} category={} page={} size={}", listAll, category, pageable.getPageNumber(), pageable.getPageSize());
        return eventService.findEvents(search, q, category, listAll, pageable);
    }

    @GetMapping("/featured")
    @ResponseStatus(HttpStatus.OK)
    public List<EventResponse> featured() {
        log.debug("GET /events/featured");
        return eventService.findFeaturedEvents();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse create(@Valid @RequestBody EventRequest request) {
        log.info("POST /events create title={}", request.title());
        return eventService.create(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventResponse update(@PathVariable UUID id, @Valid @RequestBody EventRequest request) {
        log.info("PUT /events/{} update title={}", id, request.title());
        return eventService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        log.info("DELETE /events/{}", id);
        eventService.delete(id);
    }
}
