package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreStorage {
    Genre getGenreById(Long id);

    Set<Genre> getAllGenres();
}
