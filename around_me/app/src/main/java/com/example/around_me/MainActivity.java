package com.example.around_me;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import com.example.around_me.services.HomeSelectService;

public class MainActivity extends AppCompatActivity {
    private static final long DELAY_TIME = 2000; // 2초
    private HomeSelectService homeSelectService;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handler 생성
        handler = new Handler(Looper.getMainLooper());

        // 일정 시간 후에 다음 페이지 호출 코드 실행
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                performApiCall();
            }
        }, DELAY_TIME);
    }

    private void performApiCall() {
        homeSelectService = new HomeSelectService();
        homeSelectService.select(this);
    }
}
