package de.friendsofdo.workload.android.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventService {

    @POST("/{userId}/events")
    Call<Event> save(@Path("userId") String userId, @Body Event event);
}
