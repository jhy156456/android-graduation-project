package com.mobitant.bestfood.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.ProductGridModellClass;

import java.util.ArrayList;
import java.util.List;




/**
 * Created by Rp on 6/14/2016.
 */
public class RecycleAdapteProductGrid extends RecyclerView.Adapter<RecycleAdapteProductGrid.MyViewHolder> {
    Context context;


    private List<ProductGridModellClass> moviesList;

    int myPos = 0;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        ImageView image;
        LinearLayout linear;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.contest_item_title) ;
            image = (ImageView) view.findViewById(R.id.image);
            //text = (TextView) view.findViewById(R.id.text);
            linear = (LinearLayout) view.findViewById(R.id.linear);

        }

    }


    public RecycleAdapteProductGrid(Context context, ArrayList<ProductGridModellClass> moviesList) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contest_item_popular_list, parent, false);


        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        ProductGridModellClass movie = moviesList.get(position);
        holder.image.setImageResource(movie.getImage());


/*첫두줄만 그림옆에 작게 표시가 되어있었다..
        if(position==0 | position==1){
            holder.text.setVisibility(View.VISIBLE);

        }else {
            holder.text.setVisibility(View.GONE);
        }
*/


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }



}


