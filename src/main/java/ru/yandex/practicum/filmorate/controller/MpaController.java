package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final FilmService filmService;

    @GetMapping("{id}")
    public Mpa getMpaById(@PathVariable("id") Long id) {
        log.info("Получение рейтинга по id {}", id);
        return filmService.getMpaById(id);
    }

    @GetMapping()
    public List<Mpa> getAllMpa() {
        log.info("Получение списка рейтингов");
        return filmService.getAllMpa();
    }
}
