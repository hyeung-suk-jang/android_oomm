package kr.tana.oomm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 DEMO_SQLITE이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 문자열 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        Log.d("HybridApp", "create table usertable");
        db.execSQL( "CREATE TABLE USERTABLE (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id TEXT, create_at TEXT); ");
        db.execSQL( "CREATE TABLE LANGTABLE (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "lang TEXT, create_at TEXT); ");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "CREATE TABLE LANGTABLE (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "lang TEXT, create_at TEXT); ");
    }

    public void insert(String create_at, String item, int price) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL( "INSERT INTO DEMO_SQLITE VALUES(null, " +
                "'" + item + "', " + price + ", '" + create_at + "');");
        db.close();
    }

    public void insertID(String create_at, String id) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("DELETE FROM USERTABLE ;");
        db.execSQL( "INSERT INTO USERTABLE VALUES(null, " +
                "'" + id + "', '" + create_at + "');");
        db.close();
    }

    public void update(String create_at, String id) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("DELETE FROM USERTABLE ;");
        db.execSQL( "INSERT INTO USERTABLE VALUES(null, " +
                "'" + id + "', '" + create_at + "');");
        db.close();
    }

    public void deleteID() {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM USERTABLE ;");
        db.close();
    }

    public String getID(){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Log.d("HybridApp", "select * from usertable 쿼리 수행 전");
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM USERTABLE", null);
        if(cursor.moveToNext()) {
            Log.d("HybridApp", "select * from usertable : "+cursor.getString(1));

            result += cursor.getString(1);
        }
        return result;
    }
    //setLangIfFirst

    public void setLangIfFirst(String create_at, String lang) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM  LANGTABLE", null);
        if(cursor.moveToNext()) {
            Log.d("HybridApp", "select * from usertable : "+cursor.getString(1));
        }else{
            db.execSQL( "INSERT INTO LANGTABLE VALUES(null, " +
                    "'" + lang + "', '" + create_at + "');");
        }

        db.close();
    }

    public void setLang(String create_at, String lang) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("DELETE FROM LANGTABLE ;");
        db.execSQL( "INSERT INTO LANGTABLE VALUES(null, " +
                "'" + lang + "', '" + create_at + "');");
        db.close();
    }

    public String getLang(){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM  LANGTABLE", null);
        if(cursor.moveToNext()) {
            Log.d("HybridApp", "select * from usertable : "+cursor.getString(1));

            result += cursor.getString(1);
        }
        return result;
    }

    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM USERTABLE", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + " : "
                    + cursor.getString(1)
                    + " | "
                    + cursor.getString(2)
                    + "\n";
        }

        return result;
    }

}