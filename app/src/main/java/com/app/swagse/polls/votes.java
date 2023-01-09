package com.app.swagse.polls;

public class votes {
    String answer , polling_id , id , user_id ;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPolling_id() {
        return polling_id;
    }

    public void setPolling_id(String polling_id) {
        this.polling_id = polling_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "votes{" +
                "answer='" + answer + '\'' +
                ", polling_id='" + polling_id + '\'' +
                ", id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
