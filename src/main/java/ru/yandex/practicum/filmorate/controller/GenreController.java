package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final FilmService filmService;

    @GetMapping("{id}")
    public Genre getGenreById(@PathVariable("id") Long id) {
        log.info("Получение жанра по id {}", id);
        return filmService.getGenreById(id);
    }

    @GetMapping()
    public Set<Genre> getAllGenres() {
        log.info("Получение списка жанров");
        return filmService.getAllGenres();
    }
}