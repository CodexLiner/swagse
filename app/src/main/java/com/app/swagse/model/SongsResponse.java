package com.app.swagse.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SongsResponse implements Serializable {

    @SerializedName("success")
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<SongsData> getDataList() {
        return dataList;
    }

    public void setDataList(List<SongsData> dataList) {
        this.dataList = dataList;
    }

    @SerializedName("data")
    private List<SongsData> dataList;
}
