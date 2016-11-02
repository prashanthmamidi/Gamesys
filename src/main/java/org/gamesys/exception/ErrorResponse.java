package org.gamesys.exception;

public class ErrorResponse {
    private String description;

    public ErrorResponse() {
    }
    public ErrorResponse(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
