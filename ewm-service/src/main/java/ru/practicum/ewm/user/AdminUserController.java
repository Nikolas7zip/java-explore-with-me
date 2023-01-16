package ru.practicum.ewm.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.pagination.EntityPagination;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserProhibitionDto;
import ru.practicum.ewm.user.prohibition.dto.NewProhibitionDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Validated
public class AdminUserController {
    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(name = "ids", required = false) List<@Positive Long> ids,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        if (ids != null) {
            return userService.get(ids);
        }

        return userService.get(new EntityPagination(from, size, Sort.by("id").ascending()));
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
    }

    @PutMapping("/{userId}/block")
    public UserProhibitionDto blockUser(@PathVariable Long userId,
                                        @RequestBody NewProhibitionDto newProhibitionDto) {
        return userService.block(userId, newProhibitionDto);
    }

    @DeleteMapping("/{userId}/unlock")
    public void unlockUser(@PathVariable Long userId) {
        userService.unlock(userId);
    }

    @GetMapping("/{userId}")
    public UserProhibitionDto getUser(@PathVariable Long userId) {
        return userService.get(userId);
    }

}
