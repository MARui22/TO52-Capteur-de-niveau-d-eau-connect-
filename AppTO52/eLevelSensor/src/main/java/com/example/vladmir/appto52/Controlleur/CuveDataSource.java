package com.example.vladmir.appto52.Controlleur;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.vladmir.appto52.Modele.Cuve;

/**
 * Created by vladmir on 13/01/18.
 */

public class CuveDataSource {
    // Champs de la base de données
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COL_1,
            MySQLiteHelper.COL_2,MySQLiteHelper.COL_3,MySQLiteHelper.COL_4 };
    public CuveDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertCuveData(Cuve c) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.COL_2,c.getmTemperature());
        contentValues.put(dbHelper.COL_3,c.getmProfondeur());
        contentValues.put(dbHelper.COL_4,c.getmDate());
        long result = database.insert(dbHelper.TABLE_NAME,null,contentValues);
        System.out.println("Done");

           }

    public Cursor getCuveData()
    {
        Cursor result = database.query(MySQLiteHelper.TABLE_NAME,
                allColumns, null, null,
                null, null, dbHelper.COL_1+" DESC");
        //result.close();
        result.moveToLast();
        return result;
    }

    public void close(SQLiteDatabase db ) {
        db.close();
    }
    }

