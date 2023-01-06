package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    @NotNull
    private List<Long> events;

    private Boolean pinned = false;

    @NotBlank
    private String title;

}
