package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class UserDateValidator implements ConstraintValidator<UserDate, LocalDate> {
    public LocalDate today = LocalDate.now();

    public void initialize(UserDate constraint) {

    }

    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return !value.isAfter(today);
    }
}
