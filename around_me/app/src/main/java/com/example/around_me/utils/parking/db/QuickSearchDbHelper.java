package com.example.around_me.utils.parking.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuickSearchDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quickSearch.db";
    private static final int DATABASE_VERSION = 2;

    public QuickSearchDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery =
                "CREATE TABLE quickSearchDB\n" +
                        "(\n" +
                        "    `별칭`           VARCHAR(100)   NOT NULL,\n" +
                        "    `구이름`       VARCHAR(100),\n" +
                        "    `구코드`        VARCHAR(100) NOT NULL,\n" +
                        "    `요금`           VARCHAR(100) NOT NULL,\n" +
                        "    `요일`      VARCHAR(100)   NOT NULL,\n" +
                        "    `시간`      VARCHAR(100))";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 이전 버전의 데이터베이스를 삭제하고 새로운 버전의 데이터베이스를 생성하는 작업 수행
        db.execSQL("DROP TABLE IF EXISTS " + "`quickSearchDB`");
        onCreate(db);
    }

    public void deleteItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("quickSearchDB", "별칭 = ?", new String[]{name});
        db.close();
    }
}

