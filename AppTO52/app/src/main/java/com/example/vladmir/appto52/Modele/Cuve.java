package com.example.vladmir.appto52.Modele;

/**
 * Created by vladmir on 13/01/18.
 */

public class Cuve {
    long  mId;
    float mTemperature;
    float mProfondeur;
    String mDate;
    public static final String TABLE_NAME = "Cuve";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Temperature";
    public static final String COL_3 = "Profondeur";
    public static final String COL_4 = "Date";


    public static final String CUVE_CREATE_TABLE = "CREATE TABLE " + "("
            + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " REAL NOT NULL, "
            + COL_3 + " REAL NOT NULL, " + COL_4 + " TEXT NOT NULL);";
    public Cuve() {
    }

    public Cuve(float mTemperature, float mProfondeur, String mDate) {
        this.mTemperature = mTemperature;
        this.mProfondeur = mProfondeur;
        this.mDate = mDate;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public float getmTemperature() {
        return mTemperature;
    }

    public void setmTemperature(float mTemperature) {
        this.mTemperature = mTemperature;
    }

    public float getmProfondeur() {
        return mProfondeur;
    }

    public void setmProfondeur(float mProfondeur) {
        this.mProfondeur = mProfondeur;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
