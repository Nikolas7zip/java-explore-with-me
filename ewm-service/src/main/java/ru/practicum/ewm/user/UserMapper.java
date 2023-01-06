package ru.practicum.ewm.user;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

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
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(mapToUserDto(user));
        }

        return dtos;
    }
}
