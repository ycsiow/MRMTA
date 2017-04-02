package com.example.siow.mrmta;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper{
    private static String DB_PATH = "/data/data/com.example.siow.mrmta/databases";
    public static String DATABASE_FILE = "FoodTable.sql";
    private static String DB_NAME = "FoodTable";
    private SQLiteDatabase myDataBase;
    private static Context myContext;

    public DBHelper(Context context){
        super(context, DB_NAME, null, 2);
        this.myContext = context;
    }

    public static void createDB(SQLiteDatabase db) {
        InputStream assetsDB = null;

        try {
            assetsDB = myContext.getAssets().open(DATABASE_FILE);
            BufferedReader e = new BufferedReader(new InputStreamReader(assetsDB));
            String s = null;

            while((s = e.readLine()) != null) {
                db.execSQL(s);
            }

            Log.i("DBHelper", "Database created");
        } catch (IOException var5) {
            Log.e("DBHelper", "Could not create new database...");
            var5.printStackTrace();
        }

    }

/*    public void createDatabase() throws IOException{
        boolean dbExist = checkDataBase();
        if(dbExist){
        }
        else{
            this.getReadableDatabase();
            try{
                copyDataBase();
            }catch (IOException e){
                throw new Error ("Error occur while copying database.");
            }
        }
    }*/

/*    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true:false;
    }

    private void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException{
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("alex","onCreate");
        myDataBase = db;
        createDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("alex","onCreate");
        createDB(db);
    }

    public Cursor getFood(String Name){
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor c = db.rawQuery("SELECT Name, Type, Course FROM Food WHERE Name = "+ "'"+ Name + "'", null);
        Cursor c = db.rawQuery("SELECT * FROM Food", null);
        return c;
    }
}