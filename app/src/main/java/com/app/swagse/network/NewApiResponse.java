package com.app.swagse.network;

import java.util.List;

public class NewApiResponse {
    String message , success;
    data data;

    public NewApiResponse.data getData() {
        return data;
    }

    public void setData(NewApiResponse.data data) {
        this.data = data;
    }

    class data {
        String user_id , question ;
        List options;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public List getOptions() {
            return options;
        }

        public void setOptions(List options) {
            this.options = options;
        }
    }


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
