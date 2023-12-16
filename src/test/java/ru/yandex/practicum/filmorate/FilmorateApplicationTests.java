package ru.yandex.practicum.filmorate;


import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class FilmorateApplicationTests {
    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void invalidEmailShouldFailValidation() {
        User user = new User();
        user.setEmail("asd@asd.");
        user.setLogin("Ultra lord");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void blankEmailShouldFailValidation() {
        User user = new User();
        user.setEmail(" ");
        user.setLogin("Ultra lord");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyEmailShouldFailValidation() {
        User user = new User();
        user.setEmail("");
        user.setLogin("Ultra lord");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void emptyLoginShouldFailValidation() {
        User user = new User();
        user.setLogin("");
        user.setEmail("ultralord@mail.ru");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void blankLoginShouldFailValidation() {
        User user = new User();
        user.setLogin(" ");
        user.setEmail("ultralord@mail.ru");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void invalidBirthdateShouldFailValidation() {
        User user = new User();
        user.setLogin("Ultra lord");
        user.setEmail("ultralord@mail.ru");
        user.setBirthday(LocalDate.of(2200, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void emptyNameShouldFailValidation() {
        Film film = new Film();
        film.setName("");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void descriptionWith201SymbolShouldFailValidation() {
        Film film = new Film();
        film.setName("Fight Club");
        film.setReleaseDate(LocalDate.of(2000, 1, 13));
        film.setDuration(1);
        film.setDescription("12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" + // 200 символов
                "1"); // 201

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void invalidDateShouldFailValidation() {
        Film film = new Film();
        film.setDuration(1);
        film.setName("Fight Club");
        film.setReleaseDate(LocalDate.of(1800, 1, 13));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void negativeDurationShouldFailValidation() {
        Film film = new Film();
        film.setDuration(-1);
        film.setName("Fight Club");
        film.setReleaseDate(LocalDate.of(2000, 1, 13));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertThat(violations.size()).isEqualTo(1);
    }
}
