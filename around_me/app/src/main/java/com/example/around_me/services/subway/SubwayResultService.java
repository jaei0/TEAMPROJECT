package com.example.around_me.services.subway;

import android.app.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.around_me.R;
import com.example.around_me.entities.SubwayEntity;
import com.example.around_me.utils.subway.BookmarkDbHelper;

import java.util.ArrayList;
import java.util.List;

public class SubwayResultService {
    SubwayCommonService subwayCommonService;

    public void resultView(AppCompatActivity activity, String station, String category, List<SubwayEntity> subwayList) {

        activity.setContentView(R.layout.subway_inforesult);

        subwayCommonService = new SubwayCommonService();
        subwayCommonService.common(activity);

        ViewPager viewPager = activity.findViewById(R.id.viewPager);
        TextView pageView = activity.findViewById(R.id.pageView);

        List<SubwayEntity> filteredList = filterSubwayList(station, category, subwayList);
        InfoResultAdapter adapter = new InfoResultAdapter(activity, filteredList);
        viewPager.setAdapter(adapter);

        // 첫페이지 쪽수 나타내기
        pageView.setText(1 + "/" + filteredList.size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 페이지 변화가 발생할 때의 쪽수 처리
                pageView.setText((position + 1) + "/" + filteredList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<SubwayEntity> filterSubwayList(String station, String category, List<SubwayEntity> subwayList) {
        List<SubwayEntity> filteredList = new ArrayList<>();
        for (SubwayEntity subwayEntity : subwayList) {
            if (subwayEntity.getStation().equals(station) && subwayEntity.getCategory().equals(category)) {
                filteredList.add(subwayEntity);
            }
        }
        return filteredList;
    }

    private static class InfoResultAdapter extends PagerAdapter {
        private Activity activity;
        private List<SubwayEntity> subwayList;

        public InfoResultAdapter(Activity activity, List<SubwayEntity> subwayList) {
            this.activity = activity;
            this.subwayList = subwayList;
        }

        @Override
        public int getCount() {
            return subwayList.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            View itemView = LayoutInflater.from(activity).inflate(R.layout.subway_item_result, container, false);

            TextView nameTextView = itemView.findViewById(R.id.nameTextView);
            TextView lineTextView = itemView.findViewById(R.id.lineTextView);
            TextView stationTextView = itemView.findViewById(R.id.stationTextView);
            TextView categoryTextView = itemView.findViewById(R.id.categoryTextView);
            TextView timeTextView = itemView.findViewById(R.id.timeTextView);
            TextView detailTextView = itemView.findViewById(R.id.detailTextView);

            SubwayEntity subwayEntity = subwayList.get(position);
            nameTextView.setText(subwayEntity.getName() + "\n");
            lineTextView.setText(subwayEntity.getLine());
            stationTextView.setText(subwayEntity.getStation());
            categoryTextView.setText(subwayEntity.getCategory());
            timeTextView.setText("도보로 약 " + subwayEntity.getTime()+"분 소요");
            detailTextView.setText(subwayEntity.getDetail());

            container.addView(itemView);

            // 하트 버튼 이벤트 처리
            ImageButton bookmarkBtn = itemView.findViewById(R.id.heartBtn);

            BookmarkDbHelper dbHelper = new BookmarkDbHelper(activity.getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String selection = "`상호` = ?";
            String[] selectionArgs = {subwayEntity.getName()};

            Cursor cursor = db.query(
                    "bookmarkDB",
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor.getCount() > 0) {
                bookmarkBtn.setImageResource(R.drawable.common_bookmark_selected);
            }else {
                bookmarkBtn.setImageResource(R.drawable.common_bookmark_unselected);
            }

            bookmarkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cursor.getCount() > 0) {
                        // 즐겨찾기에 이미 존재하는 데이터라면, db테이블에서 삭제
                        bookmarkBtn.setImageResource(R.drawable.common_bookmark_unselected);
                        Toast.makeText(activity, "'" + subwayEntity.getName() + "'의 즐겨찾기를 해제했습니다.", Toast.LENGTH_SHORT).show();
                        db.delete("bookmarkDB", selection, selectionArgs);
                    } else {
                        // 즐겨찾기에 없는 데이터라면, db테이블에 저장
                        bookmarkBtn.setImageResource(R.drawable.common_bookmark_selected);
                        Toast.makeText(activity, "'" + subwayEntity.getName() + "'을(를) 즐겨찾기에 추가했습니다.", Toast.LENGTH_SHORT).show();

                        ContentValues values = new ContentValues();
                        values.put("호선", subwayEntity.getLine());
                        values.put("역명", subwayEntity.getStation());
                        values.put("구분", subwayEntity.getCategory());
                        values.put("상호", subwayEntity.getName());
                        values.put("상세", subwayEntity.getDetail());
                        values.put("시간", subwayEntity.getTime());

                        db.insert("bookmarkDB", null, values);
                    }
                    cursor.close();
                    db.close();
                }
            });
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
