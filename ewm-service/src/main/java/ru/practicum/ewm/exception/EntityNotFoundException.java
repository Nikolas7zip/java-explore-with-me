package ru.practicum.ewm.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> classEntity, Long id) {
        super(classEntity.getSimpleName() + " with id=" + id + " was not found.");
    }
}