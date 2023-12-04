package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validators.FilmDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class Film {
    private int id;
    @NotNull
    @NotBlank
    private String name;
    @Length(max = 200)
    private String description;
    @FilmDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
