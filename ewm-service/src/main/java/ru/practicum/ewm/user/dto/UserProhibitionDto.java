package ru.practicum.ewm.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.user.prohibition.dto.ProhibitionDto;

@Getter
@Setter
@NoArgsConstructor
public class UserProhibitionDto {
    private Long id;

    private String email;

    private String name;

    private ProhibitionDto prohibition;
}
