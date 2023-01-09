package ru.practicum.ewm.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class ApiError {
    private String message;
    private String reason;
    private String status;
    private String timestamp;
    private List<String> errors;

    public ApiError(String message, String reason, String status) {
        this(message, reason, status, new ArrayList<>());
    }

    public ApiError(String message, String reason, String status, List<String> errors) {
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.errors = errors;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.now().format(formatter);
    }
}
