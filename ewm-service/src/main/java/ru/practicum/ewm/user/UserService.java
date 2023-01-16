package ru.practicum.ewm.user;

import ru.practicum.ewm.pagination.EntityPagination;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserProhibitionDto;
import ru.practicum.ewm.user.prohibition.dto.NewProhibitionDto;

import java.util.List;

public interface UserService {

    List<UserDto> get(EntityPagination pagination);

    List<UserDto> get(List<Long> ids);

    UserDto create(UserDto userDto);

    void delete(Long id);

    UserProhibitionDto block(Long id, NewProhibitionDto newProhibitionDto);

    void unlock(Long id);

    UserProhibitionDto get(Long id);
}
