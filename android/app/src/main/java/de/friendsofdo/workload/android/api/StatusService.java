package de.friendsofdo.workload.android.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StatusService {

    @GET("/{userId}/status")
    Call<Status> get(@Path("userId") String userId);
}
