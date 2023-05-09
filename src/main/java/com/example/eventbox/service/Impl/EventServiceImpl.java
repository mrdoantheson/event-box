package com.example.eventbox.service.Impl;

import com.example.eventbox.model.entity.Event;
import com.example.eventbox.repository.EventRepository;
import com.example.eventbox.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Page<Event> findAllPaging(Specification<Event> specification, Pageable pageable) {
        return eventRepository.findAll(specification, pageable);
    }

    @Override
    public List<Event> findAllByDeleteFalse() {
        return eventRepository.findAllByDeletedFalse();
    }

    @Override
    public void create(Event event) {
        event.setCreatedDate(LocalDateTime.now());
        event.setLastModifiedDate(LocalDateTime.now());
        eventRepository.save(event);
    }

    @Override
    public boolean existsEvenName(String name) {
        Objects.requireNonNull(name);
        return eventRepository.existsByNameAndDeletedFalse(name);
    }

    @Override
    public boolean checkStartTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return startDateTime.isAfter(endDateTime);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public void update(Event event) {
        event.setLastModifiedDate(LocalDateTime.now());
        eventRepository.save(event);
    }

    @Override
    public void delete(Event event) {
        event.setDeleted(true);
        eventRepository.save(event);
    }
}
