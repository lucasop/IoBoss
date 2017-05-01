package net.eclissi.lucasop.ioboss.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean switchSync = mPrefs.getBoolean("switch_sync", true);
        Log.i("BlueRemote", "boot_pref: " + switchSync);

        if( switchSync ){
            //Intent mService = new Intent(context, MyServiceBT.class);
            Intent mService = new Intent(context, ARService.class);
            context.startService(mService);
        }
     }
}
