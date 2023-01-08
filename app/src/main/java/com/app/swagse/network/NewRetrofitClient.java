package com.app.swagse.network;

import android.util.Base64;

import com.app.swagse.BuildConfig;
import com.app.swagse.constants.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewRetrofitClient {

    private static final String AUTH = "Basic " + Base64.encodeToString(("belalkhan:123456").getBytes(), Base64.NO_WRAP);

    private static final String BASE_URL = "http://swagse.fusionapp.site/beautindia/api/";
    private static NewRetrofitClient mInstance;
    private Retrofit retrofit;


    private NewRetrofitClient() {
       /* OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                Request.Builder requestBuilder = original.newBuilder()
                                        .addHeader("Authorization", AUTH)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        }
                ).build();
*/
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(60, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(60, TimeUnit.SECONDS);

        //for logs of api response in debug mode
        if (BuildConfig.DEBUG) {
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(interceptor);
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientBuilder.build())
                .build();
    }

    public static synchronized NewRetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new NewRetrofitClient();
        }
        return mInstance;
    }

    public NewApi getApi() {
        return retrofit.create(NewApi.class);
    }

}
