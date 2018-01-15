package com.example.vladmir.appto52.Controlleur;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.vladmir.appto52.Modele.Cuve;

import java.io.IOException;


/**
 * Created by vladmir on 04/11/17.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String COL_1 = "_id";
    public static final String COL_2 = "Temperature";
    public static final String COL_3 = "Profondeur";
    public static final String COL_4 = "Date";

    public static final String TABLE_NAME = "Cuve";
    public static final String CUVE_CREATE_TABLE = "CREATE TABLE " +TABLE_NAME + " ("
            + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " REAL, "
            + COL_3 + " REAL, " + COL_4 + " TEXT NOT NULL);";
    public static final String CUVE_TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    private static final String DATABASE_NAME = "TO52.db";
    private static final int DATABASE_VERSION = 1;

        public MySQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            System.out.println("Base created");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CUVE_CREATE_TABLE);

        }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CUVE_TABLE_DROP);
        onCreate(db);
    }


    }



   /* Cuve cuve = new Cuve();
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






    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

}*/
