package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Создание фильма {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновление фильма {}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов {}", filmService.getAllFilms().size());
        return filmService.getAllFilms();
    }

    @GetMapping("{id}")
    public Film getFilmById(@PathVariable("id") Long id) {
        log.info("Получение фильма по id {}", id);
        return filmService.getFilmById(id);
    }

    @PutMapping("{id}/like/{userId}")
    public void userLikeFilm(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Пользователь с id {} лайкнул фильм c id {}", userId, id);
        filmService.userLikeFilm(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void userDeleteLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Пользователь с id {} удалил лайк у фильма c id {}", userId, id);
        filmService.userDeleteLike(id, userId);
    }

    @GetMapping("popular")
    public List<Film> mostLikedFilms(@RequestParam(required = false, name = "count", defaultValue = "10") int count) {
        log.info("Получение самых популярных фильмов в количестве {}", count);
        return filmService.getMostLiked(count);
    }
}
