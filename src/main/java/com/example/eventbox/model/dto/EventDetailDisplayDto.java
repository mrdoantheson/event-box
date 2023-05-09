package com.example.eventbox.model.dto;

import com.example.eventbox.model.enums.EventStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class EventDetailDisplayDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime openToRegistrationDateTime;
    private LocalDateTime closeToRegistrationDateTime;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String place;
    private Boolean isPublic;
    private Integer capacity;
    private EventStatus status;
}
