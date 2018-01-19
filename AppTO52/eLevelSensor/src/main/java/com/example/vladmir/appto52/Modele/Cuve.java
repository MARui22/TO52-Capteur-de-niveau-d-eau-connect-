package com.example.vladmir.appto52.Modele;

/**
 * Created by vladmir on 13/01/18.
 */

public class Cuve {
    long  mId;
    float mTemperature;
    float mProfondeur;
    String mDate;

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
