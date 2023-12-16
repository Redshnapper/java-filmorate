package ru.yandex.practicum.filmorate.exceptions;

public class UpdateUserException extends RuntimeException {
    public UpdateUserException() {
    }

    public UpdateUserException(String message) {
        System.out.println(message + super.getMessage());
    }

    public UpdateUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateUserException(Throwable cause) {
        super(cause);
    }

    public UpdateUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
