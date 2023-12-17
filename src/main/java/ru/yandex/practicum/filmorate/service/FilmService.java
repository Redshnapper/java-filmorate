package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private FilmStorage filmStorage;
    private final UserService userService;

    public Film createFilm(Film film) {
        log.info("Создание фильма {}", film);
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        log.info("Обновление фильма {}", film);
        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Long id) {
        log.info("Получение фильма по id {}", id);
        return filmStorage.getFilmById(id);
    }

    public void userLikeFilm(Long id, Long userId) {
        log.info("Пользователь с id {} лайкнул фильм c id {}", userId, id);
        if (userService.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", userId));
        }
        filmStorage.userLikeFilm(id, userId);
    }

    public void userDeleteLike(Long id, Long userId) {
        log.info("Пользователь с id {} удалил лайк у фильма c id {}", userId, id);
        if (userService.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", userId));
        }
        filmStorage.userDeleteLike(id, userId);

    }

    public List<Film> getMostLiked(int count) {
        return filmStorage.getMostLiked(count);
    }
}
