package com.example.around_me.services.subway;

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
import com.example.around_me.entities.SubwayEntity;
import com.example.around_me.utils.subway.BookmarkDbHelper;

import java.util.ArrayList;
import java.util.List;

public class SubwayBookmarkService {
    SubwayCommonService subwayCommonService;
    ImageView visibleImage;
    TextView visibleText;

    public void bookmark(AppCompatActivity activity) {

        activity.setContentView(R.layout.subway_bookmark);

        subwayCommonService = new SubwayCommonService();
        subwayCommonService.common(activity);

        ImageButton changebtn = activity.findViewById(R.id.s_bookmarkbtn);
        changebtn.setColorFilter(R.color.white);

        visibleImage = activity.findViewById(R.id.visibleImage);
        visibleText = activity.findViewById(R.id.visibleText);

        getAllFavorites(activity);
    }

    public List<SubwayEntity> getAllFavorites(Activity activity) {
        List<SubwayEntity> favoriteItems = new ArrayList<>();

        BookmarkDbHelper dbHelper = new BookmarkDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"호선", "역명", "구분", "상호", "상세", "시간"};
        Cursor cursor = db.query("bookmarkDB", projection, null, null, null, null, null);

        LinearLayout buttonContainer = activity.findViewById(R.id.buttonContainer);

        if (cursor.moveToFirst()) {
            // 데이터가 있을 때
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("상호"));

                String line = cursor.getString(cursor.getColumnIndexOrThrow("호선"));
                String station = cursor.getString(cursor.getColumnIndexOrThrow("역명"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("구분"));
                String detail = cursor.getString(cursor.getColumnIndexOrThrow("상세"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("시간"));

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

                // 상호 버튼 이벤트 처리
                nameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 커스텀 다이얼로그 생성
                        Dialog dialog = new Dialog(activity);
                        dialog.setContentView(R.layout.dialog_s_bookmark);

                        // 다이얼로그 내부의 뷰 요소 가져오기
                        TextView nameView = dialog.findViewById(R.id.nameView);

                        TextView lineView = dialog.findViewById(R.id.lineView);
                        TextView stationView = dialog.findViewById(R.id.stationView);
                        TextView categoryView = dialog.findViewById(R.id.categoryView);
                        TextView timeView = dialog.findViewById(R.id.timeView);
                        TextView detailView = dialog.findViewById(R.id.detailView);

                        Button buttonClose = dialog.findViewById(R.id.buttonClose);

                        // 제목과 내용 설정
                        nameView.setText(name);

                        lineView.setText(line);
                        stationView.setText(station);
                        categoryView.setText(category);
                        timeView.setText("도보로 약 " +time+"분 소요");
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
//                    buttonContainer.removeView(underline);
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
