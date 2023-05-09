package com.example.eventbox.service;

import com.example.eventbox.model.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventService {
    Page<Event> findAllPaging(Specification<Event> specification, Pageable pageable);
    List<Event> findAllByDeleteFalse();
    void create(Event event);
    boolean existsEvenName(String name);
    boolean checkStartTime(LocalDateTime startDateTime, LocalDateTime endDateTime);
    Optional<Event> findById(Long id);
    void update(Event event);
    void delete(Event event);


}
