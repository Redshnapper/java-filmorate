package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    protected static Long idGenerator = 1L;

    @Override
    public User add(User user) {
        user.setId(idGenerator++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", user.getId()));
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", id));
        }
        return users.get(id);
    }

    @Override
    public void addFriend(Long id, Long friendId) {

        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", id));
        }
        if (!users.containsKey(friendId)) {
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", friendId));
        }
        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        if (users.containsKey(id) && users.containsKey(friendId)) {
            users.get(id).getFriends().remove(friendId);
            users.get(friendId).getFriends().remove(id);
        }
    }

    @Override
    public List<User> getUserFriends(Long id) {
        List<User> usersFriend;
        if (users.containsKey(id)) {
            usersFriend = users.get(id).getFriends().stream()
                    .map(users::get)
                    .collect(Collectors.toList());
            return usersFriend;
        }
        return new ArrayList<>();
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<User> commonFriends = new HashSet<>();
        if (users.containsKey(id) && users.containsKey(otherId)) {
            if (!users.get(id).getFriends().isEmpty() && !users.get(otherId).getFriends().isEmpty()) {
                users.get(id).getFriends().stream()
                        .map(users::get)
                        .forEach(commonFriends::add);

                return users.get(otherId).getFriends().stream()
                        .map(users::get)
                        .filter(commonFriends::contains
                        ).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }


}
