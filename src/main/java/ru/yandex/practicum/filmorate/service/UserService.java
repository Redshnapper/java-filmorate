package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private UserStorage userStorage;

    public void checkName(User user) {
        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    public User createUser(User user) {
        log.info("Создание пользователя {}", user);
        checkName(user);
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        log.info("Обновление пользователя {}", user);
        return userStorage.update(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(Long id) {
        return userStorage.getById(id);
    }

    public void addFriend(Long id, Long friendId) {
        log.info("Добавление пользователя с id {} в друзья пользователю с id {}", id, friendId);
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        log.info("Удаление пользователя с id {} в из друзей пользователя с id {}", id, friendId);
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getUserFriends(Long id) {
        return userStorage.getUserFriends(id);
    }

    public Set<User> getCommonFriends(Long id, Long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}
