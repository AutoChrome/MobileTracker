package com.example.a10108309.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.a10108309.object.Article;
import com.example.a10108309.object.Phone;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PhoneDB";

    private static final String TABLE_PHONE = "phones";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DEVICENAME = "deviceName";
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_CPU = "cpu";
    private static final String COLUMN_BATTERY = "battery";
    private static final String COLUMN_RESOLUTION = "resolution";
    private static final String COLUMN_CHARGERPORT = "chargerPort";
    private static final String COLUMN_MAINCAMERA = "mainCamera";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_DIMENSIONS = "dimensions";
    private static final String COLUMN_SCREENTYPE = "screenType";
    private static final String COLUMN_NFCENABLED = "nfc";
    private static final String COLUMN_INTERNAL = "internal";

    private static final String TABLE_ARTICLE = "articles";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_SOURCE = "source";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_PUBLISHED = "published";

    private static final String SQL_CREATE_PHONE_ENTRIES = "CREATE TABLE " + TABLE_PHONE +
            "(" + COLUMN_ID + "INTEGER PRIMARY KEY, " +
            COLUMN_DEVICENAME + " TEXT, " +
            COLUMN_BRAND + " TEXT, " +
            COLUMN_CPU + " TEXT, " +
            COLUMN_BATTERY + " TEXT, " +
            COLUMN_RESOLUTION + " TEXT, " +
            COLUMN_CHARGERPORT + " TEXT, " +
            COLUMN_MAINCAMERA + " TEXT, " +
            COLUMN_SIZE + " TEXT, " +
            COLUMN_WEIGHT + " TEXT, " +
            COLUMN_DIMENSIONS + " TEXT, " +
            COLUMN_SCREENTYPE + " TEXT, " +
            COLUMN_NFCENABLED + " TEXT, " +
            COLUMN_INTERNAL + " TEXT" +
            ")";
    private static final String SQL_CREATE_ARTICLES_ENTRIES = "CREATE TABLE " + TABLE_ARTICLE +
            "(" + COLUMN_ID + "INTEGER PRIMARY KEY, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_SOURCE + " TEXT, " +
            COLUMN_URL + " TEXT, " +
            COLUMN_IMAGE + " TEXT, " +
            COLUMN_PUBLISHED + " TEXT " +
            ")";

    private Context context;


    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PHONE_ENTRIES);
        db.execSQL(SQL_CREATE_ARTICLES_ENTRIES);
        Log.e("Tables", "Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS phones");
        db.execSQL("DROP TABLE IF EXISTS articles");
        this.onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertPhone(Phone phone){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DEVICENAME, phone.getDeviceName());
        contentValues.put(COLUMN_BRAND, phone.getBrand());
        contentValues.put(COLUMN_CPU, phone.getCpu());
        contentValues.put(COLUMN_BATTERY, phone.getBattery());
        contentValues.put(COLUMN_RESOLUTION, phone.getResolution());
        contentValues.put(COLUMN_CHARGERPORT, phone.getChargerPort());
        contentValues.put(COLUMN_MAINCAMERA, phone.getMainCamera());
        contentValues.put(COLUMN_SIZE, phone.getSize());
        contentValues.put(COLUMN_WEIGHT, phone.getWeight());
        contentValues.put(COLUMN_DIMENSIONS, phone.getDimensions());
        contentValues.put(COLUMN_SCREENTYPE, phone.getScreenType());
        contentValues.put(COLUMN_NFCENABLED, phone.getNfcEnabled());
        contentValues.put(COLUMN_INTERNAL, phone.getInternal());

        long newRowID = db.insert(TABLE_PHONE, null, contentValues);

        if(newRowID > -1){
            Log.e("Was inserted", "True");
            return true;
        }else{
            Log.e("Was inserted", "False");
            return false;
        }
    }

    public ArrayList<Phone> getPhones(){
        ArrayList<Phone> phoneList = new ArrayList<Phone>();
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PHONE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.e("Cursor", cursor.getCount() + "");

        if(cursor.moveToFirst()){
            do{
                Phone phone = new Phone(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13));

                phoneList.add(phone);
            }while(cursor.moveToNext());
        }
        db.close();
        return phoneList;
    }

    public boolean clearFavourites(){

        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DROP TABLE " + TABLE_PHONE);
        db.execSQL("DROP TABLE " + TABLE_ARTICLE);

        this.onCreate(db);

        return true;
    }

    public boolean checkFavorite(String deviceName){
        String selectQuery = "SELECT * FROM " + TABLE_PHONE + " WHERE " + COLUMN_DEVICENAME + " = " + "'" + deviceName + "'";

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount() > 0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    public boolean checkArticleFavorite(String title){
        String selectQuery = "SELECT * FROM " + TABLE_ARTICLE + " WHERE " + COLUMN_TITLE + " = " + '"' + title + '"';

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount() > 0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    public boolean deletePhone(String deviceName){
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_PHONE, COLUMN_DEVICENAME + " = '" + deviceName + "'", null) > 0;
    }

    public boolean insertArticle(Article article){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, article.getTitle());
        contentValues.put(COLUMN_SOURCE, article.getSource());
        contentValues.put(COLUMN_URL, article.getUrl());
        contentValues.put(COLUMN_IMAGE, article.getImageURL());
        contentValues.put(COLUMN_PUBLISHED, article.getPublishedAt());

        long newRowID = db.insert(TABLE_ARTICLE, null, contentValues);

        if(newRowID > -1){
            Log.e("Was inserted", "True");
            return true;
        }else{
            Log.e("Was inserted", "False");
            return false;
        }
    }

    public ArrayList<Article> getArticles(){
        ArrayList<Article> articleList = new ArrayList<Article>();
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_ARTICLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.e("Cursor", cursor.getCount() + "");

        if(cursor.moveToFirst()){
            do{
                Article article = new Article(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));

                articleList.add(article);
            }while(cursor.moveToNext());
        }
        db.close();
        return articleList;
    }

    public boolean deleteArticle(String title){
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_ARTICLE, COLUMN_TITLE + " = '" + title + "'", null) > 0;
    }
}
