package com.example.eventbox.model.dto;

import com.example.eventbox.model.enums.EventStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventFromDto {
    private Long id;
    @NotBlank(message = "{common.error.required}")
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer capacity;
    private String place;
    private EventStatus status;
}
