package com.laurentiu.spring_ai_app.exception;

public enum ErrorCode {
    KMC_BAD_REQUEST("E0001", "Message NOT sent to topic")
    ;


    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
