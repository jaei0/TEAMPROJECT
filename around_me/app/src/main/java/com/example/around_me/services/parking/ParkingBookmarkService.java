package com.example.around_me.services.parking;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.around_me.R;
import com.example.around_me.entities.ParkingEntity;
import com.example.around_me.utils.parking.db.FavoriteDbHelper;

import java.util.ArrayList;
import java.util.List;

public class ParkingBookmarkService {
    ParkingCommonService parkingCommonService;
    ImageView visibleImage;
    TextView visibleText;

    public void bookmark(AppCompatActivity activity) {
        activity.setContentView(R.layout.parking_bookmark);

        parkingCommonService = new ParkingCommonService();
        parkingCommonService.common(activity);

        ImageButton changebtn = activity.findViewById(R.id.p_bookmarkbtn);
        changebtn.setColorFilter(R.color.white);

        visibleImage = activity.findViewById(R.id.visibleImage);
        visibleText = activity.findViewById(R.id.visibleText);

        getAllFavorites(activity);
    }

    public List<ParkingEntity> getAllFavorites(Activity activity) {
        List<ParkingEntity> favoriteItems = new ArrayList<>();

        FavoriteDbHelper dbHelper = new FavoriteDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"이름", "주소", "기관명", "전화번호", "요금", "요일", "평일오픈시간", "평일마감시간", "주말오픈시간", "주말마감시간", "상세"};
        Cursor cursor = db.query("favoriteDB", projection, null, null, null, null, null);

        LinearLayout buttonContainer = activity.findViewById(R.id.buttonContainer);

        if (cursor.moveToFirst()) {
            // 데이터가 있을 때
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("이름"));

                String address = cursor.getString(cursor.getColumnIndexOrThrow("주소"));
                String adminName = cursor.getString(cursor.getColumnIndexOrThrow("기관명"));
                String telNum = cursor.getString(cursor.getColumnIndexOrThrow("전화번호"));
                String pay = cursor.getString(cursor.getColumnIndexOrThrow("요금"));
                String openDay = cursor.getString(cursor.getColumnIndexOrThrow("요일"));
                String weekStartTime = cursor.getString(cursor.getColumnIndexOrThrow("평일오픈시간"));
                String weekEndTime = cursor.getString(cursor.getColumnIndexOrThrow("평일마감시간"));
                String weekendStartTime = cursor.getString(cursor.getColumnIndexOrThrow("주말오픈시간"));
                String weekendEndTime = cursor.getString(cursor.getColumnIndexOrThrow("주말마감시간"));
                String detail = cursor.getString(cursor.getColumnIndexOrThrow("상세"));

                LinearLayout buttonLayout = new LinearLayout(activity);
                LinearLayout.LayoutParams btnLayout = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                buttonLayout.setLayoutParams(btnLayout);
                buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                btnLayout.setMargins(40, 20, 40, 20);
                buttonLayout.setBackgroundResource(R.drawable.linearlayout_radius_20dp);

                Button nameBtn = new Button(activity);
                LinearLayout.LayoutParams nameButtonLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        6
                );
                nameButtonLayoutParams.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
                nameButtonLayoutParams.setMargins(0, 20, 0, 20);
                nameBtn.setLayoutParams(nameButtonLayoutParams);

                nameBtn.setText(name);
                nameBtn.setTextSize(18);
                nameBtn.setBackgroundResource(R.drawable.btn_padding_rl16_tb8);

                ImageView deleteBtn = new ImageView(activity);
                deleteBtn.setImageResource(R.drawable.common_bookmark_xbutton);
                LinearLayout.LayoutParams deleteButtonLayoutParams = new LinearLayout.LayoutParams(50, 50, 1);
                deleteButtonLayoutParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
                deleteButtonLayoutParams.setMargins(0, 0, 35, 0);
                deleteBtn.setLayoutParams(deleteButtonLayoutParams);

                buttonLayout.addView(nameBtn);
                buttonLayout.addView(deleteBtn);
                buttonContainer.addView(buttonLayout);

                // 이름 버튼 이벤트 처리
                nameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 커스텀 다이얼로그 생성
                        Dialog dialog = new Dialog(activity);
                        dialog.setContentView(R.layout.dialog_p_bookmark);

                        // 다이얼로그 내부의 뷰 요소 가져오기
                        TextView nameView = dialog.findViewById(R.id.nameView);
                        TextView adrView = dialog.findViewById(R.id.adrView);
                        TextView adminNameView = dialog.findViewById(R.id.adminNameView);
                        TextView telNumView = dialog.findViewById(R.id.telNumView);
                        TextView payView = dialog.findViewById(R.id.payView);
                        TextView dayView = dialog.findViewById(R.id.dayView);
                        TextView timeView = dialog.findViewById(R.id.timeView);
                        TextView detailView = dialog.findViewById(R.id.detailView);
                        Button buttonClose = dialog.findViewById(R.id.buttonClose);

                        // 제목과 내용 설정
                        nameView.setText(name);
                        nameView.setTextSize(20);

                        adrView.setText(address);
                        adminNameView.setText(adminName);
                        telNumView.setText(telNum);
                        payView.setText(pay);
                        dayView.setText(openDay);
                        if (weekStartTime != null && !weekStartTime.isEmpty() &&
                                weekendStartTime != null && !weekendStartTime.isEmpty()) {
                            timeView.setText("평일 : " + weekStartTime + " ~ " +
                                    weekEndTime + "\n" +
                                    "주말 : " + weekendStartTime + " ~ " +
                                    weekendEndTime);
                        } else if (weekStartTime != null && !weekStartTime.isEmpty() &&
                                weekendStartTime == null || weekendStartTime.isEmpty()) {
                            timeView.setText("평일 : " + weekStartTime + " ~ " +
                                    weekEndTime);
                        } else if (weekStartTime == null || weekStartTime.isEmpty() &&
                                weekendStartTime != null && !weekendStartTime.isEmpty()) {
                            timeView.setText("주말 : " + weekendStartTime + " ~ " +
                                    weekendEndTime);
                        }
                        detailView.setText(detail);


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

                // 삭제 버튼 이벤트 처리
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "'" + name + "'의 즐겨찾기를 해제했습니다.", Toast.LENGTH_SHORT).show();

                        // 해당 아이템에 대한 데이터 삭제
                        dbHelper.deleteItem(name);

                        // 화면에서 버튼과 밑줄을 제거
                        buttonContainer.removeView(buttonLayout);

                    }
                });
            } while (cursor.moveToNext());
        } else {
            // 데이터가 없을 때
            visibleImage.setVisibility(View.VISIBLE);
            visibleText.setVisibility(View.VISIBLE);
        }

        cursor.close();
        db.close();

        return favoriteItems;
    }
}
