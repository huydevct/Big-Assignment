package com.example.sqlite.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.sqlite.model.Account;
import com.example.sqlite.model.Item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "";
    private static final int DB_VERSION = 1;
    private static final String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    public SQLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public SQLiteHelper(@Nullable Context context, String newDB) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_NAME=newDB;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE newItems2(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT, category TEXT, price TEXT, date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

//    ----------------------------------------------------------

    public List<Item> getAll() {
        List<Item> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String order = "date DESC";

        //.query(table, columns (null==all), select, conditions, groupBy, having, order)
        Cursor cursor = sqLiteDatabase.query("newItems2", null, null, null, null, null, order);
        while (cursor != null && cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1),
                    category = cursor.getString(2),
                    price = cursor.getString(3),
                    date = cursor.getString(4);
            list.add(new Item(id, title, category, price, date));
        }
        assert cursor != null;
        cursor.close();
        return list;
    }

    public void addItem(Item i) {
        ContentValues values = new ContentValues();
        values.put("title", i.getTitle());
        values.put("category", i.getCategory());
        values.put("price", i.getPrice());
        values.put("date", i.getDate());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert("newItems2", null, values);
    }

    public void updateItem(Item i) {
        ContentValues values = new ContentValues();
        values.put("title", i.getTitle());
        values.put("category", i.getCategory());
        values.put("price", i.getPrice());
        values.put("date", i.getDate());

        String whereClause = "id=?";
        String[] whereArgs = {Integer.toString(i.getId())};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.update("newItems2", values, whereClause, whereArgs);
    }

    public void deleteItem(int i) {
        String whereClause = "id=?";
        String[] whereArgs = {Integer.toString(i)};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("newItems2", whereClause, whereArgs);
    }

    public List<Item> searchByTitle(String key) {
        List<Item> list = new ArrayList<>();
        String whereClause = "title like?";
        String[] whereArgs = {"%" + key + "%"};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("newItems2", null, whereClause, whereArgs, null,
                null, null);

        while (cursor != null && cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String category = cursor.getString(2);
            String price = cursor.getString(3);
            String date = cursor.getString(4);
            list.add(new Item(id, title, category, price, date));
        }
        assert cursor != null;
        cursor.close();
        return list;
    }

    public List<Item> searchByCategory(String category) {
        List<Item> list = new ArrayList<>();
        String whereClause = "category like?";
        String[] whereArgs = {category};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("newItems2", null, whereClause, whereArgs, null,
                null, null);

        while (cursor != null && cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String price = cursor.getString(3);
            String date = cursor.getString(4);
            list.add(new Item(id, title, category, price, date));
        }
        assert cursor != null;
        cursor.close();
        return list;
    }

    public List<Item> searchByDate(String startDate, String startMonth, String toDate, String toMonth) {
        String from = year + "/" + startMonth + "/" + startDate, to = year + "/" + toMonth + "/" + toDate;

        List<Item> list = new ArrayList<>();
        String whereClause = "date >= ? AND date <= ?";
        String[] whereArgs = {from, to};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("newItems2", null, whereClause, whereArgs, null,
                null, null);

        while (cursor != null && cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String category = cursor.getString(2);
            String price = cursor.getString(3);
            String date = cursor.getString(4);
            list.add(new Item(id, title, category, price, date));
        }
        assert cursor != null;
        cursor.close();
        return list;
    }

    public List<Item> getByDate(String date) {
        List<Item> list = new ArrayList<>();
        String whereClause = "date like ?";
        String[] whereArgs = {date};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("newItems2", null, whereClause, whereArgs, null,
                null, null);

        while (cursor != null && cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String category = cursor.getString(2);
            String price = cursor.getString(3);
            list.add(new Item(id, title, category, price, date));
        }
        assert cursor != null;
        cursor.close();
        return list;
    }

    public static String getDbName(){
        return DB_NAME;
    }
}
