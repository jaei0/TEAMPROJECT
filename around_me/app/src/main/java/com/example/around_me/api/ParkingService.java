package com.example.around_me.api;

import com.example.around_me.entities.ParkingEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ParkingService {
    @GET(value = "/parking_area")
    Call<List<ParkingEntity>> parkingArea();
}