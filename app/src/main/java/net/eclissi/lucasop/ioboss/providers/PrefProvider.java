package net.eclissi.lucasop.ioboss.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class PrefProvider extends ContentProvider {

    private static final String PROVIDER_NAME = "androidcontentproviderpost2.androidcontentprovider.text";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/text");
    private static final int TXT = 1;
    private static final int TXT_ID = 2;
    private static final UriMatcher uriMatcher = getUriMatcher();
    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "text", TXT);
        uriMatcher.addURI(PROVIDER_NAME, "text/#", TXT_ID);
        return uriMatcher;
    }

    private PrefDataBase prefDataBase = null;

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TXT:
                return "vnd.android.cursor.dir/vnd.com.androidcontentproviderpost2.androidcontentprovider.provider.text";
            case TXT_ID:
                return "vnd.android.cursor.item/vnd.com.androidcontentproviderpost2.androidcontentprovider.provider.text";

        }
        return "";
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        prefDataBase = new PrefDataBase(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String id = null;
        if(uriMatcher.match(uri) == TXT_ID) {
            //Query is for one single pred. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
            Log.i("BlueRemote " , "provider query: " + id );
        }
        return prefDataBase.getPrefs(id, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            long id = prefDataBase.addNewPref(values);
            Uri returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
            return returnUri;
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == TXT_ID) {
            //Delete is for one single pref. Get the ID from the URI.
            id = uri.getPathSegments().get(1);

        }

        return prefDataBase.deletePrefs(id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == TXT_ID) {
            //Update is for one single pref. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

        return prefDataBase.updatePrefs(id, values);
    }
}