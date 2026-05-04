package com.technicalbeep.techeventsmap.mapper;

import com.technicalbeep.techeventsmap.dto.EventRequest;
import com.technicalbeep.techeventsmap.dto.EventResponse;
import com.technicalbeep.techeventsmap.entity.Event;
import com.technicalbeep.techeventsmap.enums.EventCategory;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:34:50+0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventResponse toResponse(Event event) {
        if ( event == null ) {
            return null;
        }

        UUID id = null;
        String title = null;
        String description = null;
        Instant dateTime = null;
        String venue = null;
        String city = null;
        Double latitude = null;
        Double longitude = null;
        EventCategory category = null;
        boolean featured = false;
        boolean published = false;
        boolean showOnMap = false;
        String externalUrl = null;

        id = event.getId();
        title = event.getTitle();
        description = event.getDescription();
        dateTime = event.getDateTime();
        venue = event.getVenue();
        city = event.getCity();
        latitude = event.getLatitude();
        longitude = event.getLongitude();
        category = event.getCategory();
        featured = event.isFeatured();
        published = event.isPublished();
        showOnMap = event.isShowOnMap();
        externalUrl = event.getExternalUrl();

        EventResponse eventResponse = new EventResponse( id, title, description, dateTime, venue, city, latitude, longitude, category, featured, published, showOnMap, externalUrl );

        return eventResponse;
    }

    @Override
    public Event toEntity(EventRequest request) {
        if ( request == null ) {
            return null;
        }

        Event.EventBuilder event = Event.builder();

        event.category( request.category() );
        event.city( request.city() );
        event.dateTime( request.dateTime() );
        event.description( request.description() );
        event.externalUrl( request.externalUrl() );
        event.featured( request.featured() );
        event.latitude( request.latitude() );
        event.longitude( request.longitude() );
        event.published( request.published() );
        event.showOnMap( request.showOnMap() );
        event.title( request.title() );
        event.venue( request.venue() );

        return event.build();
    }

    @Override
    public void update(Event event, EventRequest request) {
        if ( request == null ) {
            return;
        }

        event.setCategory( request.category() );
        event.setCity( request.city() );
        event.setDateTime( request.dateTime() );
        event.setDescription( request.description() );
        event.setExternalUrl( request.externalUrl() );
        event.setFeatured( request.featured() );
        event.setLatitude( request.latitude() );
        event.setLongitude( request.longitude() );
        event.setPublished( request.published() );
        event.setShowOnMap( request.showOnMap() );
        event.setTitle( request.title() );
        event.setVenue( request.venue() );
    }
}
