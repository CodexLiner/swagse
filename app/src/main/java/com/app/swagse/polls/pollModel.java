package com.app.swagse.polls;

import java.util.List;

public class pollModel {
    String user_id , question , end_date;
    List<String> options;

    public pollModel(String user_id, String question, String end_date, List<String> options) {
        this.user_id = user_id;
        this.question = question;
        this.end_date = end_date;
        this.options = options;
    }

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

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
