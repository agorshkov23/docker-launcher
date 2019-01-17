package io.github.agorshkov23.dockerlauncher.exception;

import lombok.NoArgsConstructor;

public class NotImplementException extends RuntimeException {

    public NotImplementException() {
    }

    public NotImplementException(String message) {
        super(message);
    }
}
