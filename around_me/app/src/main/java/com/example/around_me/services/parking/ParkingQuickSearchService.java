package com.example.around_me.services.parking;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.around_me.R;
import com.example.around_me.utils.parking.db.QuickSearchDbHelper;

import java.util.ArrayList;

public class ParkingQuickSearchService {

    ArrayList<String> selectList = new ArrayList<>();
    String nickName, selectedGu, guCode, pay, selectedDay, time;

    public interface QuickSelectListener {
        void onQuickSelect(ArrayList<String> selectList);
    }

    public void quickRegister(Activity activity, String selectedGu, String guCode, String pay, String day,
                              String time) {
        // 커스텀 다이얼로그 생성
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_p_quick_register);

        // 다이얼로그 내부의 뷰 요소 가져오기
        EditText editName = dialog.findViewById(R.id.nickNameText);
        TextView quickGu = dialog.findViewById(R.id.guView);
        TextView quickPay = dialog.findViewById(R.id.payView);
        TextView quickDay = dialog.findViewById(R.id.dayView);
        TextView quickTime = dialog.findViewById(R.id.timeView);
        Button registerBtn = dialog.findViewById(R.id.registerBtn);
        Button closeBtn = dialog.findViewById(R.id.closeBtn);

        editName.setTextSize(16);  // 원하는 텍스트 크기로 설정

        if (day.equals("평일+토요일+일요일")) {
            day = "매일";
        } else if (day.equals("토요일+일요일")) {
            day = "주말";
        }

        quickGu.setText(selectedGu);
        quickPay.setText(pay);
        quickDay.setText(day);
        quickTime.setText(time);

        // 등록 버튼 클릭 이벤트 처리
        String selectedDay = day;
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = editName.getText().toString();

                if (nickName.length() > 3) {
                    Toast.makeText(activity, "별칭은 3글자 이하만 가능합니다.", Toast.LENGTH_SHORT).show();
                    return; // 등록 중단
                }

                String selection = "`별칭` = ?";
                String[] selectionArgs = {nickName};

                QuickSearchDbHelper dbHelper = new QuickSearchDbHelper(activity.getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                Cursor cursor = db.query(
                        "quickSearchDB",
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );

                if (cursor.getCount() > 0) {
                    // 이미 존재하는 별칭일 때
                    Toast.makeText(activity, "이미 존재하는 별칭입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 없는 별칭이라면, db테이블에 저장
                    Toast.makeText(activity, nickName+"이(가) 등록되었습니다.", Toast.LENGTH_SHORT).show();

                    ContentValues values = new ContentValues();
                    values.put("별칭", nickName);
                    values.put("구이름", selectedGu);
                    values.put("구코드", guCode);
                    values.put("요금", pay);
                    values.put("요일", selectedDay);
                    values.put("시간", time);

                    db.insert("quickSearchDB", null, values);
                }
                cursor.close();
                db.close();
                dialog.dismiss(); // 다이얼로그 닫기
            }
        });

        // 닫기 버튼 클릭 이벤트 처리
        closeBtn.setOnClickListener(new View.OnClickListener() {
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

    public void quickSelect(Activity activity, final QuickSelectListener listener) {

        QuickSearchDbHelper dbHelper = new QuickSearchDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 커스텀 다이얼로그 생성
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_p_quick_select);

        // 다이얼로그 내부의 뷰 요소 가져오기
        Button selectBtn = dialog.findViewById(R.id.selectBtn);
        Button closeBtn = dialog.findViewById(R.id.closeBtn);
        Button deleteBtn = dialog.findViewById(R.id.deleteBtn);



        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);

        String[] projection = {"별칭", "구이름", "구코드", "요금", "요일", "시간"};
        Cursor cursor = db.query("quickSearchDB", projection, null, null, null, null, null);

        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            nickName = cursor.getString(cursor.getColumnIndexOrThrow("별칭"));
            selectedGu = cursor.getString(cursor.getColumnIndexOrThrow("구이름"));
            guCode = cursor.getString(cursor.getColumnIndexOrThrow("구코드"));
            pay = cursor.getString(cursor.getColumnIndexOrThrow("요금"));
            selectedDay = cursor.getString(cursor.getColumnIndexOrThrow("요일"));
            time = cursor.getString(cursor.getColumnIndexOrThrow("시간"));

            RadioButton radioButton = new RadioButton(new ContextThemeWrapper(activity, R.style.SmallRadioButtonStyle));

            radioButton.setText(nickName);

            String additionalText = selectedGu + "/" + pay + "/" + selectedDay + "/" + time;
            int repetitionCount = 3;  // 기본적으로 4번 반복

            if (nickName.length() == 2) {
                repetitionCount = 2;
            } else if (nickName.length() == 3) {
                repetitionCount = 1;
            }

            StringBuilder stringBuilder = new StringBuilder(radioButton.getText());
            for (int j = 0; j < repetitionCount; j++) {
                if (nickName.matches("[0-9a-z]+")) {
                    stringBuilder.append(" "); // 숫자나 소문자 알파벳인 경우 공백 한 칸 추가
                }
                stringBuilder.append("   ");
            }

            stringBuilder.append("  ");

            stringBuilder.append(additionalText);
            radioButton.setText(stringBuilder.toString());
            radioButton.setTextSize(15);
            radioButton.setId(i);
            radioGroup.addView(radioButton);

            i++;

            cursor.moveToNext();
        }

        // 선택 버튼 클릭 이벤트 처리
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = dialog.findViewById(selectedRadioButtonId);
                    String radioButtonText = selectedRadioButton.getText().toString();

                    String[] radioText = radioButtonText.split(" ");

                    String selection = "`별칭` = ?";
                    String[] selectionArgs = {radioText[0]};

                    Cursor cursor = db.query(
                            "quickSearchDB",
                            null,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            null
                    );
                    while (cursor.moveToNext()) {

                        nickName = cursor.getString(cursor.getColumnIndexOrThrow("별칭"));
                        selectedGu = cursor.getString(cursor.getColumnIndexOrThrow("구이름"));
                        guCode = cursor.getString(cursor.getColumnIndexOrThrow("구코드"));
                        pay = cursor.getString(cursor.getColumnIndexOrThrow("요금"));
                        selectedDay = cursor.getString(cursor.getColumnIndexOrThrow("요일"));
                        time = cursor.getString(cursor.getColumnIndexOrThrow("시간"));

                        selectList.add(nickName);
                        selectList.add(selectedGu);
                        selectList.add(guCode);
                        selectList.add(pay);
                        selectList.add(selectedDay);
                        selectList.add(time);
                    }
                    cursor.close();
                    db.close();
                    dialog.dismiss(); // 다이얼로그 닫기

                    // 선택 완료 후에 리스너 호출하여 selectList 전달
                    if (listener != null) {
                        listener.onQuickSelect(selectList);
                    }

                } else {
                    // 선택된 라디오버튼이 없는 경우 처리
                    Toast.makeText(activity, "버튼을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 닫기 버튼 클릭 이벤트 처리
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 다이얼로그 닫기
            }
        });

        // 삭제 버튼 클릭 이벤트 처리
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 해당 아이템에 대한 데이터 삭제
                dbHelper.deleteItem(nickName);
                Toast.makeText(activity, nickName+"을(를) 삭제했습니다.", Toast.LENGTH_SHORT).show();

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
}
