package net.eclissi.lucasop.ioboss;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.eclissi.lucasop.ioboss.fragments.EditGeoFenceFragment;

public class EditGeoFenceActivity extends AppCompatActivity {

    private static final String PROVIDER_NAME = "net.eclissi.lucasop.mypost.mypost2.PrefProvider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/text");

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.content_main);

        /*
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
        getSupportActionBar().setTitle("Edit GeoFence");

        getFragmentManager().beginTransaction().replace(R.id.content_main,
                new EditGeoFenceFragment()).commit();
    }
}
