package net.eclissi.lucasop.ioboss.database;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.eclissi.lucasop.ioboss.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by user_adnig on 11/14/15.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    static   List<DatabaseModel> dbList;
    static  Context context;
    List<String> itemsPendingRemoval;
    private OnItemClickListener mOnItemClickListener;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public RecyclerAdapter(Context context, List<DatabaseModel> dbList , OnItemClickListener mOnItemClickListener){
        this.dbList = new ArrayList<DatabaseModel>();
        this.context = context;
        this.dbList = dbList;
        this.mOnItemClickListener= mOnItemClickListener;

        itemsPendingRemoval = new ArrayList<>();
    }





    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                //R.layout.item_raw_material, null);
                R.layout.item_raw_c, parent, false);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    public boolean isPendingRemoval(RecyclerView.ViewHolder viewHolder, int position) {
        final String item = Integer.toString(viewHolder.getAdapterPosition());
        return itemsPendingRemoval.contains(item);
    }



    public void pendingRemoval(final RecyclerView.ViewHolder viewHolder, final int position, final DatabaseHelpher helpher) {
        final String item = Integer.toString(viewHolder.getAdapterPosition());
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            Log.d("###", "pendingRemoval size array :" + itemsPendingRemoval.size() + " item: " + item );
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove((RecyclerAdapter.ViewHolder) viewHolder, item, helpher);
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(RecyclerAdapter.ViewHolder holder, String position, DatabaseHelpher helpher) {

        String item = position;

        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
            Log.d("###", "remove size array :" + itemsPendingRemoval.size() + " item: " + item );


            dbList.remove(Integer.parseInt(item));
            View itemView = holder.itemView;
            notifyDataSetChanged();
            TextView id2 =(TextView)itemView.findViewById(R.id.rvdbID);
            String tvValue = id2.getText().toString();
            Log.d("###", "remove db :"  + tvValue );

            helpher.deleteARow(tvValue);
            notifyItemRemoved(Integer.parseInt(item));
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, final int position) {

        final String item = Integer.toString(holder.getAdapterPosition());
        if (itemsPendingRemoval.contains(item)) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            //holder.titleTextView.setVisibility(View.GONE);
            //holder.zonaID.setBackgroundColor(Color.LTGRAY);

            holder.stato.setTextColor(Color.LTGRAY);
            holder.zonaID.setTextColor(Color.LTGRAY);
            holder.name.setTextColor(Color.LTGRAY);
            holder.address.setTextColor(Color.LTGRAY);
            holder.coordinate.setTextColor(Color.LTGRAY);
            holder.tipo.setTextColor(Color.LTGRAY);
            holder.rag.setTextColor(Color.LTGRAY);
            holder.ragT.setTextColor(Color.LTGRAY);
            holder.sepv.setBackgroundColor(Color.LTGRAY);
            holder.img.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);

            holder.undoButton.setTextColor(Color.BLACK);
            holder.undoButton.setVisibility(View.VISIBLE);
            holder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Log.i("###", "UNDO BOTTONE onClick");
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(position);
                }
            });
        } else {

            holder.itemView.setBackgroundColor(Color.WHITE);

            holder.stato.setVisibility(View.VISIBLE);
            holder.zonaID.setTextColor(Color.DKGRAY);
            holder.name.setTextColor(Color.GRAY);
            holder.address.setTextColor(Color.GRAY);
            holder.coordinate.setTextColor(Color.GRAY);
            holder.tipo.setTextColor(Color.GRAY);
            holder.rag.setTextColor(Color.GRAY);
            holder.ragT.setTextColor(Color.GRAY);
            holder.img.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
            holder.sepv.setBackgroundColor(Color.WHITE);


            holder.undoButton.setVisibility(View.GONE);
            holder.undoButton.setOnClickListener(null);


            //Log.i("###2", "CLASS: "+ this.getClass().getSuperclass().getSimpleName());

                holder.edit.setOnClickListener(new View.OnClickListener() {
                     @Override
                    public void onClick(View v) {
                        // visualizza popup menu
                         showPopup(v, position, holder);
                        //mOnItemClickListener.onItemClick(v , position);
                        Log.i("###", "UPDATE IMAGE onClick position: "+position);
                    }
                });






            String skey = Long.toString(dbList.get(position).getdbID());
            holder.dbID.setText(skey);
            holder.stato.setText(dbList.get(position).getStato());
            //Log.i("Blue", "onBindViewHolder result stato [String]: " + stato);
            holder.zonaID.setText(dbList.get(position).getzonaID());
            holder.name.setText(dbList.get(position).getName());
            holder.address.setText(dbList.get(position).getAddress());
            holder.coordinate.setText(dbList.get(position).getCoordinate());
            holder.tipo.setText(dbList.get(position).getTipo());

            /*
            tipo: IN, OUT, DWELL
            stato: zona attiva/disattiva
            */

            String srag = Integer.toString(dbList.get(position).getRag());
            holder.rag.setText(srag);


            switch (holder.tipo.getText().toString()){
                case "IN":
                    if ( holder.stato.getText().toString().equals("1") ) {
                        holder.img.setImageDrawable(RecyclerAdapter.context.getResources().getDrawable(R.drawable.ic_place_in_v, RecyclerAdapter.context.getTheme()));
                        holder.img.setColorFilter(Color.DKGRAY);
                    } else {
                        holder.img.setImageDrawable(RecyclerAdapter.context.getResources().getDrawable(R.drawable.ic_place_in_v, RecyclerAdapter.context.getTheme()));
                        holder.img.setColorFilter(Color.LTGRAY);
                    }
                    break;
                case "OUT":
                    if ( holder.stato.getText().toString().equals("1") ) {
                        holder.img.setImageDrawable(RecyclerAdapter.context.getResources().getDrawable(R.drawable.ic_place_out_v, RecyclerAdapter.context.getTheme()));
                        holder.img.setColorFilter(Color.DKGRAY);
                    }else {
                        holder.img.setImageDrawable(RecyclerAdapter.context.getResources().getDrawable(R.drawable.ic_place_out_v, RecyclerAdapter.context.getTheme()));
                        holder.img.setColorFilter(Color.LTGRAY);
                    }
                    break;
                case "DWELL":
                    if ( holder.stato.getText().toString().equals("1") ) {
                        holder.img.setImageDrawable(RecyclerAdapter.context.getResources().getDrawable(R.drawable.ic_place_dwell_v, RecyclerAdapter.context.getTheme()));
                        holder.img.setColorFilter(Color.DKGRAY);
                    }else {
                        holder.img.setImageDrawable(RecyclerAdapter.context.getResources().getDrawable(R.drawable.ic_place_dwell_v, RecyclerAdapter.context.getTheme()));
                        holder.img.setColorFilter(Color.LTGRAY);
                    }
                    break;
            }

            /*
            switch (holder.stato.getText().toString()){
                case "0":
                     holder.sepv.setBackgroundColor(RecyclerAdapter.context.getResources().getColor( R.color.cardview_shadow_start_color));
                    break;
                case "1":
                    holder.sepv.setBackgroundColor(RecyclerAdapter.context.getResources().getColor( R.color.light_red));
                     break;
             }
             */
        }
    }


    public double getDBkey (RecyclerAdapter.ViewHolder holder, int position){
        return Integer.getInteger(holder.dbID.getText().toString());
    }


    @Override
    public int getItemCount() {
        return dbList.size();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView dbID,stato,zonaID, name, address,coordinate,tipo,rag, sepv, ragT;
        public ImageView img,edit;

        TextView titleTextView;
        Button undoButton;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            dbID = (TextView)itemLayoutView.findViewById(R.id.rvdbID);
            stato= (TextView)itemLayoutView.findViewById(R.id.rvstato);
            zonaID = (TextView) itemLayoutView.findViewById(R.id.rvzonaID);
            name = (TextView) itemLayoutView.findViewById(R.id.rvname);
            address = (TextView)itemLayoutView.findViewById(R.id.rvaddress);
            coordinate = (TextView)itemLayoutView.findViewById(R.id.rvcoordinate);
            tipo = (TextView)itemLayoutView.findViewById(R.id.rvtipo);
            rag = (TextView)itemLayoutView.findViewById(R.id.rvraggio);
            img = (ImageView)itemLayoutView.findViewById(R.id.ic_row_place);
            edit = (ImageView)itemLayoutView.findViewById(R.id.icEdit);
            sepv = (TextView)itemLayoutView.findViewById(R.id.separaV);
            titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            ragT = (TextView) itemView.findViewById(R.id.textView3);
            undoButton = (Button) itemView.findViewById(R.id.undo_button);

        }


    }

    private void showPopup( final View view, final int position, final RecyclerAdapter.ViewHolder holder){
        // pass the imageview id
        View menuItemView = view.findViewById(R.id.icEdit);
        PopupMenu popup = new PopupMenu(RecyclerAdapter.context, menuItemView);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.pop_up_recycledview, popup.getMenu());
        //Log.e("position -- " + position);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cvedit:
                        // richiama l'activity del edit della scheda.
                        mOnItemClickListener.onItemClick(view , position);
                        break;
                    case R.id.cvshare:
                        // condivide la scheda.

                        String message = holder.zonaID.getText().toString();
                        //message += holder.name.getText().toString() +"\n";
                        //message += holder.address.getText().toString() +"\n";
                        //String uriMessage = "http://maps.google.com/maps?qsaddr=" + holder.coordinate.getText().toString();

                        String mCoordinate = holder.coordinate.getText().toString();
                        String[] separateCoordinate = mCoordinate.split(",");

                        double mLat = Double.parseDouble(separateCoordinate[0]);
                        double mLon = Double.parseDouble(separateCoordinate[1]);



                        //String uriMessage = String.format(Locale.ENGLISH, "geo:%f,%f",mLat, mLon );
                        String uriMessage = "http://maps.google.com/maps?q=loc:" + String.format(Locale.ENGLISH, "%f,%f",mLat, mLon );
                        Log.i("###", "mLat: "+mLat);
                        Log.i("###", "mLon: "+mLon);
                        Log.i("###", "Uri Geo: "+ (uriMessage));



                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_SUBJECT, message);
                        share.putExtra(Intent.EXTRA_TEXT, uriMessage);


                        context.startActivity(Intent.createChooser(share, "Condividi Place"));
                        break;


                    default:
                        return false;
                }
                return false;
            }
        });
        popup.show();
    }
    
} // fine classe RecycledAdapter
