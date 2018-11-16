package com.mobitant.bestfood.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobitant.bestfood.Constant;
import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.ProductGridModellClass;
import com.mobitant.bestfood.lib.DialogLib;
import com.mobitant.bestfood.lib.GoLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView_Roboto_Bold;


/**
 * Created by Rp on 6/14/2016.
 */
public class RecycleAdapteProductGrid extends RecyclerView.Adapter<RecycleAdapteProductGrid.MyViewHolder> {
    Context context;

    private final String TAG = this.getClass().getSimpleName();
    private ArrayList<FoodInfoItem> moviesList;
    int memberSeq;
    int myPos = 0;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        ImageView image;
        LinearLayout linear;
        ImageView keep;
        MyTextView_Roboto_Bold contestNickName;

        public MyViewHolder(View view) {
            super(view);
            contestNickName = (MyTextView_Roboto_Bold)view.findViewById(R.id.contest_nick_name);
            title = (TextView) view.findViewById(R.id.contest_item_title) ;
            image = (ImageView) view.findViewById(R.id.image);
            keep = (ImageView) itemView.findViewById(R.id.keep);
            //text = (TextView) view.findViewById(R.id.text);
            linear = (LinearLayout) view.findViewById(R.id.linear);

        }

    }


    public RecycleAdapteProductGrid(Context context, ArrayList<FoodInfoItem> moviesList) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contest_item_popular_list, parent, false);


        return new MyViewHolder(itemView);


    }

    public void addItemList(ArrayList<FoodInfoItem> itemList) {
        this.moviesList.addAll(itemList);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        memberSeq = ((MyApp) context.getApplicationContext()).getMemberSeq();
        FoodInfoItem movie = moviesList.get(position);
        //holder.image.setImageResource(movie.getImage());
        final FoodInfoItem item = moviesList.get(position);
        MyLog.d(TAG, "getView " + item);

        if (item.isKeep) {
            holder.keep.setImageResource(R.drawable.ic_keep_on);
        } else {
            holder.keep.setImageResource(R.drawable.ic_keep_off);
        }

        holder.title.setText(item.name);
        //holder.description.setText(StringLib.getInstance().getSubString(context,
          //      item.description, Constant.MAX_LENGTH_DESCRIPTION));

        setImage(holder.image, item.imageFilename);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoLib.getInstance().goBestFoodInfoActivity(context, item.seq,item.post_nickname);
            }
        });
        holder.contestNickName.setText(item.post_nickname);
        holder.keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.isKeep) {
                    DialogLib.getInstance().showKeepDeleteDialog(context,
                            keepDeleteHandler, memberSeq, item.seq);
                } else {
                    DialogLib.getInstance().showKeepInsertDialog(context,
                            keepInsertHandler, memberSeq, item.seq);
                }
            }
        });

/*첫두줄만 그림옆에 작게 표시가 되어있었다..
        if(position==0 | position==1){
            holder.text.setVisibility(View.VISIBLE);

        }else {
            holder.text.setVisibility(View.GONE);
        }
*/


    }

    private void setImage(ImageView imageView, String fileName) {
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.with(context).load(R.drawable.bg_bestfood_drawer).into(imageView);
        } else {
            Picasso.with(context).load(RemoteService.IMAGE_URL + fileName).into(imageView);
        }
    }

    /**
     * 즐겨찾기 추가가 성공한 경우를 처리하는 핸들러
     */
    Handler keepInsertHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            changeItemKeep(msg.what, true);
        }
    };

    /**
     * 즐겨찾기 삭제가 성공한 경우를 처리하는 핸들러
     */
    Handler keepDeleteHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            changeItemKeep(msg.what, false);
        }
    };

    /**
     * 즐겨찾기 상태를 변경한다.
     * @param seq 맛집 정보 시퀀스
     * @param keep 즐겨찾기 추가 유무
     */
    private void changeItemKeep(int seq, boolean keep) {
        for (int i=0; i < moviesList.size(); i++) {
            if (moviesList.get(i).seq == seq) {
                moviesList.get(i).isKeep = keep;
                notifyItemChanged(i);
                break;
            }
        }
    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }



}


