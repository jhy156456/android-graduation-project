package com.mobitant.bestfood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.ImageItem;
import com.mobitant.bestfood.item.NotificationsModel;
import com.mobitant.bestfood.lib.MyLog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wolfsoft3 on 17/7/18.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {
    Context context;

    private List<NotificationsModel> modelfood9List;

    @Override
    public NotificationsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notifications, parent, false);

        return new NotificationsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationsAdapter.MyViewHolder holder, int position) {
               NotificationsModel modelfoodrecycler = modelfood9List.get(position);

        holder.foodtext1.setText(modelfoodrecycler.getTitle());
        holder.foodtext2.setText(modelfoodrecycler.getContents());


        //<=======시간표시 ========>
        String year = "";
        String month = "";
        String day = "";
        String hour = "";
        String minute = "";
        year = modelfoodrecycler.getCreated_at().substring(0, 4);
        month =modelfoodrecycler.getCreated_at().substring(5, 7);
        day =modelfoodrecycler.getCreated_at().substring(8, 10);
        int koreanHour = Integer.parseInt(modelfoodrecycler.getCreated_at().substring(11, 13));
        koreanHour += 9; //koreanHour가 21이면 30이된다 그러므로 24보다 크거나같으면 24를뺀다
        if (koreanHour >= 24) {
            koreanHour -= 24;
            int koreanDay = Integer.parseInt(day);
            koreanDay += 1;
            day = String.valueOf(koreanDay);
        }
        MyLog.d("한국시간 : " + koreanHour);
        hour = String.valueOf(koreanHour);
        minute = modelfoodrecycler.getCreated_at().substring(14, 16);
        holder.foodtext3.setText(year + "년 " + month + "월 " + day + "일 " + hour + "시 " + minute + "분");
        //<=======시간표시 ========>

        holder.foodimg1.setImageResource(R.drawable.ic_person);


    }
    /**
     * 현재 아이템 리스트에 새로운 아이템 리스트를 추가한다.
     */
    public void addItemList(ArrayList<NotificationsModel> modelfood9List) {
        this.modelfood9List.addAll(modelfood9List);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {

        return modelfood9List.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView foodtext1, foodtext2, foodtext3;

        ImageView foodimg1;

        public MyViewHolder(View itemView) {
            super(itemView);
            foodtext1 = itemView.findViewById(R.id.foodtext1);
            foodtext2 = itemView.findViewById(R.id.foodtext2);
            foodtext3 = itemView.findViewById(R.id.foodtext3);
            foodimg1 = itemView.findViewById(R.id.foodimg1);

        }
    }

    public NotificationsAdapter(Context mainActivityContacts, List<NotificationsModel> offerList) {
        this.modelfood9List = offerList;
        this.context = mainActivityContacts;
    }
}
