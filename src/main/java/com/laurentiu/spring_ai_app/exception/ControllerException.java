package com.laurentiu.spring_ai_app.exception;

public class ControllerException extends RuntimeException {

    private String errorCode;
    private String errorMessage;


    public ControllerException(ErrorCode error, Exception e) {
        super(error.getMessage() + " " + e.getMessage());

        this.errorCode = error.getCode();
        this.errorMessage = (error.getMessage() + " " + e.getMessage());
    }

    public ControllerException(ErrorCode error) {
        super(error.getMessage());
        this.errorCode = error.getCode();
        this.errorMessage = error.getMessage();
    }


    public ControllerException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
