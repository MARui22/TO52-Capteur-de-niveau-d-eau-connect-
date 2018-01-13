package com.example.vladmir.appto52.Controlleur;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.vladmir.appto52.Modele.Cuve;
import com.example.vladmir.appto52.R;

public class ArchivesActivity extends AppCompatActivity {
    private MySQLiteHelper db;
    Cuve c = new Cuve();
    ListView archiveList;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archives);
        context= this;
        archiveList= (ListView)findViewById(R.id.bdd_listview);
        db = new MySQLiteHelper(this);
       // viewArchives();
    }
    private void viewArchives()
    {

        Cursor data = db.getClientData(c);

        if(data.getCount()==0)
        {
            showMessage("Error","aucune disponible");
            return;

        }
        ToDoCursorAdapter adapter = new ToDoCursorAdapter(this, data);
        archiveList.setAdapter(adapter);



    }
    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .show();

    }
}
