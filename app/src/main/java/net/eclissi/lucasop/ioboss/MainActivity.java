package net.eclissi.lucasop.ioboss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.eclissi.lucasop.ioboss.database.DatabaseHelpher;
import net.eclissi.lucasop.ioboss.database.DatabaseModel;
import net.eclissi.lucasop.ioboss.database.RecyclerAdapter;
import net.eclissi.lucasop.ioboss.services.ARService;

import java.util.ArrayList;
import java.util.List;

/*
v0.1.0 branch maggio
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {


    DatabaseHelpher helpher;
    protected List<DatabaseModel> dbList;
    protected List<DatabaseModel> dbListOrigin;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUPrecyclerView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //*****************
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("switch_sync", false)) {
            // This is the first time running the app, let's go to onboarding
            startService(new Intent(getApplicationContext(),ARService.class));
            Log.i("BlueRemote", "servizio avviato switch");
        }else{
            stopService(new Intent(getApplicationContext(),ARService.class));
            Log.i("BlueRemote", "servizio stoppato switch");
        }
        //******************




    }

    private void setUPrecyclerView() {


        //setContentView(R.layout.placepicker);
        //setContentView(R.layout.content_main);


        helpher = new DatabaseHelpher(this);
        dbList = new ArrayList<DatabaseModel>();


        //helpher.deleteTable();
        String filtro = "1";
        dbList = helpher.getDataFromDB(filtro);
        dbListOrigin = helpher.getDataFromDB(filtro);


        mRecyclerView = (RecyclerView)findViewById(R.id.recycleviewMain);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerAdapter(this,dbList,new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i("###", "Main CALLBACK UPDATE IMAGE onClick");
            }
        });



        mRecyclerView.setAdapter(mAdapter);


    }

    private void filterRecyclerView(String charText){
        charText = charText.toLowerCase();

        dbList.clear();
        if (charText.length() == 0) {
            dbList.addAll(dbListOrigin);
        } else {
            for (DatabaseModel name : dbListOrigin) {
                if (name.getName().toLowerCase().contains(charText)) {
                    dbList.add(name);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        //return true;


        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        //adapter.setFilter(mCountryModel);

                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return true;

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
//        Toast.makeText(MainActivity.this, "Name is : "+query, Toast.LENGTH_SHORT).show();
        filterRecyclerView ( query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(MainActivity.this, "Name is : "+newText, Toast.LENGTH_SHORT).show();
        filterRecyclerView ( newText);
        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MainActivity.this, PlacePickerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(MainActivity.this, PieChartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this, PreferenceActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
