package com.app.swagse.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.app.swagse.R;
import com.app.swagse.adapter.NavWatchAdapter;
import com.app.swagse.adapter.SearchRecyclerViewAdapter;
import com.app.swagse.constants.Constants;
import com.app.swagse.controller.App;
import com.app.swagse.helper.OwnerGlobal;
import com.app.swagse.model.swagTube.SwagTubeResponse;
import com.app.swagse.model.swagTube.SwagtubedataItem;
import com.app.swagse.network.Api;
import com.app.swagse.network.RetrofitClient;
import com.app.swagse.sharedpreferences.PrefConnect;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.swagse.helper.OwnerGlobal.toast;
import static kotlin.reflect.jvm.internal.impl.builtins.StandardNames.FqNames.charSequence;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchRecyclerView;
    private Api apiInterface;
    private AppCompatImageView back_icon;
    private AppCompatEditText place_search;
    Activity activity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_search);
        apiInterface = RetrofitClient.getInstance().getApi();
        back_icon = findViewById(R.id.back_icon);
        place_search = findViewById(R.id.place_search);
        searchRecyclerView = findViewById(R.id.searchRecyclerView);
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
        place_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (v.getText().toString().length() >= 3) {
                        getSearchData(v.getText().toString().toString());
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        //Find the currently focused view, so we can grab the correct window token from it.
                        View view = activity.getCurrentFocus();
                        //If no view currently has focus, create a new one, just so we can grab a window token from it
                        if (view == null) {
                            view = new View(activity);
                        }
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        return true;
                    }

                }
                return false;
            }
        });
//        place_search.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                //  filter(charSequence.toString().substring(0,3));
//                if (charSequence.toString().length() >= 3) {
//                    getSearchData(charSequence.toString());
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                //after the change calling the method and passing the search input
//            }
//        });
//        getVideoHistory();

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getSearchData(String searchData) {
        if (App.isOnline()) {
            Call<SwagTubeResponse> userResponseCall = apiInterface.searchVideo(PrefConnect.readString(SearchActivity.this, Constants.USERID, ""), searchData);
            userResponseCall.enqueue(new Callback<SwagTubeResponse>() {
                @Override
                public void onResponse(Call<SwagTubeResponse> call, Response<SwagTubeResponse> response) {

                    if (response.code() == Constants.SUCCESS) {

                        if (response.body().getStatus().equals("1")) {

                            List<SwagtubedataItem> swagTubeDataList = response.body().getSwagtubedata();

                            if (swagTubeDataList.size() != 0 && !swagTubeDataList.isEmpty()) {
                                SearchRecyclerViewAdapter swagTubeAdapter = new SearchRecyclerViewAdapter(SearchActivity.this, swagTubeDataList);
                                searchRecyclerView.setAdapter(swagTubeAdapter);

                            }
                        } else if (response.code() == Constants.FAILED) {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                toast(SearchActivity.this, jObjError.getString("response_msg"));
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        } else if (response.code() == Constants.UNAUTHORIZED) {
                            OwnerGlobal.LoginRedirect(SearchActivity.this);
                        }

                    }
                }

                @Override
                public void onFailure(Call<SwagTubeResponse> call, Throwable t) {

                }
            });
        }
    }
}