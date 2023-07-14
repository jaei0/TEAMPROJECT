package com.example.around_me.utils.parking.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class IdentityDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "identity.db";
    private static final int DATABASE_VERSION = 2;

    public IdentityDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery =
                "CREATE TABLE identityDB\n" +
                        "(\n" +
                        "    `아이디`       VARCHAR(100) NOT NULL,\n" +
                        "    `이름`       VARCHAR(100) NOT NULL,\n" +
                        "    `생년월일`        VARCHAR(100) NOT NULL,\n" +
                        "    `주소`        VARCHAR(100) NOT NULL,\n" +
                        "    `상세주소`        VARCHAR(100) NOT NULL,\n" +
                        "    `연락처`      VARCHAR(100) NOT NULL,\n" +
                        "    `차량번호`      VARCHAR(100) NOT NULL \n)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 이전 버전의 데이터베이스를 삭제하고 새로운 버전의 데이터베이스를 생성하는 작업 수행
        db.execSQL("DROP TABLE IF EXISTS " + "`identityDB`");
        onCreate(db);
    }

    public void updateById(String id, String name, String birth, String address, String address2, String telNum, String carNum) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("이름", name);
        values.put("생년월일", birth);
        values.put("주소", address);
        values.put("상세주소", address2);
        values.put("연락처", telNum);
        values.put("차량번호", carNum);
        db.update("identityDB", values, "아이디 = ?",  new String[]{id});
    }


    public void deleteItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("identityDB", "아이디 = ?", new String[]{id});
        db.close();
    }
}
