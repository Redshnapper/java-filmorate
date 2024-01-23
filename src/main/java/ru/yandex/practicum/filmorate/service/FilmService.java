package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public Film createFilm(Film film) {
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public void userLikeFilm(Long id, Long userId) {
        if (userStorage.getById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", userId));
        }
        filmStorage.userLikeFilm(id, userId);
    }

    public void userDeleteLike(Long id, Long userId) {
        if (userStorage.getById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", userId));
        }
        filmStorage.userDeleteLike(id, userId);
    }

    public List<Film> getMostLiked(int count) {
        return filmStorage.getMostLiked(count);
    }


}
