package ru.practicum.ewm.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 512, nullable = false, unique = true)
    private String email;

    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;
}
