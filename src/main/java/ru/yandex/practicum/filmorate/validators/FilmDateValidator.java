package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmDateValidator implements ConstraintValidator<FilmDate, LocalDate> {
    public LocalDate releaseDate = LocalDate.of(1895, 12, 28);

    public void initialize(FilmDate constraint) {

    }

    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return !value.isBefore(releaseDate);
    }
}
