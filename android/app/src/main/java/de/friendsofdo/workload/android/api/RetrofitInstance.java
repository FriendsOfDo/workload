package de.friendsofdo.workload.android.api;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@EBean(scope = EBean.Scope.Singleton)
public class RetrofitInstance {

    // private static final String BACKEND_BASE_URL = "http://10.0.2.2:8080";
    private static final String BACKEND_BASE_URL = "https://workload-149313.appspot.com";

    private EventService eventService;
    private StatusService statusService;

    @AfterInject
    protected void setup() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BACKEND_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        eventService = retrofit.create(EventService.class);
        statusService = retrofit.create(StatusService.class);
    }

    public EventService getEventService() {
        return eventService;
    }

    public StatusService getStatusService() {
        return statusService;
    }
}
