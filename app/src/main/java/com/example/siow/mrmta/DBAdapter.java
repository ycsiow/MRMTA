package com.example.siow.mrmta;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Alex on 03/04/2017.
 */

public class DBAdapter {
    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context context) {
        this.context = context;
        if(this.dbHelper == null) {
            this.dbHelper = new DBHelper(context);
        }

        if(db == null){
            db = this.dbHelper.getReadableDatabase();
        }
    }

    public void getFood(String name) {
        Cursor c = db.rawQuery("SELECT * FROM Food", null);
        if(c.moveToFirst()) {
            do {
                Log.d("Food name", c.getString(c.getColumnIndex("Name")));
            } while(c.moveToNext());
        }
        c.close();
    }
}
