package com.example.around_me.utils.parking.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery =
                "CREATE TABLE favoriteDB\n" +
                        "(\n" +
                        "    `이름`       VARCHAR(100),\n" +
                        "    `주소`        VARCHAR(100) NOT NULL,\n" +
                        "    `기관명`        VARCHAR(100) NOT NULL,\n" +
                        "    `전화번호`        VARCHAR(100) NOT NULL,\n" +
                        "    `요금`           VARCHAR(100) NOT NULL,\n" +
                        "    `요일`      VARCHAR(100)   NOT NULL,\n" +
                        "    `평일오픈시간`      VARCHAR(100),\n" +
                        "    `평일마감시간`      VARCHAR(100),\n" +
                        "    `주말오픈시간`      VARCHAR(100),\n" +
                        "    `주말마감시간`      VARCHAR(100),\n" +
                        "    `상세`      VARCHAR(100)   NOT NULL)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 이전 버전의 데이터베이스를 삭제하고 새로운 버전의 데이터베이스를 생성하는 작업 수행
        db.execSQL("DROP TABLE IF EXISTS " + "`favoriteDB`");
        onCreate(db);
    }

    public void deleteItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("favoriteDB", "이름 = ?", new String[]{name});
        db.close();
    }
}
