package net.eclissi.lucasop.ioboss.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.VolleyError;

import net.eclissi.lucasop.ioboss.database.DatabaseHelperSync;
import net.eclissi.lucasop.ioboss.utils.Constants;
import net.eclissi.lucasop.ioboss.utils.IResult;
import net.eclissi.lucasop.ioboss.utils.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

//import net.eclissi.lucasop.ioboss.utils.VolleyService;

public class MyReceiver extends BroadcastReceiver {
    private static final String PROVIDER_NAME = "net.eclissi.lucasop.ioboss.providers.PrefProvider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/text");
    private static final String ACTIVITY_NAME = "net.eclissi.lucasop.ioboss.";

    IResult mResultCallback = null;
    VolleyService mVolleyService;
    private long idAdd;

    //database helper object
    private DatabaseHelperSync dbsync;


    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i("BlueRemote", "MyReceiver");

        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,context);
        dbsync = new DatabaseHelperSync(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();



        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        String mBTdb = "";
        String mEntity = "";
        String mEntityDetect = "";
        String mApiPass = "";
        String key_col = "PREF_KEY=?";
        String[] key_bt = {"bt_bounded"};
        String[] key_entity = {"entity_id"};
        String[] key_entity_detect = {"entity_detect_id"}; //c5
        String[] key_apipass = {"api_pass"};




        intent.getAction();

/*
recupero dal content provider del device BT
 */
        Cursor c2 = context.getContentResolver().query(CONTENT_URI , new String[]{"PREF_KEY, TITLE"}, key_col, key_bt, null );
        Log.i("BlueRemote", "MyReciver cursor c2 " + c2.getCount() + "/" + c2.getColumnCount());
        if (c2 == null){
            Log.i("BlueRemote", "MyReciver cursor c2 null");
        }else if (c2.getCount() < 1){
            Log.i("BlueRemote", "MyReciver cursor c2 0");
        }else{
            while (c2.moveToNext()){
                Log.i("BlueRemote", "MyReciver cursor #1-" + c2.getString(0)+ "-#2-" + c2.getString(1));
                mBTdb = c2.getString(1);
            }
        }
        c2.close();
        Log.i("BlueRemote","BR pref mBTdb #-" + mBTdb + "-#");

/*
recupero dal content provider della EntityID
 */
        Cursor c3 = context.getContentResolver().query(CONTENT_URI, new String[]{"PREF_KEY, TITLE"}, "PREF_KEY=?", key_entity, null );
        Log.i("BlueRemote", "MyReciver cursor c3 " + c3.getCount() + "/" + c3.getColumnCount());
        if (c3 == null){
            Log.i("BlueRemote", "MyReciver cursor c3 null");
        }else if (c3.getCount() < 1){
            Log.i("BlueRemote", "MyReciver cursor c3 0");
        }else{
            while (c3.moveToNext()){
                Log.i("BlueRemote", "MyReciver cursor #1-" + c3.getString(0)+ "-#2-" + c3.getString(1));
                mEntity = c3.getString(1);
            }
        }
        c3.close();
        Log.i("BlueRemote","BR pref mEntity #-" + mEntity + "-#");

/*
recupero dal content provider della password
 */
        Cursor c4 = context.getContentResolver().query(CONTENT_URI, new String[]{"PREF_KEY, TITLE"}, "PREF_KEY=?", key_apipass, null );
        Log.i("BlueRemote", "MyReciver cursor c4 " + c4.getCount() + "/" + c4.getColumnCount());
        if (c4 == null){
            Log.i("BlueRemote", "MyReciver cursor c4 null");
        }else if (c4.getCount() < 1){
            Log.i("BlueRemote", "MyReciver cursor c4 0");
        }else{
            while (c4.moveToNext()){
                Log.i("BlueRemote", "MyReciver cursor  c4 | " + c4.getString(0)+ " | " + c4.getString(1));
                mApiPass = c4.getString(1);
            }
        }
        c4.close();
        Log.i("BlueRemote","BR pref mApiPass #-" + mApiPass + "-#");

   /*
recupero dal content provider della EntityID di detaction
 */
        Cursor c5 = context.getContentResolver().query(CONTENT_URI, new String[]{"PREF_KEY, TITLE"}, "PREF_KEY=?", key_entity_detect, null );
        Log.i("BlueRemote", "MyReciver cursor c5 " + c5.getCount() + "/" + c5.getColumnCount());
        if (c5 == null){
            Log.i("BlueRemote", "MyReciver cursor c5 null");
        }else if (c5.getCount() < 1){
            Log.i("BlueRemote", "MyReciver cursor c5 0");
        }else{
            while (c5.moveToNext()){
                Log.i("BlueRemote", "MyReciver cursor  c5 | " + c5.getString(0)+ " | " + c5.getString(1));
                mEntityDetect = c5.getString(1);
            }
        }
        c5.close();
        Log.i("BlueRemote","BR pref mApiPass #-" + mEntityDetect + "-#");






        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address
            //Log.i("BlueRemote","aCON -" + deviceName.toString() +"-");
            if (deviceName.equals(mBTdb)){
                //Log.i("BlueRemote","CON sleeping " +deviceName.toString());
                String pass = "sleeping";
                //new SendPostRequest().execute(pass,mEntity,mApiPass );

                JSONObject sendObj = null;
                try {
                    sendObj = new JSONObject();
                    sendObj.put("entity_id", mEntity);
                    sendObj.put("option", pass);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // save activity to LocalStorage
                idAdd = dbsync.addActivity("BT", System.currentTimeMillis(), "", mEntity, pass, 0, 0 );
                Log.d(TAG ,"Volley BT ID: " + idAdd + " - " + pass );

                // trasmetto solamente se la coda di sincronizzazione è vuota
                //if ( dbsync.getUnsyncedActivityNr() == 0 ){
                //    mVolleyService.postDataVolley("POSTCALL", "https://eclissi.duckdns.org/api/services/input_select/select_option?api_password="+ mApiPass, sendObj, idAdd);
                //}
            }
        }

        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address
            //Log.i("BlueRemote","aDISC -" + deviceName.toString() + "-");
            if (deviceName.equals(mBTdb)){
                //Log.i("BlueRemote","DISC standby " +deviceName.toString());
                String pass = "standby";
                //new SendPostRequest().execute(pass,mEntity,mApiPass);

                JSONObject sendObj = null;
                try {
                    sendObj = new JSONObject();
                    sendObj.put("entity_id", mEntity);
                    sendObj.put("option", pass);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                idAdd = dbsync.addActivity("BT", System.currentTimeMillis(), "", mEntity, pass, 0, 0 );
                Log.d(TAG ,"Volley BT ID: " + idAdd + " - " + pass );

                // trasmetto solamente se la coda di sincronizzazione è vuota
                //if ( dbsync.getUnsyncedActivityNr() == 0 ){
                //    mVolleyService.postDataVolley("POSTCALL", "https://eclissi.duckdns.org/api/services/input_select/select_option?api_password="+ mApiPass, sendObj, idAdd);
                //}

            }
        }

        if (Constants.INTENT_ACTION.equals(action)) {
            Bundle extra = intent.getExtras();
            if ( extra != null ) {

                    //Log.d(TAG ,"Volley RECOGNIZE : "+ extra.get(key));
                    String pass = extra.get(Constants.EXTRA_OPTION).toString();
                    int mConfidenza = extra.getInt(Constants.AR_EXTRA_CONFIDENZA);
                    //mEntity = "input_select.luca_detected";

                    JSONObject sendObj = null;
                    try {
                        sendObj = new JSONObject();
                        sendObj.put("entity_id", mEntityDetect);
                        sendObj.put("option", pass);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    idAdd = dbsync.addActivity("AR", System.currentTimeMillis(), "", mEntity, pass, mConfidenza, 0 );
                    Log.d(TAG ,"Volley RECOGNIZE ID: " + idAdd + " - " + pass );

                    // trasmetto solamente se la coda di sincronizzazione è vuota
                    //if ( dbsync.getUnsyncedActivityNr() == 0 ){
                    //    mVolleyService.postDataVolley("POSTCALL", "https://eclissi.duckdns.org/api/services/input_select/select_option?api_password="+ mApiPass, sendObj, idAdd);
                    //}
                    //new SendPostRequest().execute(pass,mEntity,mApiPass);

            }
        } // fine INTENT_ACTION

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = dbsync.getUnsyncedActivity();
                if (cursor.moveToFirst()) {
                    do {
                        //calling unsynced activity to sync remote Server

                        long vID = cursor.getLong(cursor.getColumnIndex(DatabaseHelperSync.COLUMN_ID));
                        String vEntity = cursor.getString(cursor.getColumnIndex(DatabaseHelperSync.COLUMN_ENTITY));
                        String vOption = cursor.getString(cursor.getColumnIndex(DatabaseHelperSync.COLUMN_OPTION));
                        Log.d(TAG, "Volley CONNECTIVITY_ACTION DBSYNC ID: " + vID + " entity: " + vEntity + " entity: " + vOption);

                        JSONObject sendObj = null;
                        try {
                            sendObj = new JSONObject();
                            sendObj.put("entity_id", vEntity);
                            sendObj.put("option", vOption);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mVolleyService.postDataVolley("POSTCALL", "https://eclissi.duckdns.org/api/services/input_select/select_option?api_password="+ mApiPass, sendObj, vID);

                    } while (cursor.moveToNext());
                }
            }
        }
    }


    //callback Volley
    void initVolleyCallback(){
        mResultCallback = new IResult() {

            // se la richiesta va a buon fine, scrivo nel DB 1 dato sincronizzato
            @Override
            public void notifySuccess(String requestType,JSONArray response, long idDB) {
                Log.d(TAG, "Volley CALLBACK JSON OK ID: " +idDB + " - " + response);
                Log.d(TAG, "Volley CALLBACK JSON pre  queue nr: " + dbsync.getUnsyncedActivityNr() );
                dbsync.updateActivityStatus( idDB,1);
                Log.d(TAG, "Volley CALLBACK JSON post queue nr: " + dbsync.getUnsyncedActivityNr() );
            }

            // se la richiesta NON va a buon fine, non effettuo nessuna operazione
            @Override
            public void notifyError(String requestType,VolleyError error, long idDB) {
                Log.d(TAG, "Volley CALLBACK JSON KO ID: " +idDB + " coda: " + dbsync.getUnsyncedActivityNr()  );
            }
        };
    }
}
