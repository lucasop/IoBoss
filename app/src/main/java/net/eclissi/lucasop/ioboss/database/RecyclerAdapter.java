package net.eclissi.lucasop.ioboss.database;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.eclissi.lucasop.ioboss.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user_adnig on 11/14/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  {


    static   List<DatabaseModel> dbList;
    static  Context context;
    public RecyclerAdapter(Context context, List<DatabaseModel> dbList ){
        this.dbList = new ArrayList<DatabaseModel>();
        this.context = context;
        this.dbList = dbList;

    }




    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                //R.layout.item_raw_material, null);
                R.layout.item_raw, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
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

       // holder.img.setImageDrawable(RecyclerAdapter.context.getResources().getDrawable(R.drawable.ic_place_in, RecyclerAdapter.context.getTheme()));

        String srag = Integer.toString(dbList.get(position).getRag());
        holder.rag.setText(srag);


        switch (holder.tipo.getText().toString()){
            case "IN":
                holder.img.setImageDrawable(RecyclerAdapter.context.getResources().getDrawable(R.drawable.ic_place_in, RecyclerAdapter.context.getTheme()));
                break;
            case "OUT":
                holder.img.setImageDrawable(RecyclerAdapter.context.getResources().getDrawable(R.drawable.ic_place_out, RecyclerAdapter.context.getTheme()));
                break;
            case "DWELL":
                holder.img.setImageDrawable(RecyclerAdapter.context.getResources().getDrawable(R.drawable.ic_place_dwell, RecyclerAdapter.context.getTheme()));
                break;
        }

        switch (holder.stato.getText().toString()){
            case "0":
                 holder.sepv.setBackgroundColor(RecyclerAdapter.context.getResources().getColor( R.color.cardview_shadow_start_color));
                break;
            case "1":
                holder.sepv.setBackgroundColor(RecyclerAdapter.context.getResources().getColor( R.color.light_red));
                 break;
         }
    }


    public double getDBkey (RecyclerAdapter.ViewHolder holder, int position){
        return Integer.getInteger(holder.dbID.getText().toString());
    }



    @Override
    public int getItemCount() {
        return dbList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView dbID,stato,zonaID, name, address,coordinate,tipo,rag, sepv;
        public ImageView img;
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
            sepv = (TextView)itemLayoutView.findViewById(R.id.separaV);

            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*Intent intent = new Intent(context,DetailsActivity.class);

            Bundle extras = new Bundle();
            extras.putInt("position",getAdapterPosition());
            intent.putExtras(extras);
*/
            /*
            int i=getAdapterPosition();
            intent.putExtra("position", getAdapterPosition());*/
            //context.startActivity(intent);
            Toast.makeText(RecyclerAdapter.context, "you have clicked Row " + getAdapterPosition(), Toast.LENGTH_LONG).show();
        }
    }
}
