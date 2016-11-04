package org.gamesys.exception;

public class UserBlackListedException extends RuntimeException {
    public UserBlackListedException(String message) {
        super(message);
    }
}
