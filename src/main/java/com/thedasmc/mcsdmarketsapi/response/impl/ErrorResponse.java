package com.thedasmc.mcsdmarketsapi.response.impl;

public class ErrorResponse {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
            "message='" + message + '\'' +
            '}';
    }
}
