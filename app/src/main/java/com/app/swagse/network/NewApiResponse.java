package com.app.swagse.network;

public class NewApiResponse {
    String message , success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NewApiResponse{" +
                "message='" + message + '\'' +
                ", success='" + success + '\'' +
                '}';
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
