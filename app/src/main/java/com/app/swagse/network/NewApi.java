package com.app.swagse.network;

import com.app.swagse.model.GetOTPResponse;
import com.app.swagse.model.category.CategoryResponse;
import com.app.swagse.model.userDetails.UserDetailResponse;
import com.app.swagse.polls.ShowPollsResponse;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NewApi {

    //Show Polls

    @FormUrlEncoded
    @POST("polling_detail")
    Call<ShowPollsResponse> getPolls(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("votes")
    Call<NewApiResponse> vote (@Field("user_id") String userId , @Field("polling_id") String polling_id , @Field("answer") String answer);

    @FormUrlEncoded
    @POST("polling")
    Call<NewApiResponse> addPoll (@Field("user_id") String userId , @Field("question") String question , @Field("options") JSONArray options , @Field("end_date") String end_date);

}