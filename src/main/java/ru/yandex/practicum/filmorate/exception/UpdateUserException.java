package ru.yandex.practicum.filmorate.exception;

public class UpdateUserException extends RuntimeException {
    public UpdateUserException(String message) {
        System.out.println(message);
    }
}
