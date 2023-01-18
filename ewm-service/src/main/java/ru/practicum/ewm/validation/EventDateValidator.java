package ru.practicum.ewm.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDateValidator implements ConstraintValidator<EventDateConstraint, LocalDateTime> {
    public static final long MIN_HOUR_DELAY_START_EVENT_FROM_CREATED = 2L;
    public static final long MIN_HOUR_DELAY_START_EVENT_FROM_PUBLISHED = 1L;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void initialize(EventDateConstraint constraint) {
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        return value != null && value.isAfter(LocalDateTime.now().plusHours(MIN_HOUR_DELAY_START_EVENT_FROM_CREATED));
    }
}
