package com.example.around_me.services;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.around_me.R;
import com.example.around_me.api.ApiConnection;

public class HomeSelectService {
    private ApiConnection apiConnection;

    public void select(AppCompatActivity activity) {
        activity.setContentView(R.layout.activity_homeselect);
        View parkingAreaLayer = activity.findViewById(R.id.parkingSelect); // 주차장 조회 버튼
        View nearBySubwayLayer = activity.findViewById(R.id.subwaySelect); // 주변상가 조회 버튼

        // 주차장 조회 버튼 이벤트 처리
        parkingAreaLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 주차장 API 호출
                apiConnection = new ApiConnection();
                apiConnection.parkingArea(activity);
            }
        });

        // 주변상가 조회 버튼 이벤트 처리
        nearBySubwayLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 주변상가조회 API 호출
                apiConnection = new ApiConnection();
                apiConnection.nearbySubway(activity);
            }
        });
    }

}



