package net.eclissi.lucasop.ioboss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import net.eclissi.lucasop.ioboss.database.DatabaseHelpher;
import net.eclissi.lucasop.ioboss.database.DatabaseModel;
import net.eclissi.lucasop.ioboss.database.RecyclerAdapter;
import net.eclissi.lucasop.ioboss.database.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class PlacePickerActivity extends AppCompatActivity  {
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int UPDATE_GEO_REQUEST = 2;
    private ItemTouchHelper mItemTouchHelper;

    DatabaseHelpher helpher;
    List<DatabaseModel> dbList;
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;




    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(46.46, 11.30), new LatLng(46.50, 11.38));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placepicker);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Places");

        String filtro = "1' or stato ='0";

        helpher = new DatabaseHelpher(this);
        dbList= new ArrayList<DatabaseModel>();
        dbList = helpher.getDataFromDB(filtro);


        //mRecyclerView = (RecyclerView)findViewById(R.id.recycleview);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycleviewMain);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerAdapter(this,dbList);
        mRecyclerView.setAdapter(mAdapter);




        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        /* test preferenceFragment
                        Log.i("Blue", "press");
                        Intent intent = new Intent(PlacePickerActivity.this, EditGeoFenceActivity.class);
                        startActivity(intent);
                        */

                        String  name =((TextView)view.findViewById(R.id.rvname)).getText().toString();
                        String  maddress =((TextView)view.findViewById(R.id.rvaddress)).getText().toString();
                        String  coordinate =((TextView)view.findViewById(R.id.rvcoordinate)).getText().toString();
                        String  tipo =((TextView)view.findViewById(R.id.rvzonaID)).getText().toString();
                        String  dbID =((TextView)view.findViewById(R.id.rvdbID)).getText().toString();
                        String stato =((TextView)view.findViewById(R.id.rvstato)).getText().toString();
                        String  raggioT =((TextView)view.findViewById(R.id.rvraggio)).getText().toString();
                        int rag = Integer.parseInt(raggioT);
                        String  zonaID=((TextView)view.findViewById(R.id.rvzonaID)).getText().toString();



                        Intent i = new Intent(PlacePickerActivity.this, UpdateActivity.class);

                        i.putExtra("stato", stato);
                        i.putExtra("zonaID", zonaID);
                        i.putExtra("nome", name);
                        i.putExtra("indirizzo", maddress);
                        i.putExtra("coordinate", coordinate);
                        i.putExtra("tipo", tipo);
                        i.putExtra("rag", rag);
                        i.putExtra("dbID", dbID);
                        i.putExtra("cvID", position);

                        Log.i("Blue", "PlacePicker stato send [String]: " + stato);

                        //startActivity(i);
                        startActivityForResult(i, UPDATE_GEO_REQUEST);

                        // do whatever
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        //dbList.remove(position);
                        //mAdapter.notifyDataSetChanged();
                        //TextView id2 =(TextView)view.findViewById(R.id.rvkey_id);
                        //String tvValue = id2.getText().toString();
                        //helpher.deleteARow(tvValue);

                        // test fragment
                        //getFragmentManager().beginTransaction().replace(R.id.content_main,
                        //        new BlankFragment()).commit();

                        // test dialog fragment
                        //FragmentManager fm = getFragmentManager();
                        //EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newIstance("Edit geoFence");
                        //editNameDialogFragment.show(fm,"fragment_edit_name");


                        Log.i("Blue", "long press");
                    }
                })
        );



        /* ItemTouchHelper */


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView


                    dbList.remove(viewHolder.getAdapterPosition());
                    //RecyclerAdapter TestAdapter = (RecyclerAdapter)mRecyclerView.getAdapter();
                    View itemView = viewHolder.itemView;


                    //int id2 = TestAdapter.getDBkey(viewHolder.getAdapterPosition() )

                    mAdapter.notifyDataSetChanged();

                    TextView id2 =(TextView)itemView.findViewById(R.id.rvdbID);

                    String tvValue = id2.getText().toString();
                    helpher.deleteARow(tvValue);

                Log.i("vista", "vista modificata :" + tvValue);

            }


            @Override
            public boolean onMove(
                    final RecyclerView recyclerView,
                    final RecyclerView.ViewHolder viewHolder,
                    final RecyclerView.ViewHolder target) {
                return false;
            }


        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        /* ItemTouchHelper fine */

        FloatingActionButton pickerButton = (FloatingActionButton) findViewById(R.id.pickerButton);
        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    //intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build(PlacePickerActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            double mlat = place.getLatLng().latitude;
            double mlon = place.getLatLng().longitude;
            String mcoordinate = (String.valueOf(mlat).substring(0,9) + "," + String.valueOf(mlon).substring(0,9));

            //String attributions = (String) place.getAttributions();
            //if (attributions == null) {
            //    attributions = "";
            //}
            String attributions = "";
            /*
            mName.setText(name);
            mAddress.setText(address);
            mAttributions.setText(Html.fromHtml(attributions));
            */
            String stato="1";
            String zonaID = "";
            String tipo="IN";
            int rag=100;

            //helpher = new DatabaseHelpher(this);
            // insertIntoDB(String name,String address, String coordinate,String tipo)
            long idinsert = helpher.insertIntoDB( stato.toString(), zonaID, name.toString(), address.toString(), mcoordinate ,tipo, rag );


            // Add a new contact
            DatabaseModel mAdd = new DatabaseModel();

            mAdd.setdbID(idinsert);
            mAdd.setStato(stato);
            mAdd.setZonaID(zonaID);
            mAdd.setName(name.toString());
            mAdd.setAddress(address.toString());
            mAdd.setCoordinate(mcoordinate);
            mAdd.setTipo(tipo);
            mAdd.setRag(rag);




            dbList.add(mAdapter.getItemCount(), mAdd);
