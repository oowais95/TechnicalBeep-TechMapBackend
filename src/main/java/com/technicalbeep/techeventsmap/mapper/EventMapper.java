package com.technicalbeep.techeventsmap.mapper;

import com.technicalbeep.techeventsmap.dto.EventRequest;
import com.technicalbeep.techeventsmap.dto.EventResponse;
import com.technicalbeep.techeventsmap.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventResponse toResponse(Event event);

    @Mapping(target = "id", ignore = true)
    Event toEntity(EventRequest request);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Event event, EventRequest request);
}
