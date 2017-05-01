package net.eclissi.lucasop.ioboss.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import net.eclissi.lucasop.ioboss.utils.Constants;
import net.eclissi.lucasop.ioboss.utils.SendPostRequest;

public class MyReceiver extends BroadcastReceiver {
    private static final String PROVIDER_NAME = "net.eclissi.lucasop.ioboss.providers.PrefProvider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/text");
    private static final String ACTIVITY_NAME = "net.eclissi.lucasop.ioboss.";


    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i("BlueRemote", "MyReciver");


        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        String mBTdb = "";
        String mEntity = "";
        String mApiPass = "";
        String key_col = "PREF_KEY=?";
        String[] key_bt = {"bt_bounded"};
        String[] key_entity = {"entity_id"};
        String[] key_apipass = {"api_pass"};

        intent.getAction();


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




        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address
            Log.i("BlueRemote","aCON -" + deviceName.toString() +"-");
            if (deviceName.equals(mBTdb)){
                Log.i("BlueRemote","CON sleeping " +deviceName.toString());
                String pass = "sleeping";
                new SendPostRequest().execute(pass,mEntity,mApiPass );
            }
        }

        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address
            Log.i("BlueRemote","aDISC -" + deviceName.toString() + "-");
            if (deviceName.equals(mBTdb)){
                Log.i("BlueRemote","DISC standby " +deviceName.toString());
                String pass = "standby";
                new SendPostRequest().execute(pass,mEntity,mApiPass);

            }
        }

        if (Constants.INTENT_ACTION.equals(action)) {
            Bundle extra = intent.getExtras();
            if ( extra != null ) {
                for (String key : extra.keySet()){
                    Log.i("BlueRemote","RECOGNIZE : "+ extra.get(key));
                    String pass = extra.get(key).toString();
                    mEntity = "input_select.luca_detected";
                    new SendPostRequest().execute(pass,mEntity,mApiPass);
                }
            }
            //Log.i("BlueRemote","INTENT -" + intent.getExtras() + "-");

        }



    }
}
