package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    protected static Long idGenerator = 1L;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создание фильма {}", film);
        film.setId(idGenerator++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновление фильма {}", film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Не удалось обновить фильм. Фильм не существует");
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов, количество: {}", films.size());
        return new ArrayList<>(films.values());
    }

}
