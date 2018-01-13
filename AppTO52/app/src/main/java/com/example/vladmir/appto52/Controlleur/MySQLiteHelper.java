package com.example.vladmir.appto52.Controlleur;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vladmir.appto52.Modele.Cuve;


/**
 * Created by vladmir on 04/11/17.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {



    Cuve cuve = new Cuve();
    private static final String DATABASE_NAME = "TO52.db";
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }


    // Creation de toutes les tables de la basse de donnée
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(cuve.CUVE_CREATE_TABLE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + cuve.TABLE_NAME );
        onCreate(db);
    }

    //INSERTION

    public void insertCuveData(Cuve c)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(c.COL_2,c.getmTemperature());
        contentValues.put(c.COL_3,c.getmProfondeur());
        contentValues.put(c.COL_4,c.getmDate());
        long result = db.insert(c.TABLE_NAME,null,contentValues);

    }




    public Cursor getClientData(Cuve c)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select C.ID as _id, C."+c.COL_2+", C."+ c.COL_3+
                ", C."+ c.COL_4+ " from "
                +c.TABLE_NAME+ " C ",null);
        return result;
    }

}
