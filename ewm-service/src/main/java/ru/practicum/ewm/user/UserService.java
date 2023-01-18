package ru.practicum.ewm.user;

import ru.practicum.ewm.pagination.EntityPagination;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> get(EntityPagination pagination);

    List<UserDto> get(List<Long> ids);

    UserDto create(UserDto userDto);

    void delete(Long id);
}
