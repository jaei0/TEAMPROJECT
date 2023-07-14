package com.example.around_me.services.subway;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.around_me.entities.SubwayEntity;
import com.example.around_me.R;
import com.example.around_me.utils.subway.AddInfoDbHelper;

import java.util.ArrayList;
import java.util.List;

public class SubwayFilterService extends AppCompatActivity {
    private String selectedLine, selectedStation, selectedCategory;
    SubwayResultService subwayResultService;
    SubwayCommonService subwayCommonService;

    public void searchFilter(AppCompatActivity activity, List<SubwayEntity> subwayList) {
        activity.setContentView(R.layout.subway_infofilter);

        subwayCommonService = new SubwayCommonService();
        subwayCommonService.common(activity);

        ImageButton changebtn = activity.findViewById(R.id.s_searchbtn);
        changebtn.setColorFilter(R.color.white);

        Spinner line = activity.findViewById(R.id.spinner_line); // 호선 선택 Spinner
        Spinner station = activity.findViewById(R.id.spinner_station); // 역 선택 Spinner
        Spinner category = activity.findViewById(R.id.spinner_category); // 카테고리 선택 Spinner

        Button searchBtn = activity.findViewById(R.id.searchBtn); // 조회하기 버튼

        // 데이터 소스 준비 (문자열 배열 사용)
        String[] lineData = {"호선 선택", "1호선", "2호선", "3호선"};

        // 어댑터 생성 및 설정 (호선 Spinner)

        ArrayAdapter<String> lineAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, lineData) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(16); // 원하는 글자 크기로 설정
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextSize(16); // 원하는 글자 크기로 설정
                return textView;
            }
        };

        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        line.setAdapter(lineAdapter);

        // 어댑터 생성 및 설정 (역 Spinner)
        ArrayList<String> stationData = new ArrayList<>(); // 데이터를 담을 ArrayList
        stationData.add("역 선택");
        ArrayAdapter<String> stationAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, stationData){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(16); // 원하는 글자 크기로 설정
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextSize(16); // 원하는 글자 크기로 설정
                return textView;
            }
        };

        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        station.setAdapter(stationAdapter);

        // 어댑터 생성 및 설정 (카테고리 Spinner)
        ArrayList<String> categoryData = new ArrayList<>(); // 데이터를 담을 ArrayList
        categoryData.add("카테고리 선택");
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categoryData){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(16); // 원하는 글자 크기로 설정
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextSize(16); // 원하는 글자 크기로 설정
                return textView;
            }
        };

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
                stationAdapter.add("역 선택");
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

                // 각 역에 존재하는 카테고리 Spinner 설정
                ArrayList<String> categoryList = new ArrayList<>(); // 새로운 데이터를 담을 ArrayList
                for (SubwayEntity subwayEntity : subwayList) {
                    if (subwayEntity.getStation().equals(selectedStation)) {
                        if (!categoryList.toString().contains(subwayEntity.getCategory())) {
                            categoryList.add(subwayEntity.getCategory());
                        }
                    }
                }
                categoryAdapter.clear(); // 기존의 데이터 삭제
                categoryAdapter.add("카테고리 선택");
                categoryAdapter.addAll(categoryList); // 새로운 데이터 추가
                categoryAdapter.notifyDataSetChanged(); // 어댑터 갱신
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

        // 조회하기 버튼 이벤트 처리
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // filter 적용된 결과 출력 (SubwayResultService 클래스 연결)
                if (!selectedLine.equals("호선 선택") &&
                        !selectedStation.equals("역 선택") &&
                        !selectedCategory.equals("카테고리 선택")) {

                    subwayResultService = new SubwayResultService();
                    subwayResultService.resultView(activity, selectedStation, selectedCategory, subwayList);
                } else {
                    // 선택되지 않은 항목이 있을 때 처리
                    Toast.makeText(activity, "항목을 모두 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getAddInfoList(AppCompatActivity activity, List<SubwayEntity> subwayList) {

        AddInfoDbHelper dbHelper = new AddInfoDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "addInfoDB",
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                String line = cursor.getString(cursor.getColumnIndexOrThrow("호선"));
                String station = cursor.getString(cursor.getColumnIndexOrThrow("역명"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("구분"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("상호"));
                String detail = cursor.getString(cursor.getColumnIndexOrThrow("상세"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("시간"));

                SubwayEntity addInfoEntity = new SubwayEntity(line, station, category, name, detail, time);
                subwayList.add(addInfoEntity);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        searchFilter(activity, subwayList);
    }
}
