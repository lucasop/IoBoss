package net.eclissi.lucasop.ioboss.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.sql.SQLException;

public class PrefDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PrefsDatabase2.db";
    private static final String TABLE_NAME = "prefstore";
    private static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME +
            " (_id INTEGER PRIMARY KEY, PREF_KEY TEXT UNIQUE ON CONFLICT REPLACE, TITLE TEXT , SUMMARY TEXT )";

    private static final String SQL_DROP = "DROP TABLE IS EXISTS " + TABLE_NAME ;

    PrefDataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        onCreate(db);
    }

    public Cursor getPrefs(String id, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqliteQueryBuilder = new SQLiteQueryBuilder();
        sqliteQueryBuilder.setTables(TABLE_NAME);

        if(id != null) {
            sqliteQueryBuilder.appendWhere("_id" + " = " + id);
        }

        if(sortOrder == null || sortOrder == "") {
            sortOrder = "PREF_KEY";
        }
        Cursor cursor = sqliteQueryBuilder.query(getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        return cursor;
    }

    public long addNewPref(ContentValues values) throws SQLException {
        long id = getWritableDatabase().insert(TABLE_NAME, "", values);
        if(id <=0 ) {
            throw new SQLException("Failed to add an pref");
        }

        return id;
    }

    public int deletePrefs(String id) {
        if(id == null) {
            return getWritableDatabase().delete(TABLE_NAME, null , null);
        } else {

            return getWritableDatabase().delete(TABLE_NAME, "_id=?", new String[]{id});
        }
    }

    public int updatePrefs(String id, ContentValues values) {
        if(id == null) {
            return getWritableDatabase().update(TABLE_NAME, values, null, null);
        } else {
            return getWritableDatabase().update(TABLE_NAME, values, "_id=?", new String[]{id});
        }
    }
}