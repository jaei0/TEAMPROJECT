package com.example.around_me.services.parking;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.around_me.R;
import com.example.around_me.api.ApiConnection;
import com.example.around_me.services.HomeSelectService;

public class ParkingCommonService {
    public void common(AppCompatActivity activity) {
        ImageButton homeBtn = activity.findViewById(R.id.p_homebtn);
        ImageButton searchbtn = activity.findViewById(R.id.p_searchbtn);
        ImageButton bookmarkBtn = activity.findViewById(R.id.p_bookmarkbtn);
        ImageButton identitybtn = activity.findViewById(R.id.p_identitybtn);

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
                apiConnection.parkingArea(activity);
            }
        });

        // 즐겨찾기 버튼 이벤트 처리
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParkingBookmarkService bookmarkService = new ParkingBookmarkService();
                bookmarkService.bookmark(activity);
            }
        });

//         사용자 설정 버튼 이벤트 처리
        identitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParkingMyPageService identityService = new ParkingMyPageService();
                identityService.identity(activity);
            }
        });

    }
}
