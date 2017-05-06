package net.eclissi.lucasop.ioboss.services;

import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import net.eclissi.lucasop.ioboss.receiver.MyReceiver;
import net.eclissi.lucasop.ioboss.utils.Constants;

/**
 * lucasop 15/01/17
 */
public class ARService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public GoogleApiClient mApiClient;
    private static PendingIntent pIntent;
    MyReceiver mBluetoothEventReceiver = null;
    private static final String ACTIVITY_NAME = "net.eclissi.lucasop.ioboss";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getApplicationContext().unregisterReceiver(mBluetoothEventReceiver);
        Log.i("BlueRemote", "Filtro deregistrato");
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mApiClient, pIntent);
        mApiClient.disconnect();
        Log.i("BlueRemote", "Google service Disconnect & remove Recognition Update");
     }

    @Override
    public int onStartCommand(Intent i, int flags, int startId){
        mApiClient = new GoogleApiClient.Builder(this)
            .addApi(ActivityRecognition.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();
        mApiClient.connect();

        Log.i("BlueRemote", "Inizializza Database Helper Sync");


        Log.i("BlueRemote", "Servizio avviato");
        mBluetoothEventReceiver = new MyReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(Constants.INTENT_ACTION);

        // aggiungo filtro connettività WIFI | Mobile
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        getApplicationContext().registerReceiver(mBluetoothEventReceiver, filter);
        Log.i("BlueRemote", "Filtro creato");

        return super.onStartCommand(i, flags, startId);
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        Log.e( "ActivityRecogition", "Google Service connection fail " );
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Log.e( "ActivityRecogition", "Google Service suspend " );
    }

    @Override
    public void onConnected(Bundle arg0) {
        Log.e( "ActivityRecogition", "Google Service connect OK " );
        Intent intent = new Intent(this, ActivityRecognizedService.class);

        pIntent = PendingIntent.getService(getApplicationContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //requestActivityUpdates(period, pIntent);

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mApiClient, 3000, pIntent );

    }
}
