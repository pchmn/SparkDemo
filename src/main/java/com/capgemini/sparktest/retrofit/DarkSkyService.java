package com.capgemini.sparktest.retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DarkSkyService {

    @GET("forecast/8060f54513f84b8cd50ec70eb0c48904/{latitude},{longitude},{time}?units=si")
    Call<Weather> timeMachineRequest(@Path("latitude") String latitude,
                                     @Path("longitude") String longitude,
                                     @Path("time") long time);

    @GET("forecast/8060f54513f84b8cd50ec70eb0c48904/{latitude},{longitude}?units=si")
    Call<Weather> forecastRequest(@Path("latitude") String latitude,
                                  @Path("longitude") String longitude);
}
