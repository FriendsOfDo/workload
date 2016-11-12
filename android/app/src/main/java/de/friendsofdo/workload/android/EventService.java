package de.friendsofdo.workload.android;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EventService {

    @POST("events")
    Call<Event> save(@Body Event event);
}
