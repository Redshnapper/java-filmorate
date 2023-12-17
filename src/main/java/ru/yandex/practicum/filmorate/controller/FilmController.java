package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private FilmService filmService;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("{id}")
    public Film getFilmById(@PathVariable("id") Long id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("{id}/like/{userId}")
    public void userLikeFilm(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        filmService.userLikeFilm(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void userDeleteLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        filmService.userDeleteLike(id, userId);
    }

    @GetMapping("popular")
    public List<Film> mostLikedFilms(@RequestParam(required = false, name = "count", defaultValue = "10") int count) {
        return filmService.getMostLiked(count);
    }
}
