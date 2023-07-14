package com.example.around_me.utils.subway;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AddInfoDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "addinfo.db";
    private static final int DATABASE_VERSION = 5;

    public AddInfoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery =
                "CREATE TABLE addInfoDB\n" +
                        "(\n" +
                        "    `호선`           VARCHAR(100)   NOT NULL,\n" +
                        "    `역명`       VARCHAR(100),\n" +
                        "    `구분`        VARCHAR(100) NOT NULL,\n" +
                        "    `상호`           VARCHAR(100) NOT NULL,\n" +
                        "    `상세`      VARCHAR(100)   NOT NULL,\n" +
                        "    `시간`      VARCHAR(100) NOT NULL)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 이전 버전의 데이터베이스를 삭제하고 새로운 버전의 데이터베이스를 생성하는 작업 수행
        db.execSQL("DROP TABLE IF EXISTS " + "`addInfoDB`");
        onCreate(db);
    }
//    public void deleteItem(String name) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete("addInfoDB", "상호 = ?", new String[]{name});
//        db.close();
//    }
}
