package com.example.around_me.services.parking;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.around_me.R;
import com.example.around_me.utils.parking.db.IdentityDbHelper;

public class ParkingIdentityService {
    EditText nameText, birthText, addressText, addressText2, telNumText, carNumText;
    Button modifyBtn, registerBtn, deleteBtn, closeBtn;
    IdentityDbHelper dbHelper;

    ParkingMyPageService parkingMyPageService = new ParkingMyPageService();

    public void register(AppCompatActivity activity) {
        // 커스텀 다이얼로그 생성
        Dialog dialog = createDialog(activity);

        registerBtn.setVisibility(View.VISIBLE); // 등록 버튼을 보이도록 설정

        // 등록 버튼 클릭 이벤트 처리
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put("아이디", "1"); // 아이디는 고정된 text임(수정/삭제 할때 필요한 고유한 아이디가 필요해서 사용)
                values.put("이름", nameText.getText().toString());
                values.put("생년월일", birthText.getText().toString());
                values.put("주소", addressText.getText().toString());
                values.put("상세주소", addressText2.getText().toString());
                values.put("연락처", telNumText.getText().toString());
                values.put("차량번호", carNumText.getText().toString());

                db.insert("identityDB", null, values);
                db.close();
                Toast.makeText(activity, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // 다이얼로그 닫기
                parkingMyPageService.identity(activity);
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

    public void modify(AppCompatActivity activity, String id, String name, String birth,
                       String address,  String address2, String telNum, String carNum) {
        // 커스텀 다이얼로그 생성
        Dialog dialog = createDialog(activity);

        modifyBtn.setVisibility(View.VISIBLE); // 수정 버튼을 보이도록 설정
        deleteBtn.setVisibility(View.VISIBLE); // 삭제 버튼을 보이도록 설정

        nameText.setText(name);
        birthText.setText(birth);
        addressText.setText(address);
        addressText2.setText(address2);
        telNumText.setText(telNum);
        carNumText.setText(carNum);

        // 수정 버튼 클릭 이벤트 처리
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modifiedName = nameText.getText().toString();
                String modifiedBirth = birthText.getText().toString();
                String modifiedAddress = addressText.getText().toString();
                String modifiedAddress2 = addressText2.getText().toString();
                String modifiedTelNum = telNumText.getText().toString();
                String modifiedCarNum = carNumText.getText().toString();

                dbHelper.updateById(id, modifiedName, modifiedBirth, modifiedAddress,
                        modifiedAddress2, modifiedTelNum, modifiedCarNum);

                Toast.makeText(activity, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // 다이얼로그 닫기
                parkingMyPageService.identity(activity);
            }
        });

        // 삭제 버튼 클릭 이벤트 처리
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteItem(id);
                Toast.makeText(activity, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // 다이얼로그 닫기
                parkingMyPageService.identity(activity);
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
        parkingMyPageService.identity(activity);
    }

    private Dialog createDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_p_identity);

        nameText = dialog.findViewById(R.id.nameText);
        birthText = dialog.findViewById(R.id.birthText);
        addressText = dialog.findViewById(R.id.addressText);
        addressText2 = dialog.findViewById(R.id.addressText2);
        telNumText = dialog.findViewById(R.id.telNumText);
        carNumText = dialog.findViewById(R.id.carNumText);

        modifyBtn = dialog.findViewById(R.id.modifyBtn);
        registerBtn = dialog.findViewById(R.id.registerBtn);
        deleteBtn = dialog.findViewById(R.id.deleteBtn);
        closeBtn = dialog.findViewById(R.id.closeBtn);

        dbHelper = new IdentityDbHelper(activity.getApplicationContext());

        return dialog;
    }

}
