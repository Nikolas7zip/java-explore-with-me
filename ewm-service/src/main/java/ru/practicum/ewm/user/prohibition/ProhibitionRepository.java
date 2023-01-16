package ru.practicum.ewm.user.prohibition;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProhibitionRepository extends JpaRepository<Prohibition, Long> {

    Optional<Prohibition> findByUserId(Long userId);
}
