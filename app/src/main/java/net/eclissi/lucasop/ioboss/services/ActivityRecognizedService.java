package net.eclissi.lucasop.ioboss.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import net.eclissi.lucasop.ioboss.utils.Util;


import java.util.List;

/**
 * Created by LucaSop on 2/1/16.
 */
public class ActivityRecognizedService extends IntentService {

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {



        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities( result.getProbableActivities() );
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for( DetectedActivity activity : probableActivities ) {
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    String mActivity = "vehicle";
                    Log.e( "ActivityRecogition", mActivity + ": " + activity.getConfidence() +" type: "+ activity.getType() + " old: "+ Util.oldRecognition + " check : " + !Util.oldRecognition.equals(mActivity) );
                    if( (!Util.oldRecognition.equals(mActivity)) && (activity.getConfidence() >= 50)) {
                        Util.mSendNotification(getApplicationContext(),mActivity);
                        Util.mSendBroadcast(getApplicationContext(),mActivity);
                        Util.oldRecognition = mActivity;
                    }
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    String mActivity = "bicycle";
                    Log.e( "ActivityRecogition", mActivity + ": " + activity.getConfidence() +" type: "+ activity.getType() + " old: "+ Util.oldRecognition + " check : " + !Util.oldRecognition.equals(mActivity) );
                    if( (!Util.oldRecognition.equals(mActivity)) && (activity.getConfidence() >= 50)) {
                        Util.mSendNotification(getApplicationContext(),mActivity);
                        Util.mSendBroadcast(getApplicationContext(),mActivity);
                        Util.oldRecognition = mActivity;
                    }
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    String mActivity = "on_foot";
                    Log.e( "ActivityRecogition", mActivity + ": " + activity.getConfidence() +" type: "+ activity.getType() + " old: "+ Util.oldRecognition + " check : " + !Util.oldRecognition.equals(mActivity) );
                    break;
                }
                case DetectedActivity.RUNNING: {
                    String mActivity = "running";
                    Log.e( "ActivityRecogition", mActivity + ": " + activity.getConfidence() +" type: "+ activity.getType() + " old: "+ Util.oldRecognition + " check : " + !Util.oldRecognition.equals(mActivity) );
                    if( (!Util.oldRecognition.equals(mActivity)) && (activity.getConfidence() >= 50)) {
                        Util.mSendNotification(getApplicationContext(),mActivity);
                        Util.mSendBroadcast(getApplicationContext(),mActivity);
                        Util.oldRecognition = mActivity;
                    }
                    break;
                }
                case DetectedActivity.STILL: {
                    String mActivity = "still";
                    Log.e( "ActivityRecogition", mActivity + ": " + activity.getConfidence() +" type: "+ activity.getType() + " old: "+ Util.oldRecognition + " check : " + !Util.oldRecognition.equals(mActivity) );
                    if( (!Util.oldRecognition.equals(mActivity)) && (activity.getConfidence() >= 50)) {
                        Util.mSendNotification(getApplicationContext(),mActivity);
                        Util.mSendBroadcast(getApplicationContext(),mActivity);
                        Util.oldRecognition = mActivity;
                    }
                    break;
                }
                case DetectedActivity.TILTING: {
                    String mActivity = "tilting";

                    Log.e( "ActivityRecogition", mActivity + ": " + activity.getConfidence() +" type: "+ activity.getType() + " old: "+ Util.oldRecognition + " check : " + !Util.oldRecognition.equals(mActivity) );
                    /*
                    if( (!Util.oldRecognition.equals(mActivity)) && (activity.getConfidence() >= 50)) {
                        Util.mSendNotification(getApplicationContext(),mActivity);
                        Util.mSendBroadcast(getApplicationContext(),mActivity);
                        Util.oldRecognition = mActivity;
                    }
                    */
                    break;
                }
                case DetectedActivity.WALKING: {
                    String mActivity = "walking";
                    Log.e( "ActivityRecogition", mActivity + ": " + activity.getConfidence() +" type: "+ activity.getType() + " old: "+ Util.oldRecognition + " check : " + !Util.oldRecognition.equals(mActivity) );
                    if( (!Util.oldRecognition.equals(mActivity)) && (activity.getConfidence() >= 50)) {
                        Util.mSendNotification(getApplicationContext(),mActivity);
                        Util.mSendBroadcast(getApplicationContext(),mActivity);
                        Util.oldRecognition = mActivity;
                    }
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.e( "ActivityRecogition", "Unknown: " + activity.getConfidence() );
                    break;
                }
            }
        }
    }
}

