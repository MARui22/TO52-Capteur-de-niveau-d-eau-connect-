package com.example.vladmir.appto52.Controlleur;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vladmir.appto52.R;


/**
 * Created by vladmir on 12/01/18.
 */

public class ToDoCursorAdapter extends CursorAdapter {

    private Context con;
    private Cursor curs;
    public ToDoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.con = context;
        this.curs = cursor;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.bdd_items, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView  temp = (TextView) view.findViewById(R.id.temperatureCol_textview);
        TextView profondeur = (TextView) view.findViewById(R.id.profondeurCol_textview);
        TextView date = (TextView) view.findViewById(R.id.dateCol_textview);

        // Extract properties from cursor
        float temperature =cursor.getFloat(cursor.getColumnIndexOrThrow("Temperature")) ;
        float prof =cursor.getFloat(cursor.getColumnIndexOrThrow("Profondeur")) ;
        String ColDate = cursor.getString(cursor.getColumnIndexOrThrow("Date"));

        // Populate fields with extracted properties
        temp.setText(String.valueOf(temperature));
        profondeur.setText(String.valueOf(prof));
        date.setText(ColDate);



    }
}
