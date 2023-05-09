package com.example.eventbox.resource;

import com.example.eventbox.constant.AppConstant;
import com.example.eventbox.exception.ResourceFoundException;
import com.example.eventbox.model.dto.EventDetailDisplayDto;
import com.example.eventbox.model.dto.EventFromDto;
import com.example.eventbox.model.dto.EventListDisplayDto;
import com.example.eventbox.model.entity.Event;
import com.example.eventbox.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventResource {
    private final EventService eventService;

    public EventResource(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<Page<EventListDisplayDto>> showList(
            @RequestParam(required = false, defaultValue = AppConstant.DEFAULT_PAGE_STR) Integer page,
            @RequestParam(required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(required = false, name = "sort",
                    defaultValue = AppConstant.DEFAULT_SORT_FIELD) List<String> sorts,
            @RequestParam(required = false, name = "q") Optional<String> keywordOpt) {

        List<Sort.Order> orders = new ArrayList<>();
        for (String sortField : sorts) {
            boolean isDesc = sortField.startsWith("-");
            orders.add(isDesc ? Sort.Order.desc(sortField.substring(1))
                    : Sort.Order.asc(sortField));
        }
        Specification<Event> specification = (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("deleted"), false);
        if (keywordOpt.isPresent()) {
            //WHERE deleted = 0 AND (name like '%Fville1%')
            Specification<Event> specByKeyword = (root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("name"), "%" + keywordOpt.get() + "%"),
                            criteriaBuilder.like(root.get("place"), "%" + keywordOpt.get() + "%")
                    );
            //WHERE deleted = 0 AND (name like '%Fville1%' OR place like '%Fville')
            specification = specification.and(specByKeyword);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(orders));
        Page<Event> eventPage = eventService.findAllPaging(specification, pageRequest);
        // Page<Event> => Page<EventListDisplayDto>
        // Covert List<Event> => List<EventListDisplayDto>
        List<EventListDisplayDto> displayDtos = eventPage.getContent().stream()
                .map(event -> {
                    EventListDisplayDto eventListDisplayDto = new EventListDisplayDto();
                    BeanUtils.copyProperties(event, eventListDisplayDto);
                    return eventListDisplayDto;
                }).collect(Collectors.toList());
        Page<EventListDisplayDto> result = new PageImpl<>(displayDtos, pageRequest, eventPage.getTotalElements());
        return ResponseEntity.ok(result);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        Optional<Event> eventOpt = eventService.findById(id);
        Event event = eventOpt.orElseThrow(ResourceFoundException::new);
        eventService.delete(event);
    }

    @PostMapping
    public ResponseEntity<EventDetailDisplayDto> createEvent(@RequestBody @Valid EventFromDto eventFromDto) {
        Event event = new Event();
        BeanUtils.copyProperties(eventFromDto, event);
        event.setDeleted(false);
        eventService.create(event);

        ResponseEntity.ok(event);

        EventDetailDisplayDto eventDetailDisplayDto = new EventDetailDisplayDto();
        BeanUtils.copyProperties(event, eventDetailDisplayDto);

        return ResponseEntity.ok(eventDetailDisplayDto);

    }
}
