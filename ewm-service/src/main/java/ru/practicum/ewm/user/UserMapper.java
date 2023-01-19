package ru.practicum.ewm.user;

import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserProhibitionDto;
import ru.practicum.ewm.user.prohibition.Prohibition;
import ru.practicum.ewm.user.prohibition.dto.ProhibitionDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static User mapToNewUser(UserDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());

        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());

        return dto;
    }

    public static List<UserDto> mapToUserDto(List<User> users) {
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public static UserProhibitionDto mapToUserProhibitionDto(User user, Prohibition prohibition) {
        UserProhibitionDto dto = new UserProhibitionDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        if (prohibition != null) {
            dto.setProhibition(new ProhibitionDto(
                    prohibition.getId(),
                    prohibition.getCreated(),
                    prohibition.getBlockingTime()
            ));
        }

        return dto;
    }
}
