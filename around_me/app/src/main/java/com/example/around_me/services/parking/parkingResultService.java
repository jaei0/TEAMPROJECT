package com.example.around_me.services.parking;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.around_me.R;
import com.example.around_me.entities.ParkingEntity;
import com.example.around_me.utils.parking.db.FavoriteDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class parkingResultService {
    ParkingCommonService parkingCommonService;
    TextView adrTextView;

    public void resultView(AppCompatActivity activity, String guCode, String pay, String day,
                           String time, List<ParkingEntity> parkingList) {

        activity.setContentView(R.layout.parking_item_result);

        parkingCommonService = new ParkingCommonService();
        parkingCommonService.common(activity);

        adrTextView = activity.findViewById(R.id.adrText);

        List<ParkingEntity> filteredList = filterParkingList(activity, guCode, pay, day, time, parkingList);
        List<String> addressList = new ArrayList<>(); // 주소를 저장할 리스트

        for (ParkingEntity filteredEntity : filteredList) {
            // 이전 주소들과 중복되지 않는 주소만 추가
            String extractedAddress = extractAddress(filteredEntity.getAddress());

            if (addressList.contains(extractedAddress)) {
                continue;
            }
            addressList.add(extractedAddress);

            LinearLayout adrContainer = activity.findViewById(R.id.adrSelectContainer);
            LinearLayout buttonLayout = new LinearLayout(activity);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

            Button roadAddrBtn = new Button(activity);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    900,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(40, 30, -30, 30); // 버튼 간의 간격 설정
            roadAddrBtn.setLayoutParams(layoutParams);

            roadAddrBtn.setText(extractedAddress);
            roadAddrBtn.setTextSize(20);
            roadAddrBtn.setBackgroundResource(R.drawable.linearlayout_radius_20dp);
            buttonLayout.addView(roadAddrBtn);
            adrContainer.addView(buttonLayout);

            // 도로명주소 버튼 이벤트 처리
            roadAddrBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 버튼 클릭 시 동작 처리
                    activity.setContentView(R.layout.parking_inforesult);

                    parkingCommonService = new ParkingCommonService();
                    parkingCommonService.common(activity);

                    ImageButton backBtn = activity.findViewById(R.id.backButton);

                    for (ParkingEntity filteredEntity : filteredList) {
                        if (filteredEntity.getAddress().contains(roadAddrBtn.getText())) {

                            LinearLayout nameContainer = activity.findViewById(R.id.resultContainer);
                            LinearLayout buttonLayout = new LinearLayout(activity);
                            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

                            Button nameBtn = new Button(activity);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    900,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.setMargins(40, 30, -30, 30); // 버튼 간의 간격 설정
                            nameBtn.setLayoutParams(layoutParams);

                            nameBtn.setText(filteredEntity.getName());
                            nameBtn.setTextSize(20);
                            nameBtn.setBackgroundResource(R.drawable.linearlayout_radius_20dp);

                            buttonLayout.addView(nameBtn);
                            nameContainer.addView(buttonLayout);

                            // 주차장명 버튼 이벤트 처리
                            nameBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 커스텀 다이얼로그 생성
                                    Dialog dialog = new Dialog(activity);
                                    dialog.setContentView(R.layout.dialog_p_result);

                                    // 다이얼로그 내부의 뷰 요소 가져오기
                                    TextView textTitle = dialog.findViewById(R.id.textTitle);
                                    TextView adrView = dialog.findViewById(R.id.adrView);
                                    TextView adminNameView = dialog.findViewById(R.id.adminNameView);
                                    TextView telNumView = dialog.findViewById(R.id.telNumView);
                                    TextView payView = dialog.findViewById(R.id.payView);
                                    TextView dayView = dialog.findViewById(R.id.dayView);
                                    TextView timeView = dialog.findViewById(R.id.timeView);
                                    TextView detailView = dialog.findViewById(R.id.detailView);

                                    Button buttonClose = dialog.findViewById(R.id.buttonClose);

                                    // 제목과 내용 설정
                                    textTitle.setText(filteredEntity.getName());

                                    adrView.setText(filteredEntity.getAddress());
                                    adminNameView.setText(filteredEntity.getAdminName());
                                    telNumView.setText(filteredEntity.getTelNum());
                                    payView.setText(filteredEntity.getPay());
                                    dayView.setText(filteredEntity.getOpenDay());
                                    if (!filteredEntity.getWeekStartTime().isEmpty() &&
                                            !filteredEntity.getWeekendStartTime().isEmpty()) {
                                        timeView.setText("평일 : " + filteredEntity.getWeekStartTime() + " ~ " +
                                                filteredEntity.getWeekEndTime() + "\n" +
                                                "주말 : " + filteredEntity.getWeekendStartTime() + " ~ " +
                                                filteredEntity.getWeekendEndTime());
                                    } else if (!filteredEntity.getWeekStartTime().isEmpty() &&
                                            filteredEntity.getWeekendStartTime().isEmpty()) {
                                        timeView.setText("평일 : " + filteredEntity.getWeekStartTime() + " ~ " +
                                                filteredEntity.getWeekEndTime());
                                    } else if (filteredEntity.getWeekStartTime().isEmpty() &&
                                            !filteredEntity.getWeekendStartTime().isEmpty()) {
                                        timeView.setText("주말 : " + filteredEntity.getWeekendStartTime() + " ~ " +
                                                filteredEntity.getWeekendEndTime());
                                    }
                                    detailView.setText(filteredEntity.getDetail());

                                    // 하트 버튼 이벤트 처리
                                    ImageButton bookmarkBtn = dialog.findViewById(R.id.heartBtn);

                                    FavoriteDbHelper dbHelper = new FavoriteDbHelper(activity.getApplicationContext());
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                                    String selection = "`이름` = ?";
                                    String[] selectionArgs = {filteredEntity.getName()};

                                    Cursor cursor = db.query(
                                            "favoriteDB",
                                            null,
                                            selection,
                                            selectionArgs,
                                            null,
                                            null,
                                            null
                                    );

                                    if (cursor.getCount() > 0) {
                                        bookmarkBtn.setImageResource(R.drawable.common_bookmark_selected);
                                    } else {
                                        bookmarkBtn.setImageResource(R.drawable.common_bookmark_unselected);
                                    }

                                    bookmarkBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                                            String selection = "`이름` = ?";
                                            String[] selectionArgs = {filteredEntity.getName()};

                                            Cursor cursor = db.query(
                                                    "favoriteDB",
                                                    null,
                                                    selection,
                                                    selectionArgs,
                                                    null,
                                                    null,
                                                    null
                                            );

                                            if (cursor.getCount() > 0) {
                                                // 즐겨찾기에 이미 존재하는 데이터라면, db테이블에서 삭제
                                                bookmarkBtn.setImageResource(R.drawable.common_bookmark_unselected);
                                                Toast.makeText(activity, "'" + filteredEntity.getName() + "'의 즐겨찾기를 해제했습니다.", Toast.LENGTH_SHORT).show();
                                                db.delete("favoriteDB", selection, selectionArgs);
                                            } else {
                                                // 즐겨찾기에 없는 데이터라면, db테이블에 저장
                                                bookmarkBtn.setImageResource(R.drawable.common_bookmark_selected);
                                                Toast.makeText(activity, "'" + filteredEntity.getName() + "'을(를) 즐겨찾기에 추가했습니다.", Toast.LENGTH_SHORT).show();

                                                ContentValues values = new ContentValues();
                                                values.put("이름", filteredEntity.getName());
                                                values.put("주소", filteredEntity.getAddress());
                                                values.put("기관명", filteredEntity.getAdminName());
                                                values.put("전화번호", filteredEntity.getTelNum());
                                                values.put("요금", filteredEntity.getPay());
                                                values.put("요일", filteredEntity.getOpenDay());
                                                if (!filteredEntity.getWeekStartTime().isEmpty()) {
                                                    values.put("평일오픈시간", filteredEntity.getWeekStartTime());
                                                    values.put("평일마감시간", filteredEntity.getWeekEndTime());
                                                }
                                                if (!filteredEntity.getWeekendStartTime().isEmpty()) {
                                                    values.put("주말오픈시간", filteredEntity.getWeekendStartTime());
                                                    values.put("주말마감시간", filteredEntity.getWeekendEndTime());
                                                }
                                                values.put("상세", filteredEntity.getDetail());

                                                db.insert("favoriteDB", null, values);
                                            }
                                            cursor.close();
                                            db.close();
                                        }
                                    });

                                    // 닫기 버튼 클릭 이벤트 처리
                                    buttonClose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            dialog.dismiss(); // 다이얼로그 닫기

                                        }
                                    });

                                    // 다이얼로그 크기 설정
                                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                    dialog.getWindow().setAttributes(layoutParams);

                                    // 다이얼로그 표시
                                    dialog.show();
                                }
                            });

                            backBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    resultView(activity, guCode, pay, day, time, parkingList);

                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public List<ParkingEntity> filterParkingList(Activity activity, String guCode, String pay, String day,
                                                 String time, List<ParkingEntity> parkingList) {
        List<ParkingEntity> filteredList = new ArrayList<>();
        for (ParkingEntity parkingEntity : parkingList) {

            if (day.equals("매일")) {
                day = "평일+토요일+일요일";
            } else if (day.equals("주말")) {
                day = "토요일+일요일";
            }

            String weekStartTime, weekEndTime, weekendStartTime, weekendEndTime;

            if (parkingEntity.getGuCode().equals(guCode) &&
                    parkingEntity.getPay().equals(pay) &&
                    parkingEntity.getOpenDay().equals(day)) {
                if (parkingEntity.getOpenDay().contains("평일")) {
                    weekStartTime = parkingEntity.getWeekStartTime();
                    weekEndTime = parkingEntity.getWeekEndTime();

                    if (isWithinTimeRange(time, weekStartTime, weekEndTime) &&
                            !filteredList.contains(parkingEntity)) {
                        filteredList.add(parkingEntity);
                    }
                }
                if (parkingEntity.getOpenDay().contains("토요일") || parkingEntity.getOpenDay().contains("일요일")) {
                    weekendStartTime = parkingEntity.getWeekendStartTime();
                    weekendEndTime = parkingEntity.getWeekendEndTime();
                    if (isWithinTimeRange(time, weekendStartTime, weekendEndTime) &&
                            !filteredList.contains(parkingEntity)) {
                        filteredList.add(parkingEntity);
                    }
                }
            }

        }
        return filteredList;
    }

    // filterService에서 선택한 시간에 있는 Entity 데이터만 가지고 올 수 있도록 시간 범위 설정하는 메서드
    public static boolean isWithinTimeRange(String time, String startTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            Date currentTime = format.parse(time);
            Date startTimeObj = format.parse(startTime);
            Date endTimeObj = format.parse(endTime);

            // 현재 시간의 시분 정보만 가져옴
            int currentHour = currentTime.getHours();

            // 시작 시간과 종료 시간의 시분 정보를 가져옴
            int startHour = startTimeObj.getHours();
            int endHour = endTimeObj.getHours();

            if (endHour < startHour || (startTime.equals("00:00") && endTime.equals("24:00"))) {
                // 마감 시간이 시작 시간보다 이전인 경우 다음 날로 가정하고 조건을 처리
                return (currentHour >= startHour)
                        || (currentHour <= endHour);
            } else {
                // 일반적인 경우
                return (currentHour >= startHour)
                        && (currentHour <= endHour);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 주소에서 숫자 앞까지만 추출하는 메서드
    private String extractAddress(String address) {

        String[] addressParts = address.split(" ");
        if (addressParts.length > 2) {
            adrTextView.setText(addressParts[0] + " " + addressParts[1]);
            String roadAddress = addressParts[2];
            Pattern pattern = Pattern.compile("(\\D+)");
            Matcher matcher = pattern.matcher(roadAddress);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return "0";
    }
}