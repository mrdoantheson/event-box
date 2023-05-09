package com.example.eventbox.model.entity;

import com.example.eventbox.model.enums.EventStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Event extends AbstractAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 1000)
    @Nationalized
    private String description;
    @Column(name = "open_to_registration_date_time")
    private LocalDateTime openToRegistrationDateTime;
    @Column(name = "close_to_registration_date_time")
    private LocalDateTime closeToRegistrationDateTime;
    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;
    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;
    private String place;
    @Column(name = "is_public")
    private Boolean isPublic;
    private Integer capacity;
    @Enumerated(EnumType.STRING)
    private EventStatus status;
}