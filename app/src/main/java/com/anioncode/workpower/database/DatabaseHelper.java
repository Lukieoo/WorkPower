package com.anioncode.workpower.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.anioncode.workpower.Model.ModelEvent;

import java.util.ArrayList;

/**
 * Created by Rodzinka on 2019-01-17.
 */


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "ToDo";
    private static final String COL1 = "ID";
    private static final String COL2 = "Nazwa";
    private static final String COL3 = "Data";
    private static final String COL4 = "Priorytet";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT,"+
                COL3 +" TEXT,"+
                COL4 +" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
      //  db.execSQL("DROP  TABLE IF EXISTS " + TABLE_NAME);
     //   onCreate(db);
    }
    public boolean updateData(int id,String pri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ID",id);
        contentValues.put("Priorytet",pri);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { String.valueOf(id) });
        return true;
    }
    public boolean addData(String nazwa, String data, String Typ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, nazwa);
        contentValues.put(COL3, data);
        contentValues.put(COL4, Typ);


        Log.d(TAG, "addData: Adding " + Typ + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME+"  ORDER BY ID DESC LIMIT 1";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor AllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public ArrayList<ModelEvent> getAllData(){
        ArrayList<ModelEvent> arrayList=new ArrayList<>();
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * from "+TABLE_NAME+" ORDER BY Priorytet DESC",null);
        while(cursor.moveToNext()){

            int id=cursor.getInt(0);
            String name= cursor.getString(1);
            String date=cursor.getString(2);
            String type=cursor.getString(3);

            ModelEvent event=new ModelEvent(id,name,date,type);
            arrayList.add(event);


        }
        return arrayList;
    }
    public void deleteName(int idx){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE id="+idx;
        db.execSQL(query);
    }
}
























