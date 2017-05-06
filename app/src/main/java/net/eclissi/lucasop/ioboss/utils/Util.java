package net.eclissi.lucasop.ioboss.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import net.eclissi.lucasop.ioboss.MainActivity;
import net.eclissi.lucasop.ioboss.R;

/**
 * Created by lucasoprana on 16/01/2017.
 */

public final class Util {
    public final static int KIBU = 1024;
    public static String  oldRecognition= "unknown";

    private Util(){}

    public static void mSendBroadcast( Context context, String ActivityRecognized , int ActivityConfidenza){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Constants.INTENT_ACTION);
        broadcastIntent.putExtra(Constants.EXTRA_OPTION, ActivityRecognized);
        broadcastIntent.putExtra(Constants.AR_EXTRA_CONFIDENZA, ActivityConfidenza);
        context.sendBroadcast(broadcastIntent);
    }

    public static  void mSendNotification2 ( Context context, String ActivityRecognized ){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentText( ActivityRecognized );
        builder.setSmallIcon( R.mipmap.ic_launcher );
        builder.setContentTitle( "oiBoss");
        NotificationManagerCompat.from(context).notify(0,builder.build());
    }

    public static void mSendNotification(Context context,String ActivityRecognized)
    {
        NotificationManager nm = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,notificationIntent,0);
        //set
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher );
        builder.setContentText(ActivityRecognized);
        builder.setContentTitle("oiBoss");
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        nm.notify((int)System.currentTimeMillis(),notification);
    }
}