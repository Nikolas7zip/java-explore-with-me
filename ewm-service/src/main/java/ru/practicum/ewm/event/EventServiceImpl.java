package ru.practicum.ewm.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.pagination.EntityPagination;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.validation.EventDateValidator;
import ru.practicum.ewm.event.QEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            CategoryRepository categoryRepository,
                            UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }


    @Override
    public EventFullDto getPublished(Long id) {
        Event event = eventRepository.findEventById(id)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, id));
        if (event.getState() != EventState.PUBLISHED) {
            throw new BadRequestException("Event with id=" + id + " is not published");
        }

        return EventMapper.mapToFullEventDto(event);
    }

    @Override
    public List<EventShortDto> getAllPublished(EntityPagination pagination, GetEventsPublicRequest request) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();

        conditions.add(event.state.eq(EventState.PUBLISHED));
        if (request.getText() != null) {
            String searchText = request.getText();
            conditions.add(event.annotation.containsIgnoreCase(searchText)
                    .or(event.description.containsIgnoreCase(searchText)));
        }
        if (request.getCategories() != null) {
            conditions.add(event.category.id.in(request.getCategories()));
        }

        if (request.getRangeStart() == null && request.getRangeEnd() == null) {
            conditions.add(event.eventDate.gt(LocalDateTime.now()));
        } else {
            if (request.getRangeStart() != null) {
                conditions.add(event.eventDate.goe(request.getRangeStart()));
            }

            if (request.getRangeEnd() != null) {
                conditions.add(event.eventDate.loe(request.getRangeEnd()));
            }
        }

        if (request.getPaid() != null) {
            conditions.add(event.paid.eq(request.getPaid()));
        }
        if (request.getOnlyAvailable().equals(Boolean.TRUE)) {
            conditions.add(event.participantLimit.gt(0L));
            conditions.add(event.confirmedRequests.eq(event.participantLimit));
        }

        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        List<Event> events = findEventsByFinalCondition(finalCondition, pagination);

        return EventMapper.mapToShortEventDto(events);
    }


    @Transactional
    @Override
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, userId));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new EntityNotFoundException(Category.class, newEventDto.getCategory()));
        Event createdEvent = eventRepository.save(EventMapper.mapToNewEvent(newEventDto, initiator, category));
        log.info("Created by initiator " + createdEvent);

        return EventMapper.mapToFullEventDto(createdEvent);
    }

    @Override
    public EventFullDto getByInitiator(Long userId, Long eventId) {
        Event event = findEventByInitiator(userId, eventId);

        return EventMapper.mapToFullEventDto(event);
    }

    @Override
    public List<EventShortDto> getAllByInitiator(Long userId, EntityPagination pagination) {
        List<Event> events =  eventRepository.findAllByInitiatorId(userId, pagination.getPageable());

        return EventMapper.mapToShortEventDto(events);
    }

    @Override
    public List<EventFullDto> getAllByAdmin(EntityPagination pagination, GetEventsAdminRequest request) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();
        if (request.getUsers() != null) {
            conditions.add(event.initiator.id.in(request.getUsers()));
        }

        if (request.getStates() != null) {
            conditions.add(event.state.in(request.getStates()));
        }

        if (request.getCategories() != null) {
            conditions.add(event.category.id.in(request.getCategories()));
        }

        if (request.getRangeStart() != null) {
            conditions.add(event.eventDate.goe(request.getRangeStart()));
        }

        if (request.getRangeEnd() != null) {
            conditions.add(event.eventDate.loe(request.getRangeEnd()));
        }

        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        List<Event> events = findEventsByFinalCondition(finalCondition, pagination);

        return EventMapper.mapToFullEventDto(events);
    }


    @Transactional
    @Override
    public EventFullDto updateByInitiator(Long userId, UserUpdateEventDto updateEventDto) {
        Event event = findEventByInitiator(userId, updateEventDto.getEventId());
        if (event.getState() != EventState.PENDING && event.getState() != EventState.CANCELED) {
            throw new BadRequestException("Only pending or canceled events can be changed");
        }
        Category newCategory = categoryRepository.findById(updateEventDto.getCategory())
                .orElseThrow(() -> new EntityNotFoundException(Category.class, updateEventDto.getCategory()));
        EventMapper.updateEventFromUserDto(event, updateEventDto, newCategory);
        Event updatedEvent = eventRepository.save(event);
        log.info("Updated by initiator " + updatedEvent);

        return EventMapper.mapToFullEventDto(updatedEvent);
    }

    @Transactional
    @Override
    public EventFullDto updateByAdmin(Long eventId, AdminUpdateEventDto updateEventDto) {
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        Long categoryId = updateEventDto.getCategory();
        Category newCategory = null;
        if (categoryId != null) {
            newCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException(Category.class, categoryId));
        }
        EventMapper.updateEventFromAdminDto(event, updateEventDto, newCategory);
        Event updatedEvent = eventRepository.save(event);
        log.info("Updated by admin " + updatedEvent);

        return EventMapper.mapToFullEventDto(updatedEvent);
    }

    @Transactional
    @Override
    public EventFullDto cancelByInitiator(Long userId, Long eventId) {
        Event event = findEventByInitiator(userId, eventId);
        if (event.getState() != EventState.PENDING) {
            throw new BadRequestException("Only pending events can be canceled");
        }
        event.setState(EventState.CANCELED);
        Event updatedEvent = eventRepository.save(event);
        log.info("Canceled by initiator " + updatedEvent);

        return EventMapper.mapToFullEventDto(updatedEvent);
    }

    @Transactional
    @Override
    public EventFullDto publishByAdmin(Long eventId) {
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        LocalDateTime nowMoment = LocalDateTime.now().withNano(0);
        LocalDateTime limitOnDate = nowMoment.plusHours(EventDateValidator.MIN_HOUR_DELAY_START_EVENT_FROM_PUBLISHED);
        if (event.getState() != EventState.PENDING || event.getEventDate().isBefore(limitOnDate)) {
            throw new BadRequestException("Do not meet conditions for publish event");
        }
        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(nowMoment);
        Event updatedEvent = eventRepository.save(event);
        log.info("Published by admin " + updatedEvent);

        return EventMapper.mapToFullEventDto(updatedEvent);
    }

    @Transactional
    @Override
    public EventFullDto cancelByAdmin(Long eventId) {
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));

        if (event.getState() == EventState.PUBLISHED) {
            throw new BadRequestException("Can't cancel published event");
        }

        if (event.getState() == EventState.PENDING) {
            event.setState(EventState.CANCELED);
            eventRepository.save(event);
        }
        log.info("Canceled by admin  " + event);

        return EventMapper.mapToFullEventDto(event);
    }

    @Transactional
    @Override
    public void incrementViews(Long eventId) {
        eventRepository.incrementViews(eventId);
    }

    private Event findEventByInitiator(Long userId, Long eventId) {
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("User and event do not match");
        }

        return event;
    }

    private List<Event> findEventsByFinalCondition(BooleanExpression finalCondition, EntityPagination pagination) {
        List<Event> eventsShort = eventRepository.findAll(finalCondition, pagination.getPageable()).getContent();
        List<Event> eventsFull = new ArrayList<>();
        if (!eventsShort.isEmpty()) {
            List<Long> eventIds = eventsShort.stream().map(Event::getId).collect(Collectors.toList());
            eventsFull = eventRepository.findAllByEventIds(eventIds, pagination.getSort());
        }

        return eventsFull;
    }
}
