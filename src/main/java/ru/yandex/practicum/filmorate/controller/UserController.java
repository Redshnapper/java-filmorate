package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    protected static Long idGenerator = 1L;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public User create(@Valid @RequestBody User user) {
        log.info("Создание пользователя {}", user);
        isValid(user);
        user.setId(idGenerator++);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public User updateUser(@RequestBody User user) {
        log.info("Обновление пользователя {}", user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Не удалось обновить пользователя. Пользователь не существует");
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получение всех пользователей, количество: {}", users.size());
        return new ArrayList<>(users.values());
    }

    public void isValid(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
