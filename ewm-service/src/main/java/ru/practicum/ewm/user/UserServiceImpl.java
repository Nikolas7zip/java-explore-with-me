package ru.practicum.ewm.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.pagination.EntityPagination;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserProhibitionDto;
import ru.practicum.ewm.user.prohibition.dto.NewProhibitionDto;
import ru.practicum.ewm.user.prohibition.Prohibition;
import ru.practicum.ewm.user.prohibition.ProhibitionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProhibitionRepository prohibitionRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ProhibitionRepository prohibitionRepository) {
        this.userRepository = userRepository;
        this.prohibitionRepository = prohibitionRepository;
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
        if (userRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(User.class, id);
        }
        userRepository.deleteById(id);
        log.info("Deleted user with id=" + id);
    }

    @Transactional
    @Override
    public UserProhibitionDto block(Long id, NewProhibitionDto newProhibitionDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        Prohibition userProhibition = prohibitionRepository.findByUserId(id)
                .orElse(null);
        if (userProhibition == null) {
            userProhibition = new Prohibition();
            userProhibition.setUserId(id);
            userProhibition.setBlockingTime(newProhibitionDto.getBlockingTime());
        } else {
            userProhibition.setCreated(LocalDateTime.now().withNano(0));
            userProhibition.setBlockingTime(newProhibitionDto.getBlockingTime());
        }
        Prohibition savedProhibition = prohibitionRepository.save(userProhibition);
        log.info("Block user with " + savedProhibition);

        return UserMapper.mapToUserProhibitionDto(user, savedProhibition);
    }

    @Transactional
    @Override
    public void unlock(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        Prohibition prohibition = prohibitionRepository.findByUserId(id)
                .orElse(null);
        if (prohibition != null) {
            prohibitionRepository.deleteById(prohibition.getId());
            log.info("Unlock user with id=" + id);
        }
    }

    @Override
    public UserProhibitionDto get(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        Prohibition prohibition = prohibitionRepository.findByUserId(id)
                .orElse(null);

        return UserMapper.mapToUserProhibitionDto(user, prohibition);
    }

}
