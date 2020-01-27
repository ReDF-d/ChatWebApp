package com.redf.chatwebapp.exception;

public class AvatarTooLargeException {

    private String message;

    public AvatarTooLargeException(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
