package ru.practicum.ewm.user.prohibition.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewProhibitionDto {

    @Positive
    @Max(value = 720)
    private Integer blockingTime;
}