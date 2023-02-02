package com.app.swagse.polls;

import android.content.Intent;

import com.app.swagse.model.userDetails.Userdata;
import com.app.swagse.polls.comments.comments;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowDataItem {

    @SerializedName("question")
    private String question;

    @SerializedName("votes_count")
    private String votes_count;

    @SerializedName("options")
    private List<String> options;

    @SerializedName("comments")
    private List<comments> comments;

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

    @SerializedName("likes_count")
    private int likes_count;

    @SerializedName("comments_count")
    private int comments_count;

    @SerializedName("start_date")
    private String start_date;

    public com.app.swagse.polls.votes getLikes() {
        return likes;
    }

    public void setLikes(com.app.swagse.polls.votes likes) {
        this.likes = likes;
    }

    @SerializedName("likes")
    private votes likes;

    @SerializedName("result_count")
    private int[] result_count;

    public int[] getResult_count() {
        return result_count;
    }

    public void setResult_count(int[] result_count) {
        this.result_count = result_count;
    }

    public Userdata getUserdata() {
        return userdata;
    }

    public void setUserdata(Userdata userdata) {
        this.userdata = userdata;
    }


    public List<com.app.swagse.polls.comments.comments> getComments() {
        return comments;
    }

    public void setComments(List<com.app.swagse.polls.comments.comments> comments) {
        this.comments = comments;
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
                ", options=" + options +
                ", comments=" + comments +
                ", id='" + id + '\'' +
                ", userdata=" + userdata +
                ", status='" + status + '\'' +
                ", deadline='" + deadline + '\'' +
                ", votes=" + votes +
                ", likes=" + likes +
                '}';
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
}
