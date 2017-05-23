package net.eclissi.lucasop.ioboss;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PlacePickerActivity extends AppCompatActivity  {
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int UPDATE_GEO_REQUEST = 2;
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    private ItemTouchHelper mItemTouchHelper;
    public static final float ALPHA_FULL = 1.0f;

    // float icon

    FloatingActionButton fab, fab1, fab2, fab3;
    LinearLayout fabLayout1, fabLayout2, fabLayout3;
    View fabBGLayout;
    boolean isFABOpen=false;

    DatabaseHelpher helpher;
    List<DatabaseModel> dbList;
    //List<String> itemsPendingRemoval;
    RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //boolean undoOn;

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(46.46, 11.30), new LatLng(46.50, 11.38));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placepicker);
        //undoOn = true;

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Places");

        // float icon
        fabLayout1= (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2= (LinearLayout) findViewById(R.id.fabLayout2);
        fabLayout3= (LinearLayout) findViewById(R.id.fabLayout3);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.pickerButton);
        fab2= (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fabBGLayout=findViewById(R.id.fabBGLayout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        // helper
        String filtro = "1' or stato ='0";

        helpher = new DatabaseHelpher(this);
        dbList= new ArrayList<DatabaseModel>();
        dbList = helpher.getDataFromDB(filtro);
        //mAdapter.setOnItemClickListener(this);


        //mRecyclerView = (RecyclerView)findViewById(R.id.recycleview);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycleviewMain);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new RecyclerAdapter(this,dbList, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view2, int position) {
                Log.i("###", "CALLBACK UPDATE IMAGE onClick position: " + position);

                View view = mLayoutManager.findViewByPosition(position);

                String  name =((TextView)view.findViewById(R.id.rvname)).getText().toString();
                String  maddress =((TextView)view.findViewById(R.id.rvaddress)).getText().toString();
                String  coordinate =((TextView)view.findViewById(R.id.rvcoordinate)).getText().toString();
                String  tipo =((TextView)view.findViewById(R.id.rvtipo)).getText().toString();
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
        });


        mRecyclerView.setAdapter(mAdapter);
        setUpAnimationDecoratorHelper();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            //ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            // implementazione undo

            Drawable background;
            Drawable xMark;
            Paint txtDraw;
            String mText;
            int xMarkMargin;
            boolean initiated;


            // implementazione undo
            private void init() {
                background = new ColorDrawable(Color.LTGRAY);


                xMark = ContextCompat.getDrawable(PlacePickerActivity.this, R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) PlacePickerActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);

                txtDraw = new Paint(Paint.ANTI_ALIAS_FLAG);
                mText = "Elimina";
                txtDraw.setStyle(Paint.Style.FILL);
                txtDraw.setColor(Color.BLACK);
                txtDraw.setTextSize(48);
                txtDraw.setAntiAlias(true);

                initiated = true;
          }



            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();

                if ( mAdapter.isPendingRemoval(viewHolder, position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
              mAdapter.pendingRemoval(viewHolder, viewHolder.getAdapterPosition(), helpher);
            }

            @Override
            public boolean onMove(
                    final RecyclerView recyclerView,
                    final RecyclerView.ViewHolder viewHolder,
                    final RecyclerView.ViewHolder target) {
                return false;
            }

            // implementazione undo
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;




                        // not sure why, but this method get's called for viewholder that are already swiped away
                        if (viewHolder.getAdapterPosition() == -1) {
                            // not interested in those
                            return;
                        }

                        if (!initiated) {
                            init();
                        }

                        // draw red background
                        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        background.draw(c);

                        // draw x mark
                        int itemHeight = itemView.getBottom() - itemView.getTop();
                        int intrinsicWidth = xMark.getIntrinsicWidth();
                        int intrinsicHeight = xMark.getIntrinsicWidth();

                        int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                        int xMarkRight = itemView.getRight() - xMarkMargin;
                        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                        int xMarkBottom = xMarkTop + intrinsicHeight;
                        xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                        xMark.draw(c);

                    //Log.d("IDRAW", "item TOP: " + itemView.getTop());
                    //Log.d("IDRAW", "item xTOP: " + xMarkTop);

                    // draw text
                    int  mIntrinsicWidth = (int) (txtDraw.measureText(mText, 0, mText.length()) + .5);
                    int  mIntrinsicHeight = txtDraw.getFontMetricsInt(null);
                    int yDraw = itemView.getTop()+(itemHeight+mIntrinsicHeight)/2;
                    int xDraw = (itemView.getRight()/2)-mIntrinsicWidth;

                    c.translate(xDraw, yDraw);
                    c.drawText(mText, 0, 0, txtDraw);


                    // Fade out the view as it is swiped out of the parent's bounds
                    final float alpha = (float) (ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth());
                    int alphaB = Math.round(Color.alpha(Color.WHITE) * alpha);
                    //Log.d("IDRAW", "alphaB: " + alphaB);

                    viewHolder.itemView.getBackground().mutate().setAlpha(alphaB);
                    viewHolder.itemView.setAlpha(alpha);

                    viewHolder.itemView.setTranslationX(dX);
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }


        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

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
            String stato="1";
            String zonaID = "";
            String tipo="IN";
            int rag=100;

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

            // Add a new cv e db
            // long idinsert = helpher.insertIntoDB( stato, zonaID, name, address, mcoordinate ,tipo, rag );
            helpher.updateItemDB( Long.valueOf(dbID), stato, zonaID, name, address, mcoordinate ,tipo, rag );

            DatabaseModel mAdd = new DatabaseModel();
            mAdd.setdbID(Long.valueOf(dbID));
            mAdd.setStato(stato);
            mAdd.setZonaID(zonaID);
            mAdd.setName(name);
            mAdd.setAddress(address);
            mAdd.setCoordinate(mcoordinate);
            mAdd.setTipo(tipo);
            mAdd.setRag(rag);

            dbList.add(cvID, mAdd);
            // Notify the adapter that an item was inserted at position 0
            //mAdapter.notifyItemInserted(mAdapter.getItemCount());
            mAdapter.notifyItemRangeChanged(cvID,mAdapter.getItemCount());
        }
    }



    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.LTGRAY);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

// float ico

    private void showFABMenu(){
        isFABOpen=true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(225);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotationBy(-225);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(isFABOpen){
            closeFABMenu();
        }else{
            super.onBackPressed();
        }
    }
}


