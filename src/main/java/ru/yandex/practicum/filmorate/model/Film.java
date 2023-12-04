package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validators.FilmDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class)
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @Length(min = 1,max = 200)
    private String description;
    @FilmDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
