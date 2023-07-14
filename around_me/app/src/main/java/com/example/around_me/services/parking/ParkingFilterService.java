package com.example.around_me.services.parking;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.around_me.R;
import com.example.around_me.entities.ParkingEntity;
import com.example.around_me.utils.parking.db.QuickSearchDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParkingFilterService {
    ParkingCommonService parkingCommonService;
    parkingResultService parkingResultService;
    ParkingQuickSearchService parkingQuickSearchService;
    private String selectedGu, selectedPay, selectedDay, selectedTime, guCode;

    public void searchFilter(AppCompatActivity activity, List<ParkingEntity> parkingList) {
        activity.setContentView(R.layout.parking_infofilter);

        parkingCommonService = new ParkingCommonService();
        parkingCommonService.common(activity);

        ImageButton changebtn = activity.findViewById(R.id.p_searchbtn);
        changebtn.setColorFilter(R.color.white);

        Spinner guName = activity.findViewById(R.id.spinner_gu); // 구 선택 Spinner
        Spinner pay = activity.findViewById(R.id.spinner_pay); // 유/무료 선택 Spinner
        Spinner day = activity.findViewById(R.id.spinner_day); // 요일 선택 Spinner
        Spinner time = activity.findViewById(R.id.spinner_time); // 시간 선택 Spinner

        Button quickSearchBtn = activity.findViewById(R.id.quickSearchBtn); // 간편구매 버튼
        Button findBtn = activity.findViewById(R.id.findBtn); // 조회 버튼

        // 데이터 소스 준비 (문자열 배열 사용)
        String[] guData = {"구 선택", "동구", "서구", "남구", "북구", "중구", "수성구", "달서구", "달성군"};

        // 어댑터 생성 및 설정 (구 Spinner)
        ArrayAdapter<String> guAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, guData){
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
        guAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guName.setAdapter(guAdapter);

        // 어댑터 생성 및 설정 (유/무료 Spinner)
        ArrayList<String> payData = new ArrayList<>(); // 데이터를 담을 ArrayList
        payData.add("요금 선택");
        ArrayAdapter<String> payAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, payData){
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
        payAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pay.setAdapter(payAdapter);

        // 어댑터 생성 및 설정 (요일 Spinner)
        ArrayList<String> dayData = new ArrayList<>(); // 데이터를 담을 ArrayList
        dayData.add("요일 선택");
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, dayData){
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
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(dayAdapter);

        // 어댑터 생성 및 설정 (시간 Spinner)
        ArrayList<String> TimeData = new ArrayList<>(); // 데이터를 담을 ArrayList
        TimeData.add("시간 선택");
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, TimeData){
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
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(timeAdapter);

        // 구 Spinner에서 선택 이벤트 처리
        guName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGu = parent.getItemAtPosition(position).toString();

                if (selectedGu.equals("동구")) {
                    guCode = "27140";
                } else if (selectedGu.equals("서구")) {
                    guCode = "27170";
                } else if (selectedGu.equals("남구")) {
                    guCode = "27200";
                } else if (selectedGu.equals("북구")) {
                    guCode = "27230";
                } else if (selectedGu.equals("중구")) {
                    guCode = "27110";
                } else if (selectedGu.equals("수성구")) {
                    guCode = "27260";
                } else if (selectedGu.equals("달서구")) {
                    guCode = "27290";
                } else if (selectedGu.equals("달성군")) {
                    guCode = "27710";
                }

                // 존재하는 유/무료 Spinner 설정
                ArrayList<String> payList = new ArrayList<>(); // 새로운 데이터를 담을 ArrayList
                Boolean isPay = false;
                Boolean isFree = false;

                for (ParkingEntity parkingEntity : parkingList) {
                    if (parkingEntity.getGuCode().equals(guCode)) {
                        if (!payList.toString().contains(parkingEntity.getPay())) {
                            payList.add(parkingEntity.getPay());
                        } else if (parkingEntity.getPay().equals("무료") && !isFree) {
                            if (payList.toString().contains("유료+무료")) {
                                payList.add(parkingEntity.getPay());
                                isFree = true;
                            } else if (parkingEntity.getPay().contains("무료")) {
                                isFree = true;
                            }
                        } else if (parkingEntity.getPay().equals("유료") && !isPay) {
                            if (payList.toString().contains("유료+무료")) {
                                payList.add(parkingEntity.getPay());
                                isPay = true;
                            } else if (parkingEntity.getPay().contains("유료")) {
                                isPay = true;
                            }
                        }
                    }

                    payAdapter.clear(); // 기존의 데이터 삭제
                    payAdapter.add("요금 선택");
                    payAdapter.addAll(payList); // 새로운 데이터 추가
                    payAdapter.notifyDataSetChanged(); // 어댑터 갱신
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        });

        // 주차요금 Spinner에서 선택 이벤트 처리
        pay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPay = parent.getItemAtPosition(position).toString();

                // 사용 요일 Spinner 설정
                ArrayList<String> dayList = new ArrayList<>(); // 새로운 데이터를 담을 ArrayList

                boolean hasDaily = dayList.contains("매일"); // "매일"이 이미 존재하는지 여부를 저장하는 변수
                boolean IsWeekend = dayList.contains("주말"); // "주말"이 이미 존재하는지 여부를 저장하는 변수

                for (ParkingEntity parkingEntity : parkingList) {
                    if (parkingEntity.getGuCode().equals(guCode) &&
                            parkingEntity.getPay().equals(selectedPay)) {
                        String openDay = parkingEntity.getOpenDay();

                        if (!dayList.toString().contains(openDay) && !openDay.equals("미운영") &&
                                !openDay.equals("일요일") && !openDay.equals("평일+일요일")) {
                            if ((openDay.equals("평일+토요일+일요일") && hasDaily) ||
                                    (openDay.equals("토요일+일요일") && IsWeekend)) {
                                continue; // "매일"이 이미 존재하므로 다음 반복으로 건너뜁니다.
                            }

                            if (openDay.equals("평일+토요일+일요일")) {
                                dayList.add("매일");
                                hasDaily = true; // "매일"이 추가되었음을 표시합니다.
                            } else if (openDay.equals("토요일+일요일")) {
                                dayList.add("주말");
                                IsWeekend = true; // "매일"이 추가되었음을 표시합니다.
                            } else {
                                dayList.add(openDay);
                            }
                        }
                    }
                }
                dayAdapter.clear(); // 기존의 데이터 삭제
                dayAdapter.add("요일 선택");
                dayAdapter.addAll(dayList); // 새로운 데이터 추가
                dayAdapter.notifyDataSetChanged(); // 어댑터 갱신
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        });

        // 요일 Spinner에서 선택 이벤트 처리
        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay = parent.getItemAtPosition(position).toString();

                // 존재하는 시간 Spinner 설정
                ArrayList<String> timeList = new ArrayList<>(); // 새로운 데이터를 담을 ArrayList
                for (ParkingEntity parkingEntity : parkingList) {

                    if (selectedDay.equals("매일")) {
                        selectedDay = "평일+토요일+일요일";
                    } else if (selectedDay.equals("주말")) {
                        selectedDay = "토요일+일요일";
                    }

                    String cleanedStartTime, cleanedEndTime;

                    if (parkingEntity.getGuCode().equals(guCode) &&
                            parkingEntity.getPay().equals(selectedPay) &&
                            parkingEntity.getOpenDay().equals(selectedDay)) {
                        if (!(parkingEntity.getWeekStartTime().isEmpty() &&
                                parkingEntity.getWeekEndTime().isEmpty())) {

                            String startTime = parkingEntity.getWeekStartTime();
                            String endTime = parkingEntity.getWeekEndTime();

                            // 입력된 시작 시간과 마감 시간이 정시(00분)인지 확인
                            boolean isStartTimeOnTheHour = startTime.endsWith(":00");
                            boolean isEndTimeOnTheHour = endTime.endsWith(":00");

                            // 시작 시간과 마감 시간이 정시(00분)인 경우 그대로 사용
                            cleanedStartTime = isStartTimeOnTheHour ?
                                    startTime : Integer.parseInt(startTime.split(":")[0]) + 1 + ":00";
                            cleanedEndTime = isEndTimeOnTheHour ?
                                    endTime : endTime.split(":")[0] + ":00";

                            timeList.addAll(getHourlyIncrements(cleanedStartTime,
                                    cleanedEndTime));
                        }
                        if (!(parkingEntity.getWeekendStartTime().isEmpty() &&
                                parkingEntity.getWeekendEndTime().isEmpty())) {

                            String startTime = parkingEntity.getWeekendStartTime();
                            String endTime = parkingEntity.getWeekendEndTime();

                            // 입력된 시작 시간과 마감 시간이 정시(00분)인지 확인
                            boolean isStartTimeOnTheHour = startTime.endsWith(":00");
                            boolean isEndTimeOnTheHour = endTime.endsWith(":00");

                            // 시작 시간과 마감 시간이 정시(00분)인 경우 그대로 사용
                            cleanedStartTime = isStartTimeOnTheHour ?
                                    startTime : Integer.parseInt(startTime.split(":")[0]) + 1 + ":00";
                            cleanedEndTime = isEndTimeOnTheHour ?
                                    endTime : endTime.split(":")[0] + ":00";

                            timeList.addAll(getHourlyIncrements(cleanedStartTime,
                                    cleanedEndTime));
                        }
                    }
                }
                timeAdapter.clear(); // 기존의 데이터 삭제
                timeAdapter.add("시간 선택");

                sortTimeList(timeList);
                for (String time : timeList) {
                    timeAdapter.add(time); // 새로운 데이터 추가
                }
                timeAdapter.notifyDataSetChanged(); // 어댑터 갱신
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        });

        // 시간 Spinner에서 선택 이벤트 처리
        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        });

        // 간편검색 버튼 이벤트 처리
        quickSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkingQuickSearchService = new ParkingQuickSearchService();

                // 커스텀 다이얼로그 생성
                Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.dialog_p_quick);

                // 다이얼로그 내부의 뷰 요소 가져오기
                Button quickSelectBtn = dialog.findViewById(R.id.quickSelectBtn);
                Button quickRegisterBtn = dialog.findViewById(R.id.quickRegisterBtn);
                Button quickCloseBtn = dialog.findViewById(R.id.quickCloseBtn);

                // 선택 버튼 클릭 이벤트 처리
                quickSelectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QuickSearchDbHelper dbHelper = new QuickSearchDbHelper(activity.getApplicationContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String[] projection = {"별칭", "구이름", "구코드", "요금", "요일", "시간"};
                        Cursor cursor = db.query("quickSearchDB", projection, null, null, null, null, null);

                        if (cursor.getCount() == 0) {
                            Toast.makeText(activity, "등록된 간편검색이 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            parkingQuickSearchService.quickSelect(activity, new ParkingQuickSearchService.QuickSelectListener() {
                                @Override
                                public void onQuickSelect(ArrayList<String> selectList) {
                                    // 선택 완료 후 selectList를 사용하여 원하는 작업 수행
                                    String quickGu = selectList.get(1);
                                    String quickGuCode = selectList.get(2);
                                    String quickPay = selectList.get(3);
                                    String quickDay = selectList.get(4);
                                    String quickTime = selectList.get(5);

                                    Toast.makeText(activity, quickGu + " " +quickPay+ " "+quickDay+ " "+quickTime+"을 불러오겠습니다.", Toast.LENGTH_SHORT).show();
                                    parkingResultService = new parkingResultService();
                                    parkingResultService.resultView(activity, quickGuCode, quickPay,
                                            quickDay, quickTime, parkingList);

                                }
                            });
                        }
                        cursor.close();
                        db.close();
                    }
                });

                // 등록 버튼 클릭 이벤트 처리
                quickRegisterBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!selectedGu.equals("구 선택") &&
                                !selectedPay.equals("주차요금 선택") &&
                                !selectedDay.equals("요일 선택") &&
                                !selectedTime.equals("시간 선택")) {

                            parkingQuickSearchService.quickRegister(activity, selectedGu, guCode, selectedPay,
                                    selectedDay, selectedTime);
                        } else {
                            // 선택되지 않은 항목이 있을 때 처리
                            Toast.makeText(activity, "항목을 모두 선택해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // 닫기 버튼 클릭 이벤트 처리
                quickCloseBtn.setOnClickListener(new View.OnClickListener() {
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

        // 조회하기 버튼 이벤트 처리
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedGu.equals("구 선택") &&
                        !selectedPay.equals("주차요금 선택") &&
                        !selectedDay.equals("요일 선택") &&
                        !selectedTime.equals("시간 선택")) {
                    parkingResultService = new parkingResultService();
                    parkingResultService.resultView(activity, guCode, selectedPay,
                            selectedDay, selectedTime, parkingList);
                } else {
                    // 선택되지 않은 항목이 있을 때 처리
                    Toast.makeText(activity, "항목을 모두 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // timeList를 시간 순서대로 정렬하는 메서드
    public void sortTimeList(List<String> timeList) {
        // 중복 제거를 위해 Set에 추가
        Set<String> uniqueTimes = new HashSet<>(timeList);

        // Set을 리스트로 변환
        List<String> uniqueTimeList = new ArrayList<>(uniqueTimes);

        // 시간 형식을 나타내는 포맷 설정
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        // Date 객체를 기준으로 정렬하는 Comparator 객체 생성
        Comparator<String> timeComparator = new Comparator<String>() {
            @Override
            public int compare(String time1, String time2) {
                try {
                    // 시간 문자열을 Date 객체로 변환
                    Date date1 = dateFormat.parse(time1);
                    Date date2 = dateFormat.parse(time2);

                    // Date 객체를 비교하여 정렬 순서를 결정
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        };

        // uniqueTimeList를 정렬
        Collections.sort(uniqueTimeList, timeComparator);

        // 정렬된 결과를 다시 원래의 리스트에 저장
        timeList.clear();
        timeList.addAll(uniqueTimeList);
    }

    public static List<String> getHourlyIncrements(String startTime, String endTime) {
        List<String> timeList = new ArrayList<>();

        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date startDate = format.parse(startTime);
            Date endDate = format.parse(endTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
                String time = format.format(calendar.getTime());
                timeList.add(time);
                calendar.add(Calendar.HOUR_OF_DAY, 1);

                // 마감 시간이 다음날 새벽인 경우
                if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
                    calendar.add(Calendar.DATE, 1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return timeList;
    }
}
