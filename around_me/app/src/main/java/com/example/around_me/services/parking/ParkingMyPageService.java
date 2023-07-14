package com.example.around_me.services.parking;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.around_me.R;
import com.example.around_me.utils.parking.db.IdentityDbHelper;
import com.example.around_me.utils.parking.slide.MyAdapter;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator3;

public class ParkingMyPageService extends AppCompatActivity {
    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 3;
    private CircleIndicator3 mIndicator;
    private Timer timer;
    private TimerTask timerTask;
    private static final int PAGE_TRANSITION_DURATION = 3;

    ParkingCommonService parkingCommonService;
    String id, name, birth, address, address2, telNum, carNum;
    public void identity(AppCompatActivity activity) {
        activity.setContentView(R.layout.parking_identity);

        parkingCommonService = new ParkingCommonService();
        parkingCommonService.common(activity);

        ImageButton changebtn = activity.findViewById(R.id.p_identitybtn);
        changebtn.setColorFilter(R.color.white);

        TextView birthdayView = activity.findViewById(R.id.birthdayView);
        TextView addressView = activity.findViewById(R.id.addressView);
        TextView contactView = activity.findViewById(R.id.contactView);
        TextView carNumView = activity.findViewById(R.id.carNumberView);
        TextView profileNameView = activity.findViewById(R.id.profileNameView);

        Button identityBtn = activity.findViewById(R.id.identityBtn);

        IdentityDbHelper dbHelper = new IdentityDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {"아이디","이름","생년월일","주소","상세주소","연락처","차량번호"};
        Cursor cursor = db.query("identityDB", projection, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            profileNameView.setText("회원정보를 찾을 수 없습니다.");
            identityBtn.setText("나의 정보 등록");
        } else {
            while (cursor.moveToNext()) {
                id = cursor.getString(cursor.getColumnIndexOrThrow("아이디"));

                name = cursor.getString(cursor.getColumnIndexOrThrow("이름"));
                birth = cursor.getString(cursor.getColumnIndexOrThrow("생년월일"));
                address = cursor.getString(cursor.getColumnIndexOrThrow("주소"));
                address2 = cursor.getString(cursor.getColumnIndexOrThrow("상세주소"));
                telNum = cursor.getString(cursor.getColumnIndexOrThrow("연락처"));
                carNum = cursor.getString(cursor.getColumnIndexOrThrow("차량번호"));

                // 제목과 내용 설정

                profileNameView.setText(name+"님 환영합니다!");
                profileNameView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                profileNameView.setTextSize(25);
                birthdayView.setText(birth);
                addressView.setText(address + " " + address2);
                contactView.setText(telNum);
                carNumView.setText(carNum);
            }
        }

        // 나의정보수정/등록 버튼 이벤트 처리
        identityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParkingIdentityService identityService = new ParkingIdentityService();
                if(identityBtn.getText().equals("나의 정보 등록")){
                    identityService.register(activity);
                } else {
                    identityService.modify(activity, id, name, birth, address, address2, telNum, carNum);
                }
            }
        });

        /**
         * 가로 슬라이드 뷰 Fragment
         */

        // ViewPager2
        mPager = activity.findViewById(R.id.viewpager);
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        // Adapter
        pagerAdapter = new MyAdapter(activity, num_page);
        mPager.setAdapter(pagerAdapter);
        // Indicator
        mIndicator = activity.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page, 0);

        mPager.setCurrentItem(0); // 시작 지점
        mPager.setOffscreenPageLimit(3); // 최대 이미지 수
        mPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
//                float absPos = Math.abs(position);
//                page.setAlpha(1 - absPos);
//                page.setScaleY(0.85f + (0.15f * (1 - absPos)));

                // 페이지 전환 애니메이션을 커스터마이즈하는 코드를 작성합니다.
                // position 매개변수는 현재 페이지의 위치를 나타내며, -1부터 1까지의 범위입니다.

                // 절대값을 사용하여 현재 페이지와의 거리를 계산합니다.
                float absPosition = Math.abs(position);

                // 페이지 전환 애니메이션 지속 시간을 계산합니다.
                int transitionDuration = (int) (PAGE_TRANSITION_DURATION * absPosition);

                // 페이지 전환 애니메이션 속도 조절을 위해 애니메이션 지속 시간을 설정합니다.
                page.animate().setDuration(transitionDuration).start();
            }


        });

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }

                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position % num_page);
            }
        });

        // 자동 스크롤 시작
        startAutoScroll(3000); // 3초마다 자동 스크롤


    }
    private void startAutoScroll(int delay) {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mPager.post(() -> {
                    int currentItem = mPager.getCurrentItem();
                    mPager.setCurrentItem(currentItem + 1);
                });
            }
        };

        timer.schedule(timerTask, delay, delay);
    }


}


