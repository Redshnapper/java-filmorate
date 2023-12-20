package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public User createUser(@Valid @RequestBody User user) {
        log.info("Создание пользователя {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public User updateUser(@RequestBody User user) {
        log.info("Обновление пользователя {}", user);
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получение всех пользователей в количестве {}", userService.getAllUsers().size());
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable("id") Long id) {
        log.info("Получение пользователя с id {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Добавление пользователя с id {} в друзья пользователю с id {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Удаление пользователя с id {} в из друзей пользователя с id {}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") Long id) {
        log.info("Получение друзей пользователя с id {}", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        log.info("Получение общих друзей пользователя с id {} и пользователя c id {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
