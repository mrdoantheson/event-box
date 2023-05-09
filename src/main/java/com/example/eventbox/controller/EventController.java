package com.example.eventbox.controller;

import com.example.eventbox.constant.AppConstant;
import com.example.eventbox.model.dto.EventFromDto;
import com.example.eventbox.model.entity.Event;
import com.example.eventbox.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public String showList(Model model,
                           @RequestParam(required = false, defaultValue = AppConstant.DEFAULT_PAGE_STR) Integer page,
                           @RequestParam(required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size,
                           @RequestParam(required = false, name = "sort",
                                   defaultValue = AppConstant.DEFAULT_SORT_FIELD) List<String> sorts,
                           @RequestParam(required = false, name = "q")Optional<String> keywordOpt) {

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
        model.addAttribute("eventPage", eventPage);
        return "/events/list";
    }

    @GetMapping("/events/create")
    public String showCreate(Model model) {
        model.addAttribute("eventFromDto", new EventFromDto());
        return "/events/form";
    }

    @PostMapping({"/events/create"})
    public String create(@Valid EventFromDto eventFromDto,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "events/form";
        }

        if (eventService.existsEvenName(eventFromDto.getName())) {
            bindingResult.rejectValue("name", "common.name.duplicate");
            return "events/form";
        }
        if (bindingResult.hasErrors()) {
            return "events/form";
        }

        if (eventService.checkStartTime(eventFromDto.getStartDateTime(), eventFromDto.getEndDateTime())) {
            bindingResult.rejectValue("startDateTime", "startDateTime.invalid");
            return "events/form";
        }
        if (bindingResult.hasErrors()) {
            return "events/form";
        }
        //Convert eventFormDto -> event entity
        Event event = new Event();
        BeanUtils.copyProperties(eventFromDto, event);
        event.setDeleted(false);
        eventService.create(event);

        redirectAttributes.addFlashAttribute("successMessage", "event.create.success");

        return "redirect:/events";

    }

    @GetMapping("/events/update/{id}")
    public String showUpdate(Model model, @PathVariable Long id) {
        Optional<Event> event = eventService.findById(id);

        EventFromDto eventFromDto = new EventFromDto();
        BeanUtils.copyProperties(event.get(), eventFromDto);

        model.addAttribute("eventFromDto", eventFromDto);

        return "/events/form";
    }

    @PostMapping({"/events/update/{id}"})
    public String update(@Valid EventFromDto eventFromDto,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "events/form";
        }

        if (eventService.existsEvenName(eventFromDto.getName())) {
            bindingResult.rejectValue("name", "common.name.duplicate");
            return "events/form";
        }
        if (bindingResult.hasErrors()) {
            return "events/form";
        }

        Event event = new Event();
        BeanUtils.copyProperties(eventFromDto, event);
        event.setId(id);
        eventService.update(event);

        redirectAttributes.addFlashAttribute("successMessage", "event.update.success");

        return "redirect:/events";
    }

    @GetMapping("/events/delete/{id}")
    public String delete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        Optional<Event> event = eventService.findById(id);

        if (event.isEmpty()) {
            return "redirect:/events";
        }

        event.get().setDeleted(true);
        eventService.update(event.get());

        redirectAttributes.addFlashAttribute("successMessage", "event.delete.success");
        return "redirect:/events";
    }
}
