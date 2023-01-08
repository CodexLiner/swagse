package com.app.swagse.polls;

import com.app.swagse.model.category.CategoryItem;
import com.app.swagse.model.userDetails.Userdata;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowPollsResponse {
    @SerializedName("data")
    private List<ShowDataItem> dataItems;


    public List<ShowDataItem> getDataItems() {
        return dataItems;
    }

    public void setDataItems(List<ShowDataItem> dataItems) {
        this.dataItems = dataItems;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @SerializedName("success")
    private String success;

    @Override
    public String toString() {
        return "ShowPollsResponse{" +
                "dataItems=" + dataItems +
                ", success='" + success + '\'' +
                '}';
    }
}
