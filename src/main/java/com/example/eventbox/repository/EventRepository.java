package com.example.eventbox.repository;

import com.example.eventbox.model.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends BaseRepository<Event, Long> {
    boolean existsByNameAndDeletedFalse(String name);
    Page<Event> findAllByDeletedFalseOrderByLastModifiedDateDesc(Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE(e.isPublic = :isPublic AND e.capacity >= :capacity)" +
            "OR (e.isPublic = :isPublic AND e.capacity < :capacity)")
    List<Event> findAllEvents(@Param("capacity") Integer capacity, @Param("isPublic") boolean isPublic);
}
