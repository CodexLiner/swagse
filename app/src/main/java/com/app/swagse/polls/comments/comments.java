package com.app.swagse.polls.comments;

import com.app.swagse.model.userDetails.Userdata;
import com.google.gson.annotations.SerializedName;

public class comments {
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Userdata getUserdata() {
        return userdata;
    }

    public void setUserdata(Userdata userdata) {
        this.userdata = userdata;
    }

    @SerializedName("comment")
    String comment;
    @SerializedName("user")
    private Userdata userdata;

    @Override
    public String toString() {
        return "comments{" +
                "comment='" + comment + '\'' +
                ", userdata=" + userdata +
                '}';
    }
}
