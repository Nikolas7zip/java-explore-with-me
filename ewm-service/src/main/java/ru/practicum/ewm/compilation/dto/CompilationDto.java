package ru.practicum.ewm.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

@Data
@NoArgsConstructor
public class CompilationDto {
    private Long id;

    private List<EventShortDto> events;

    private Boolean pinned;

    private String title;
}
