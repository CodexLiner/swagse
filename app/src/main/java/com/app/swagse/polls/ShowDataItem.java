package com.app.swagse.polls;

import com.google.gson.annotations.SerializedName;

public class ShowDataItem {
    @SerializedName("question")
    private String question;

    @SerializedName("votes_count")
    private String votes_count;

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

    @SerializedName("status")
    private String status;

    @SerializedName("deadline")
    private String deadline;


}
