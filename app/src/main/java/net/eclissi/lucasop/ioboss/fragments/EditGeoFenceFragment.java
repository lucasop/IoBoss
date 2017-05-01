package net.eclissi.lucasop.ioboss.fragments;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.View;

import net.eclissi.lucasop.ioboss.R;

/**
 * Created by lucasoprana on 03/02/2017.
 */

public class EditGeoFenceFragment extends PreferenceFragment {
    SharedPreferences mPrefsF;



    public EditGeoFenceFragment(){

    }

    @Override
    public void onStart(){
        super.onStart();
        mPrefsF = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.edit_dialog_geofence);
    }

    @Override
    public void onViewCreated(View view, Bundle savedIstanceState){
        super.onViewCreated(view, savedIstanceState);

        //****************
        final SwitchPreference serviceStatus = (SwitchPreference) findPreference("lg_sync");
        serviceStatus.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object isServiceEnabled) {
                boolean isServiceOn= ((Boolean) isServiceEnabled).booleanValue();
                SharedPreferences.Editor e = mPrefsF.edit();
                e.putBoolean("lg_sync", isServiceOn);
                e.commit();
                if(isServiceOn){
                    serviceStatus.setSummary("Location Geofence Enabled");
                    //getActivity().startService(new Intent(getActivity(),MyServiceBT.class));
                    //getActivity().startService(new Intent(getActivity(),ARService.class));
                    Log.i("BlueRemote", "geoFence Enable switch");

                } else{
                    serviceStatus.setSummary("Location Geofence Enabled");
                    //getActivity().stopService(new Intent(getActivity(),MyServiceBT.class));
                    //getActivity().stopService(new Intent(getActivity(),ARService.class));
                    Log.i("BlueRemote", "geoFence Disable switch");
                }
                return true;
            }
        });
        //******************

        final EditTextPreference lg_name = (EditTextPreference) findPreference("lg_name");
        lg_name.setSummary(lg_name.getText());
        lg_name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String inputHost = (String)newValue;
                lg_name.setSummary(inputHost);
                return true;
            }
        });

        final EditTextPreference lg_indirizzo = (EditTextPreference) findPreference("lg_indirizzo");
        final ContentValues contentValuesP = new ContentValues();
        lg_indirizzo.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String inputPass = (String)newValue;
                lg_indirizzo.setSummary("***");
                //contentValuesP.put("PREF_KEY", "api_pass");
                //contentValuesP.put("TITLE", newValue.toString());
                //contentValuesP.put("SUMMARY", newValue.toString());
                //Uri uriI =  getContext().getContentResolver().insert(CONTENT_URI, contentValuesP);
                return true;
            }
        });

        final EditTextPreference lg_latlong = (EditTextPreference) findPreference("lg_latlong");
        final ContentValues contentValuesE = new ContentValues();
        lg_latlong.setSummary(lg_latlong.getText());
        lg_latlong.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue2) {
                String inputEntityID = (String)newValue2;
                lg_latlong.setSummary(inputEntityID);
                //contentValuesE.put("PREF_KEY", "entity_id");
                //contentValuesE.put("TITLE", newValue2.toString());
                //contentValuesE.put("SUMMARY", newValue2.toString());
                //Uri uriI =  getContext().getContentResolver().insert(CONTENT_URI, contentValuesE);
                return true;
            }
        });

        final ListPreference lg_type = (ListPreference) findPreference("lg_type");
        final ContentValues  contentValues = new ContentValues();
        lg_type.setSummary(lg_type.getValue().toString());
        lg_type.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue3) {
                lg_type.setSummary(newValue3.toString());
                //contentValues.put("PREF_KEY", "bt_bounded");
                //contentValues.put("TITLE", newValue3.toString());
                //contentValues.put("SUMMARY", newValue3.toString());
                //Uri uriI =  getContext().getContentResolver().insert(CONTENT_URI, contentValues);//Log.i("BlueRemote " , "provider delete nr: " + uriD );
                return true;

            }

        }); // fine listener

    }
    }
