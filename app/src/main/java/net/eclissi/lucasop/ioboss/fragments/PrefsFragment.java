package net.eclissi.lucasop.ioboss.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import net.eclissi.lucasop.ioboss.services.ARService;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by lucasoprana on 21/01/2017.
 */


public class PrefsFragment extends PreferenceFragment {

    private static final String PROVIDER_NAME = "net.eclissi.lucasop.ioboss.providers.PrefProvider";

    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/text");

    SharedPreferences mPrefsF;
    ArrayList<CharSequence> mEntries;
    ArrayList<CharSequence> mEntriesValues;

    public PrefsFragment(){

    }




    @Override
    public void onStart(){
        super.onStart();
        mPrefsF = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //Log.i("BlueRemote", "activity MyActivity" + getActivity() );
        //SharedPreferences mPrefs = context.getSharedPreferences("mSharedPrefs", context.MODE_PRIVATE);

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);


        //final Preference servicesyncUpdate = (Preference) findPreference("switch_sync");


    }
    @Override
    public void onViewCreated(View view, Bundle savedIstanceState){
        super.onViewCreated(view, savedIstanceState);

        //****************
        final SwitchPreference serviceStatus = (SwitchPreference) findPreference("switch_sync");
        serviceStatus.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object isServiceEnabled) {
                boolean isServiceOn= ((Boolean) isServiceEnabled).booleanValue();
                SharedPreferences.Editor e = mPrefsF.edit();
                e.putBoolean("switch_sync", isServiceOn);
                e.commit();
                if(isServiceOn){
                    serviceStatus.setSummary("service Enabled");
                    //getActivity().startService(new Intent(getActivity(),MyServiceBT.class));
                    getActivity().startService(new Intent(getActivity(),ARService.class));
                    Log.i("BlueRemote", "servizio avviato switch");

                } else{
                    serviceStatus.setSummary("service Disabled");
                    //getActivity().stopService(new Intent(getActivity(),MyServiceBT.class));
                    getActivity().stopService(new Intent(getActivity(),ARService.class));
                    Log.i("BlueRemote", "servizio stoppato switch");
                }
                return true;
            }
        });
        //******************

        final EditTextPreference hostName = (EditTextPreference) findPreference("host_name");
        hostName.setSummary(hostName.getText());
        hostName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String inputHost = (String)newValue;
                hostName.setSummary(inputHost);
                return true;
            }
        });

        final EditTextPreference apiPass = (EditTextPreference) findPreference("api_pass");
        final ContentValues contentValuesP = new ContentValues();
        apiPass.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String inputPass = (String)newValue;
                apiPass.setSummary("***");
                contentValuesP.put("PREF_KEY", "api_pass");
                contentValuesP.put("TITLE", newValue.toString());
                contentValuesP.put("SUMMARY", newValue.toString());
                Uri uriI =  getContext().getContentResolver().insert(CONTENT_URI, contentValuesP);

                return true;
            }
        });

        final EditTextPreference entityID = (EditTextPreference) findPreference("entity_id");
        final ContentValues  contentValuesE = new ContentValues();
        entityID.setSummary(entityID.getText());
        entityID.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue2) {
                String inputEntityID = (String)newValue2;
                entityID.setSummary(inputEntityID);
                contentValuesE.put("PREF_KEY", "entity_id");
                contentValuesE.put("TITLE", newValue2.toString());
                contentValuesE.put("SUMMARY", newValue2.toString());
                Uri uriI =  getContext().getContentResolver().insert(CONTENT_URI, contentValuesE);
                return true;
            }
        });

        // TODO : entity detect id
        final EditTextPreference entitydetectID = (EditTextPreference) findPreference("entity_detect_id");
        final ContentValues  contentValuesED = new ContentValues();
        entitydetectID.setSummary(entitydetectID.getText());
        entitydetectID.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue2) {
                String inputEntityDetectID = (String)newValue2;
                entitydetectID.setSummary(inputEntityDetectID);
                contentValuesED.put("PREF_KEY", "entity_detect_id");
                contentValuesED.put("TITLE", newValue2.toString());
                contentValuesED.put("SUMMARY", newValue2.toString());
                Uri uriI =  getContext().getContentResolver().insert(CONTENT_URI, contentValuesED);
                return true;
            }
        });


        final ListPreference BTentity = (ListPreference) findPreference("bt_bounded");
        final ContentValues  contentValues = new ContentValues();
        BTentity.setSummary(BTentity.getValue().toString());
        BTentity.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue3) {
                BTentity.setSummary(newValue3.toString());
                contentValues.put("PREF_KEY", "bt_bounded");
                contentValues.put("TITLE", newValue3.toString());
                contentValues.put("SUMMARY", newValue3.toString());
                Uri uriI =  getContext().getContentResolver().insert(CONTENT_URI, contentValues);//Log.i("BlueRemote " , "provider delete nr: " + uriD );
                return true;

            }

        }); // fine listener

        //*************
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        mEntries = new ArrayList<CharSequence>();
        mEntriesValues = new ArrayList<CharSequence>();

        for(BluetoothDevice d : pairedDevices) {
            mEntries.add(d.getName());
            //mEntriesValues.add(d.getAddress());
            mEntriesValues.add(d.getName());
        }

        ListPreference lp = (ListPreference) findPreference("bt_bounded");
        lp.setEntries(listToArray(mEntries));
        lp.setEntryValues(listToArray(mEntriesValues));

    }

    public CharSequence[] listToArray(ArrayList<CharSequence> list){
        CharSequence[] sequences = new CharSequence[list.size()];
        for (int i = 0; i < list.size(); i++ ){
            sequences[i] = list.get(i);
        }
        return sequences;
    }


}