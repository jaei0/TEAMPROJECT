package com.example.around_me.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String SUBWAY_URL = "http://192.168.0.54:8080/";
    private static final String PARKING_URL = "http://192.168.0.54:8088/";
    private static Retrofit retrofit;

    public static Retrofit getSubwayClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(SUBWAY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit getParkingClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(PARKING_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}