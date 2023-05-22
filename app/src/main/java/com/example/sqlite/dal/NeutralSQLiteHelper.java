package com.example.sqlite.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.sqlite.model.Account;
import com.example.sqlite.dal.SQLiteHelper;

public class NeutralSQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Accounts";
    private static final int DB_VERSION = 1;

    public NeutralSQLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTables(sqLiteDatabase);
    }

    private void createTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE accounts(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT, password TEXT, fullName TEXT, email TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE key_accounts(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT, password TEXT, fullName TEXT, email TEXT)");

        initKeyAcc(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public boolean register(Account account) {
        if (checkDuplicateAccount(account.getUsername())) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put("username", account.getUsername().trim());
        values.put("password", account.getPassword().trim());
        values.put("fullName", account.getFullName().trim());
        values.put("email", account.getEmail().trim());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long res = sqLiteDatabase.insert("accounts", null, values);
        return res != -1;
    }

    public boolean login(String username, String password) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from accounts where username = ? and password = ?", new String[]{username, password});
        cursor.moveToFirst();
        return cursor.getCount() != 0;
    }

    private boolean checkDuplicateAccount(String username) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from accounts where username = ?", new String[]{username});
        assert cursor != null;
        return cursor.getCount() > 0;
    }

    public Account getAccount() {
        String username = SQLiteHelper.getDbName();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from accounts where username = ? ", new String[]{username});
        cursor.moveToFirst();
        return new Account(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
    }

    public void setPassword(String newPassIn) {
        ContentValues values = new ContentValues();
        values.put("password", newPassIn);

        String username = SQLiteHelper.getDbName();
        String whereClause = "username=?";
        String[] whereArgs = {username};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.update("accounts", values, whereClause, whereArgs);
    }

//    ----------------------------------------------------------

    public void setKeyAccount(String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = "id=?";
        String[] whereArgs = {"1"};
        sqLiteDatabase.update("key_accounts", values, whereClause, whereArgs);
    }

    public String getKeyAccount() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("key_accounts", null, null, null, null, null, null);
        cursor.moveToNext();
        String username = cursor.getString(1),
                password = cursor.getString(2);
        return !username.equals("default") && !password.equals("default") ? username : "";
    }

    private void initKeyAcc(SQLiteDatabase sqLiteDatabase) {
        ContentValues values = new ContentValues();
        values.put("username", "default");
        values.put("password", "default");
        sqLiteDatabase.insert("key_accounts", null, values);
    }
}
