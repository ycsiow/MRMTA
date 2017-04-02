package com.example.siow.mrmta;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.io.IOException;
import java.sql.SQLException;




public class Result extends AppCompatActivity{
    TextView msg;
    ListView listView;
    Cursor cursor;
    DBHelper mydb;
    public String searchText;
    public String searchText2;
    private static final String TAG = "MRMTA";
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.result);

            searchText = getIntent().getStringExtra("TO_RESULT");
            //Toast.makeText(this,searchText,Toast.LENGTH_SHORT).show();
            searchText2 = getIntent().getStringExtra("TO_RESULT2");
            //Toast.makeText(this,searchText2,Toast.LENGTH_SHORT).show();
            listView = (ListView)findViewById(R.id.listView);

/*            mydb = new DBHelper(this);
            try {
                mydb.createDatabase();
                Log.e("aaaa","aaa");
            } catch (IOException ioe) {
                Log.e("aaaa","aaa");
                throw new Error("Unable to create database");
            }
            try {
                mydb.openDataBase();
            }catch(SQLiteException sqle){
                throw sqle;
            }*/
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.getFood("");
            //showResult();
    }

    private void showResult(){
        if(searchText2 == null) {
            cursor = mydb.getFood(searchText);
            msg = (TextView)findViewById(R.id.txtNoteMsg);
            Log.v(TAG, "Populating Food Description");
            if (cursor.getCount() > 0) {
                NotesCursorAdapter adapter = new NotesCursorAdapter(this, cursor);
                listView.setAdapter(adapter);
                msg.setText("");
            } else {
                msg.setText("The result could not be found");
            }
        }
        else if(searchText == null){
            cursor = mydb.getFood(searchText2);
            msg = (TextView) findViewById(R.id.txtNoteMsg);
            Log.v(TAG, "Populating Food Description");
            if (cursor.getCount() > 0) {
                NotesCursorAdapter adapter = new NotesCursorAdapter(this, cursor);
                listView.setAdapter(adapter);
                msg.setText("");
            } else {
                msg.setText("The result could not be found");
            }
        }
    }

    public void ShareToFacebook(){

    }
}

