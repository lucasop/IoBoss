package net.eclissi.lucasop.ioboss.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRABHU on 11/12/2015.
 */
public class DatabaseHelpher extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="geo2";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PLACES = "places";
    private static final String CREATE_PLACES_TABLE = "create table "+TABLE_PLACES + "(key_id INTEGER primary key AUTOINCREMENT DEFAULT 1,stato TEXT,zonaID TEXT,name TEXT,address TEXT,coordinate TEXT,tipo TEXT,rag INTEGER)";

    //private static final String STU_TABLE = "create table "+STUDENT_TABLE +"(name TEXT,email TEXT primary key,roll TEXT,address TEXT,branch TEXT)";

    /*
  key_id INTEGER primary key AUTOINCREMENT,
  name TEXT,
  address TEXT,
  coordinate TEXT,
  tipo TEXT
     */
Context context;

    public DatabaseHelpher(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_PLACES_TABLE);

        Log.i("Blue", "create table :" + CREATE_PLACES_TABLE);
    }

/*
    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null || !db.isOpen())
            db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PLACES);
    }
    */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);

        // Create tables again
        onCreate(db);
    }
/* Insert into database*/
    public long insertIntoDB(
            String stato,
            String zonaID,
            String name,
            String address,
            String coordinate,
            String tipo,
            int rag)



    {
        Log.d("insert", "before insert");

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        //values.put("key_id", key_id);
        values.put("stato", stato);
        values.put("zonaID", zonaID);
        values.put("name", name);
        values.put("address", address);
        values.put("coordinate", coordinate);
        values.put("tipo", tipo);
        values.put("rag", rag);


        // 3. insert
        long insertedId= db.insert(TABLE_PLACES, null, values);

        Log.i(TABLE_PLACES, " - db.insert ID= " + insertedId );
        // 4. close
        db.close();
        Toast.makeText(context, "insert value", Toast.LENGTH_LONG);
        Log.i("insert into DB", "After insert");
        return insertedId;
    }
/* Retrive  data from database */
    public List<net.eclissi.lucasop.ioboss.database.DatabaseModel> getDataFromDB(String filtro){
        List<net.eclissi.lucasop.ioboss.database.DatabaseModel> modelList = new ArrayList<net.eclissi.lucasop.ioboss.database.DatabaseModel>();
        String query = "select * from "+TABLE_PLACES +" where stato = '"+ filtro + "'";

        SQLiteDatabase db = this.getWritableDatabase();
Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                net.eclissi.lucasop.ioboss.database.DatabaseModel model = new net.eclissi.lucasop.ioboss.database.DatabaseModel();

                model.setdbID(cursor.getInt(0));
                model.setStato(cursor.getString(1));
                model.setZonaID(cursor.getString(2));
                model.setName(cursor.getString(3));
                model.setAddress(cursor.getString(4));
                model.setCoordinate(cursor.getString(5));
                model.setTipo(cursor.getString(6));
                model.setRag(cursor.getInt(7));

                modelList.add(model);
            }while (cursor.moveToNext());
        }


        Log.d("student data", modelList.toString());


        return modelList;
    }

    public void updateItemDB(
            double id,
            String stato,
            String zonaID,
            String name,
            String address,
            String coordinate,
            String tipo,
            int rag
    ) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stato", stato);
        values.put("zonaID", zonaID);
        values.put("name", name);
        values.put("address", address);
        values.put("coordinate", coordinate);
        values.put("tipo", tipo);
        values.put("rag", rag);

        // update Row
        db.update(TABLE_PLACES,values,"key_id = '"+id+"'",null);
        db.close(); // Closing database connection
        Log.i("Blue", "update cv into sqlite: ");

    }



    /*delete a row from database*/
    public void deleteARow(String key_id){
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(TABLE_PLACES, "key_id" + " = ?", new String[]{key_id});
        Log.i(TABLE_PLACES, " - db.deleteARow KEY_ID= " + key_id );
        db.close();
    }

}
