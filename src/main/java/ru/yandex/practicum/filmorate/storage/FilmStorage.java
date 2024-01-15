package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    List<Film> getAll();

    Film getFilmById(Long id);

    void userLikeFilm(Long id, Long userId);

    void userDeleteLike(Long id, Long userId);

    List<Film> getMostLiked(int count);
}
