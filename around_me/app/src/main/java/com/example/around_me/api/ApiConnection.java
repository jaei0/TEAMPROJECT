package com.example.around_me.api;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.around_me.entities.ParkingEntity;
import com.example.around_me.entities.SubwayEntity;
import com.example.around_me.services.parking.ParkingFilterService;
import com.example.around_me.services.subway.SubwayAddService;
import com.example.around_me.services.subway.SubwayFilterService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiConnection {
    protected List<SubwayEntity> subwayList;
    protected List<ParkingEntity> parkingList;
    private Retrofit retrofit;
    private SubwayService subwayService;
    private ParkingService parkingService;
    private SubwayFilterService subwayFilterService;
    private ParkingFilterService parkingFilterService;
    private SubwayAddService subwayAddService;

    public void parkingArea(AppCompatActivity activity) {
        retrofit = ApiClient.getParkingClient();
        parkingService = retrofit.create(ParkingService.class);

        Call<List<ParkingEntity>> call = parkingService.parkingArea();
        call.enqueue(new Callback<List<ParkingEntity>>() {
            @Override
            public void onResponse(Call<List<ParkingEntity>> call, Response<List<ParkingEntity>> response) {
                if (response.isSuccessful()) {
                    // 호출한 데이터 결과를 parkingList 넣기
                    parkingList = response.body();

                    // parkingFilterService의 searchFilter 메서드 호출
                    parkingFilterService = new ParkingFilterService();
                    parkingFilterService.searchFilter(activity, parkingList);
                }
            }

            @Override
            public void onFailure(Call<List<ParkingEntity>> call, Throwable t) {
                // 오류 처리
            }
        });
    }

    public void nearbySubway(AppCompatActivity activity) {
        retrofit = ApiClient.getSubwayClient();
        subwayService = retrofit.create(SubwayService.class);

        Call<List<SubwayEntity>> call = subwayService.nearbySubway();
        call.enqueue(new Callback<List<SubwayEntity>>() {
            @Override
            public void onResponse(Call<List<SubwayEntity>> call, Response<List<SubwayEntity>> response) {
                if (response.isSuccessful()) {
                    // 호출한 데이터 결과를 subwayList에 넣기
                    subwayList = response.body();

                    // addInfo 데이터가 추가된 subwayList 사용하기 위해 infoFilterService클래스의 getAddInfoList 메서드 호출
                    subwayFilterService = new SubwayFilterService();
                    subwayFilterService.getAddInfoList(activity, subwayList);
                }
            }

            @Override
            public void onFailure(Call<List<SubwayEntity>> call, Throwable t) {
                // 오류 처리
            }
        });
    }

    public void subwayAddInfo(AppCompatActivity activity) {
        retrofit = ApiClient.getSubwayClient();
        subwayService = retrofit.create(SubwayService.class);

        Call<List<SubwayEntity>> call = subwayService.nearbySubway();
        call.enqueue(new Callback<List<SubwayEntity>>() {
            @Override
            public void onResponse(Call<List<SubwayEntity>> call, Response<List<SubwayEntity>> response) {
                if (response.isSuccessful()) {
                    // 호출한 데이터 결과를 subwayList에 넣기
                    subwayList = response.body();

                    // SubwayAddService 클래스 호출
                    subwayAddService = new SubwayAddService();
                    subwayAddService.updateAddInfo(activity, subwayList);
                }
            }

            @Override
            public void onFailure(Call<List<SubwayEntity>> call, Throwable t) {
                // 오류 처리
            }
        });
    }
}
