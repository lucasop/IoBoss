package net.eclissi.lucasop.ioboss.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Belal on 1/27/2017.
 */
public class DatabaseHelperSync extends SQLiteOpenHelper {

    //Constants for Database name, table name, and column names
    public static final String DB_NAME = "ActivityDB";
    public static final String TABLE_NAME = "activity3";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FONTE = "fonte";
    public static final String COLUMN_DATATIME = "datatime";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_ENTITY = "entity";
    public static final String COLUMN_OPTION = "option";
    public static final String COLUMN_CONFIDENZA = "confidenza";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_DATATIME2 = "datatime2";



    //database version
    private static final int DB_VERSION = 3;

    //Constructor
    public DatabaseHelperSync(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FONTE + " VARCHAR, " +
                COLUMN_DATATIME + " INT, " +
                COLUMN_TYPE + " VARCHAR, " +
                COLUMN_ENTITY + " VARCHAR, " +
                COLUMN_OPTION + " VARCHAR, " +
                COLUMN_CONFIDENZA + " TINYINT, " +
                COLUMN_STATUS + " TINYINT " +
        ");";

        db.execSQL(sql);
    }

    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Persons";
        db.execSQL(sql);
        onCreate(db);
    }

    /*
    * This method is taking two arguments
    * first one is the name that is to be saved
    * second one is the status
    * 0 means the name is synced with the server
    * 1 means the name is not synced with the server
    * */
    public long addActivity(String fonte, long datatime, String type, String entity, String option, int confidenza,  int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_FONTE, fonte);
        contentValues.put(COLUMN_DATATIME, datatime);
        contentValues.put(COLUMN_TYPE, type);
        contentValues.put(COLUMN_ENTITY, entity);
        contentValues.put(COLUMN_OPTION, option);
        contentValues.put(COLUMN_CONFIDENZA, confidenza);
        contentValues.put(COLUMN_STATUS, status);

        long insertedId=db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return insertedId;
    }

    /*
    * This method taking two arguments
    * first one is the id of the name for which
    * we have to update the sync status
    * and the second one is the status that will be changed
    * */
    public boolean updateActivityStatus(long id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }

    /*
    * this method will give us all the activity stored in sqlite
    * */
    public Cursor getActivity() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /*
    * this method is for getting all the unsynced name
    * so that we can sync it with database
    * */
    public Cursor getUnsyncedActivity() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /*
 * this method is for getting all the unsynced activity
 * so that we can sync it with database
 * */
    public int getUnsyncedActivityNr() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c.getCount();
    }

 /*
 * this method is for getting all activity time diff n+1
 *
 * */
    public Cursor getDiffRowToDay() {
        SQLiteDatabase db = this.getReadableDatabase();
        //String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0;";

        String sql2 = "SELECT q.option, q.datatime, "+
                "coalesce((select r.datatime  from " + TABLE_NAME + " as r " +
                        "where r.datatime < q.datatime " +
                        " order by r.datatime DESC limit 1), q.datatime ) as datatime2 " +
        "FROM " + TABLE_NAME +" as q WHERE q." + COLUMN_OPTION + " NOT NULL " +
                " and date(q.datatime / 1000, \"unixepoch\" ) = date(\"now\") and fonte = \"AR\" " +
        "ORDER BY q.datatime ASC;";
        Log.d("Blue", "SQL: " + sql2 );
        Cursor c = db.rawQuery(sql2, null);
        return c;
    }

    public Cursor getActivity2() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

}
