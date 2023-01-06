package ru.practicum.ewm.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.pagination.EntityPagination;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> get(EntityPagination pagination) {
        log.info("Get users " + pagination);
        List<User> users = userRepository.findAll(pagination.getPageable()).getContent();

        return UserMapper.mapToUserDto(users);
    }

    @Override
    public List<UserDto> get(List<Long> ids) {
        log.info("Get users " + ids);
        List<User> users = userRepository.findAllById(ids);

        return UserMapper.mapToUserDto(users);
    }

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        User createdUser = userRepository.save(UserMapper.mapToNewUser(userDto));
        log.info("Created " + createdUser);

        return UserMapper.mapToUserDto(createdUser);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(User.class, id);
        }
        userRepository.deleteById(id);
        log.info("Deleted user with id=" + id);
    }
}
