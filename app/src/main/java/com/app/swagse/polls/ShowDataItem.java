package com.app.swagse.polls;

import com.app.swagse.model.userDetails.Userdata;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowDataItem {

    @SerializedName("question")
    private String question;

    @SerializedName("votes_count")
    private String votes_count;

    @SerializedName("options")
    private List<String> options;

    @SerializedName("id")
    private String id;

    @SerializedName("user")
    private Userdata userdata;

    @SerializedName("status")
    private String status;

    @SerializedName("deadline")
    private String deadline;

    @SerializedName("votes")
    private votes votes;

    public Userdata getUserdata() {
        return userdata;
    }

    public void setUserdata(Userdata userdata) {
        this.userdata = userdata;
    }





    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getVotes_count() {
        return votes_count;
    }

    public void setVotes_count(String votes_count) {
        this.votes_count = votes_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "ShowDataItem{" +
                "question='" + question + '\'' +
                ", votes_count='" + votes_count + '\'' +
                ", options='" + options + '\'' +
                ", id='" + id + '\'' +
                ", userdata=" + userdata +
                ", status='" + status + '\'' +
                ", deadline='" + deadline + '\'' +
                ", votes=" + votes +
                '}';
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public com.app.swagse.polls.votes getVotes() {
        return votes;
    }

    public void setVotes(com.app.swagse.polls.votes votes) {
        this.votes = votes;
    }
}
