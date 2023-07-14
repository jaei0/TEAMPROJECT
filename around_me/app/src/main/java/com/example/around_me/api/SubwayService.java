package com.example.around_me.api;

import com.example.around_me.entities.SubwayEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SubwayService {
    @GET(value = "/nearby_subway")
    Call<List<SubwayEntity>> nearbySubway();
}