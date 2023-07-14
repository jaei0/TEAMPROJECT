package com.example.around_me.services.subway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.around_me.MainActivity;
import com.example.around_me.R;
import com.example.around_me.api.ApiConnection;
import com.example.around_me.services.HomeSelectService;

public class SubwayCommonService extends AppCompatActivity{


    public void common(AppCompatActivity activity) {


        ImageButton homeBtn = activity.findViewById(R.id.s_homebtn);
        ImageButton searchbtn = activity.findViewById(R.id.s_searchbtn);
        ImageButton createbtn = activity.findViewById(R.id.s_createbtn);
        ImageButton bookmarkBtn = activity.findViewById(R.id.s_bookmarkbtn);


        // 홈 버튼 이벤트 처리
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeSelectService homeSelectService = new HomeSelectService();
                homeSelectService.select(activity);

            }
        });

        // 검색으로 돌아가는 버튼 이벤트 처리
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiConnection apiConnection = new ApiConnection();
                apiConnection.nearbySubway(activity);
            }
        });

        // 글쓰기 버튼 이벤트 처리
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiConnection apiConnection = new ApiConnection();
                apiConnection.subwayAddInfo(activity);
            }
        });

        // 즐겨찾기 버튼 이벤트 처리
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubwayBookmarkService buttonService = new SubwayBookmarkService();
                buttonService.bookmark(activity);
            }
        });



    }
}
