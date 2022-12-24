package com.app.swagse.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SongsData implements Serializable {
    @SerializedName("title")
    private String title;

    @SerializedName("song")
    private String song;

    @Override
    public String toString() {
        return "SongsData{" +
                "title='" + title + '\'' +
                ", song='" + song + '\'' +
                ", image='" + image + '\'' +
                ", album_id='" + album_id + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getSong() {
        return song;
    }

    public String getImage() {
        return image;
    }

    public String getAlbum_id() {
        return album_id;
    }


    @SerializedName("image")
    private String image;

    @SerializedName("album_id")
    private String album_id;
}
