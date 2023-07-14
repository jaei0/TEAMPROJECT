package com.example.around_me.services.subway;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.around_me.R;
import com.example.around_me.entities.SubwayEntity;
import com.example.around_me.utils.subway.AddInfoDbHelper;

import java.util.ArrayList;
import java.util.List;

public class SubwayAddService {
    SubwayCommonService commonService;
    private String selectedLine, selectedStation, selectedCategory;

    public void updateAddInfo(AppCompatActivity activity, List<SubwayEntity> subwayList) {
        activity.setContentView(R.layout.subway_addinfo);

        commonService = new SubwayCommonService();
        commonService.common(activity);

        ImageButton changebtn = activity.findViewById(R.id.s_createbtn);
        changebtn.setColorFilter(R.color.white);

        EditText nameContent = activity.findViewById(R.id.nameContent);
        EditText detailContent = activity.findViewById(R.id.detailContent);
        EditText timeContent = activity.findViewById(R.id.timeContent);

        Spinner line = activity.findViewById(R.id.spinner_line); // 호선 선택 Spinner
        Spinner station = activity.findViewById(R.id.spinner_station); // 역 선택 Spinner
        Spinner category = activity.findViewById(R.id.spinner_category); // 카테고리 선택 Spinner

        Button submitBtn = activity.findViewById(R.id.buttonSubmit); // 등록 버튼

        // 데이터 소스 준비 (문자열 배열 사용)
        String[] lineData = {" 호선 ", "1호선", "2호선", "3호선"};

        // 어댑터 생성 및 설정 (호선 Spinner)
        ArrayAdapter<String> lineAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, lineData);
        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        line.setAdapter(lineAdapter);

        // 어댑터 생성 및 설정 (역 Spinner)
        ArrayList<String> stationData = new ArrayList<>(); // 데이터를 담을 ArrayList
        stationData.add(" 역 ");
        ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, stationData);
        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        station.setAdapter(stationAdapter);

        // 어댑터 생성 및 설정 (카테고리 Spinner)
        String[] categoryData = {"카테고리", "먹거리", "살거리", "문화 휴식"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, categoryData);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);

        // 호선 Spinner에서 선택 이벤트 처리
        line.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLine = parent.getItemAtPosition(position).toString();
                // 각 호선에 해당하는 데이터만 역 Spinner에 설정
                ArrayList<String> stationList = new ArrayList<>(); // 새로운 데이터를 담을 ArrayList
                for (SubwayEntity subwayEntity : subwayList) {
                    if (subwayEntity.getLine().equals(selectedLine)) {
                        if (!stationList.toString().contains(subwayEntity.getStation())) {
                            stationList.add(subwayEntity.getStation());
                        }
                    }
                }
                stationAdapter.clear(); // 기존의 데이터 삭제
                stationAdapter.add(" 역 ");
                stationAdapter.addAll(stationList); // 새로운 데이터 추가
                stationAdapter.notifyDataSetChanged(); // 어댑터 갱신
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        });

        // 역 선택 Spinner에서 선택 이벤트 처리
        station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        });

        // 카테고리 선택 Spinner에서 선택 이벤트 처리
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        });

        // 등록하기 버튼 이벤트 처리
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 항목 모두 선택하고, 내용 모두 입력했을 때 db 처리
                if (!selectedLine.equals(" 호선 ") &&
                        !selectedStation.equals(" 역 ") &&
                        !selectedCategory.equals("카테고리") &&
                        !nameContent.getText().toString().isEmpty() &&
                        !detailContent.getText().toString().isEmpty() &&
                        !timeContent.getText().toString().isEmpty()) {

                    // 커스텀 다이얼로그 생성
                    Dialog dialog = new Dialog(activity);
                    dialog.setContentView(R.layout.dialog_s_addinfo);

                    // 다이얼로그 내부의 뷰 요소 가져오기
                    TextView nameView = dialog.findViewById(R.id.nameView);
                    TextView lineView = dialog.findViewById(R.id.lineView);
                    TextView stationView = dialog.findViewById(R.id.stationView);
                    TextView categoryView = dialog.findViewById(R.id.categoryView);
                    TextView detailView = dialog.findViewById(R.id.detailView);
                    TextView timeView = dialog.findViewById(R.id.timeView);

                    Button modifyBtn = dialog.findViewById(R.id.modifyBtn);
                    Button finishBtn = dialog.findViewById(R.id.finishBtn);

                    // 제목과 내용 설정
                    nameView.setText(nameContent.getText().toString());
                    nameView.setTextSize(25);
//                    textDescription.setText("\n호선: " + selectedLine +
//                            "\n\n역명: " + selectedStation +
//                            "\n\n구분: " + selectedCategory +
//                            "\n\n상세: " + detailContent.getText().toString() +
//                            "\n\n시간(분): " + timeContent.getText().toString() + "\n\n");

                    lineView.setText(selectedLine);
                    lineView.setTextSize(20);
                    stationView.setText(selectedStation);
                    stationView.setTextSize(20);
                    categoryView.setText(selectedCategory);
                    categoryView.setTextSize(20);
                    detailView.setText(detailContent.getText().toString());
                    detailView.setTextSize(20);
                    timeView.setText(timeContent.getText().toString());
                    timeView.setTextSize(20);


                    // 수정 버튼 클릭 이벤트 처리
                    modifyBtn.setOnClickListener(new View.OnClickListener() {
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

                    // 다이얼로그의 등록 버튼 눌렀을 때
                    finishBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            boolean isAlreadyExists = false;

                            for (SubwayEntity subwayEntity : subwayList) {
                                if (nameContent.getText().toString().equals(subwayEntity.getName())) {
                                    isAlreadyExists = true;
                                    Toast.makeText(activity, "이미 존재합니다.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    break;
                                }
                            }

                            if (!isAlreadyExists) {
                                //db 처리
                                AddInfoDbHelper dbHelper = new AddInfoDbHelper(activity.getApplicationContext());
                                SQLiteDatabase db = dbHelper.getWritableDatabase();

                                ContentValues values = new ContentValues();

                                values.put("호선", selectedLine);
                                values.put("역명", selectedStation);
                                values.put("구분", selectedCategory);
                                values.put("상호", nameContent.getText().toString());
                                values.put("상세", detailContent.getText().toString());
                                values.put("시간", timeContent.getText().toString());

                                db.insert("addInfoDB", null, values);
                                db.close();
                                Toast.makeText(activity, "등록 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });

                } else {
                    // 선택되지 않은 항목이 있을 때 처리
                    Toast.makeText(activity, "항목을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
