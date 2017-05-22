package net.eclissi.lucasop.ioboss;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by lucasoprana on 04/02/2017.
 */

public class UpdateActivity extends AppCompatActivity {

   // public final String ulg_tipo = "";
   private RadioGroup rg;
    private RadioButton rb;
    private String ulg_tipo;
    private int cvID;
    private String dbID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("GeoFency Update");

        Button button = (Button) findViewById(R.id.lg_botton_update);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {

                Switch switchButton = (Switch)findViewById(R.id.lg_switch);
                Boolean ulg_switch = switchButton.isChecked();
                String  ulg_zonaID =((TextView)findViewById(R.id.lg_zonaID)).getText().toString();
                String  ulg_nome =((TextView)findViewById(R.id.lg_name)).getText().toString();
                String  ulg_maddress =((TextView)findViewById(R.id.lg_indirizzo)).getText().toString();
                String  ulg_latlong =((TextView)findViewById(R.id.lg_latlong)).getText().toString();
                Integer ulg_radius =((SeekBar)findViewById(R.id.lg_radius)).getProgress();


                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupLocation);
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
                String ulg_tipo = (String) radioButton.getText();

                String stato = "0";
                 if (ulg_switch) {
                    stato = "1";
                } else {
                    stato = "0";
                }


                Intent i = new Intent();
                i.putExtra("stato", stato);
                i.putExtra("zonaID", ulg_zonaID);
                i.putExtra("nome", ulg_nome);
                i.putExtra("indirizzo", ulg_maddress);
                i.putExtra("coordinate", ulg_latlong);
                i.putExtra("tipo", ulg_tipo);
                i.putExtra("radius", ulg_radius);
                i.putExtra("dbID", dbID);
                i.putExtra("cvID", cvID);

                Log.i("Blue", "Update send stato [String]: " + stato);




                setResult(RESULT_OK, i);
                finish();


            }
        });

        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroupLocation);


        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                View radioButton = group.findViewById(checkedId);
                int index = group.indexOfChild(radioButton);
                ImageView imgFp = (ImageView) findViewById(R.id.lg_imgtype);
                switch (index){
                    case 0:
                        //Log.i("Blue", "checked: IN 0");
                        imgFp.setImageDrawable(getResources().getDrawable(R.drawable.ic_place_in_v, getApplicationContext().getTheme()));
                        break;
                    case 1:
                        //Log.i("Blue", "checked: IN 1");
                        imgFp.setImageDrawable(getResources().getDrawable(R.drawable.ic_place_out_v, getApplicationContext().getTheme()));
                        break;
                    case 2:
                        //Log.i("Blue", "checked: DWELL 2");
                        imgFp.setImageDrawable(getResources().getDrawable(R.drawable.ic_place_dwell_v, getApplicationContext().getTheme()));
                        break;
                }

            }
        });

        Intent intent = getIntent();
        String stato1 = intent.getStringExtra("stato");
        String zonaID = intent.getStringExtra("zonaID");
        String name = intent.getStringExtra("nome");
        String maddress = intent.getStringExtra("indirizzo");
        String coordinate = intent.getStringExtra("coordinate");
        String tipo = intent.getStringExtra("tipo");
        int rag = intent.getIntExtra("rag",0);
        dbID = intent.getStringExtra("dbID");
        cvID = intent.getIntExtra("cvID",0);

        Log.i("Blue", "Update dbID: " + dbID);

        Boolean stato = false;
        switch (stato1){
            case "0":
                stato = false;
                break;
            case "1":
                stato = true;
                break;
        }

        ((Switch)findViewById(R.id.lg_switch)).setChecked(stato);
        ((TextView)findViewById(R.id.lg_zonaID)).setText(zonaID);
        ((TextView)findViewById(R.id.lg_name)).setText(name);
        ((TextView)findViewById(R.id.lg_indirizzo)).setText(maddress);
        ((TextView)findViewById(R.id.lg_latlong)).setText(coordinate);

        ((SeekBar)findViewById(R.id.lg_radius)).setProgress(rag);
        ((TextView)findViewById(R.id.lg_radius_text)).setText(rag + " m");



        ImageView imgFp = (ImageView) findViewById(R.id.lg_imgtype);

            switch (tipo){

                case "IN":
                    Log.i("Blue", "start: IN 0");
                    imgFp.setImageDrawable(getResources().getDrawable(R.drawable.ic_place_in_v, getApplicationContext().getTheme()));
                    ((RadioButton)findViewById(R.id.lg_in)).setChecked(true);
                    break;
                case "OUT":
                    Log.i("Blue", "start: IN 1");
                    imgFp.setImageDrawable(getResources().getDrawable(R.drawable.ic_place_out_v, getApplicationContext().getTheme()));
                    ((RadioButton)findViewById(R.id.lg_out)).setChecked(true);

                    break;
                case "DWELL":
                    Log.i("Blue", "start: DWELL 2");
                    imgFp.setImageDrawable(getResources().getDrawable(R.drawable.ic_place_dwell_v, getApplicationContext().getTheme()));
                    ((RadioButton)findViewById(R.id.lg_dwell)).setChecked(true);
                    break;

            }




        ((TextView)findViewById(R.id.lg_zonaID)).setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        SeekBar seekBar = (SeekBar)findViewById(R.id.lg_radius);
        final TextView seekBarValue = (TextView)findViewById(R.id.lg_radius_text);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                seekBarValue.setText(String.valueOf(progress) + " m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

    }

}
