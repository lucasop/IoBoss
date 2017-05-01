package net.eclissi.lucasop.ioboss.services;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import net.eclissi.lucasop.ioboss.receiver.MyReceiver;

public class MyServiceBT extends Service {
    MyReceiver mBluetoothEventReceiver = null;

    @Override
    public int onStartCommand(Intent i, int flags, int startId){
        Log.i("BlueRemote", "Servizio avviato");
           mBluetoothEventReceiver = new MyReceiver();

           IntentFilter filter = new IntentFilter();
           filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
           filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
           getApplicationContext().registerReceiver(mBluetoothEventReceiver, filter);
           Log.i("BlueRemote", "Filtro creato");

        return super.onStartCommand(i, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getApplicationContext().unregisterReceiver(mBluetoothEventReceiver);
        Log.i("BlueRemote", "Filtro deregistrato");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
