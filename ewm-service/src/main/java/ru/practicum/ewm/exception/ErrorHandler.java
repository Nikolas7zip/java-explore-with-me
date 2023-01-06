package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        ApiError error = new ApiError(
                ex.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.BAD_REQUEST.name(),
                details
        );
        log.warn("MethodArgumentNotValidException: " + String.join(", ", details));

        return error;
    }

    @ExceptionHandler(value = {BadRequestException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final RuntimeException ex) {
        ApiError error = new ApiError(
                ex.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.BAD_REQUEST.name()
        );
        log.warn("BadRequestException: {}", ex.getMessage());

        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(final EntityNotFoundException ex) {
        ApiError error = new ApiError(
                ex.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND.name()
        );
        log.warn("EntityNotFoundException: {}", ex.getMessage());

        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException ex) {
        ApiError error = new ApiError(
                ex.getMessage(),
                "Integrity constraint has been violated",
                HttpStatus.CONFLICT.name()
        );
        log.error("Error database, {}", ex.getMessage(), ex);

        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnexpectedError(final Throwable ex) {
        ApiError error = new ApiError(
                ex.getMessage(),
                "Error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.name()
        );
        log.error("Error 500, {}", ex.getMessage(), ex);

        return error;
    }
}
