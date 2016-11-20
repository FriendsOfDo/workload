package de.friendsofdo.workload.android.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WorkplaceService {

    @POST("/{userId}/workplaces")
    Call<Workplace> save(@Path("userId") String userId, @Body Workplace workplace);
}
