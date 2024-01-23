package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmLikesComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    protected static Long idGenerator = 1L;

    @Override
    public Film add(Film film) {
        film.setId(idGenerator++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден", film.getId()));
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден", id));
        }
        return films.get(id);
    }

    @Override
    public void userLikeFilm(Long id, Long userId) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден", id));
        }
        films.get(id).getLikes().add(userId);
    }

    @Override
    public void userDeleteLike(Long id, Long userId) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден", id));
        }
        films.get(id).getLikes().remove(userId);
    }

    @Override
    public List<Film> getMostLiked(int count) {
        return films.values().stream()
                .sorted(new FilmLikesComparator())
                .limit(count).collect(Collectors.toList());
    }


}