// Notify the adapter that an item was inserted at position 0
            mAdapter.notifyItemInserted(mAdapter.getItemCount());



        }
        if (requestCode == UPDATE_GEO_REQUEST && resultCode == Activity.RESULT_OK) {

            String stato=data.getStringExtra("stato");
            String zonaID=data.getStringExtra("zonaID");
            String name=data.getStringExtra("nome");
            String address=data.getStringExtra("indirizzo");
            String mcoordinate=data.getStringExtra("coordinate");
            String tipo=data.getStringExtra("tipo");
            int rag = data.getIntExtra("radius",0);
            int cvID = data.getIntExtra("cvID",0);
            String dbID = data.getStringExtra("dbID");

            Log.i("Blue", "PlacePicker result stato [String]: " + stato);

            //helpher.deleteARow(String.valueOf(dbID));
            dbList.remove(cvID);
            mAdapter.notifyDataSetChanged();

           //TextView id2 =(TextView)view.findViewById(R.id.rvkey_id);
            //String tvValue = id2.getText().toString();
            //helpher.deleteARow(dbID);

            // Add a new cv e db
            // long idinsert = helpher.insertIntoDB( stato, zonaID, name, address, mcoordinate ,tipo, rag );
            helpher.updateItemDB( Long.valueOf(dbID), stato, zonaID, name, address, mcoordinate ,tipo, rag );

            DatabaseModel mAdd = new DatabaseModel();
            //mAdd.setdbID(idinsert);
            mAdd.setdbID(Long.valueOf(dbID));
            mAdd.setStato(stato);
            mAdd.setZonaID(zonaID);
            mAdd.setName(name);
            mAdd.setAddress(address);
            mAdd.setCoordinate(mcoordinate);
            mAdd.setTipo(tipo);
            mAdd.setRag(rag);

            //dbList.add(mAdapter.getItemCount(), mAdd);
            dbList.add(cvID, mAdd);

            // Notify the adapter that an item was inserted at position 0
            //mAdapter.notifyItemInserted(mAdapter.getItemCount());
            mAdapter.notifyItemRangeChanged(cvID,mAdapter.getItemCount());

            //TextView id2 =(TextView)view.findViewById(R.id.rvkey_id);
            //String tvValue = id2.getText().toString();
            //helpher.deleteARow(tvValue);
        }

        //else {
        //    super.onActivityResult(requestCode, resultCode, data);
        //}


    }
}


