package de.friendsofdo.workload.android.api;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitInstance {

    private static final String BACKEND_BASE_URL = "http://10.0.2.2:8080";

    public static Retrofit get() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BACKEND_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit;
    }
}
