package com.mobitant.bestfood.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.Food7_Model;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.OrderCheckItem;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import customfonts.MyTextView_Roboto_Regular;

public class Food7_Adapter extends RecyclerView.Adapter<Food7_Adapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderCheckItem> modelArrayList;

    public Food7_Adapter(Context context, ArrayList<OrderCheckItem> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food7_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderCheckItem food7_model = modelArrayList.get(position);

        holder.Restaurant_Id.setText(food7_model.getInfoTitle());
        holder.Address_Id.setText(food7_model.getPostNickName());
        //  holder.Reviews_Id.setText(food7_model.getReviews_Id());

        //<=======시간표시 ========>
        String year = "";
        String month = "";
        String day = "";
        String hour = "";
        String minute = "";
        year = food7_model.getCreated_at().substring(0, 4);
        month = food7_model.getCreated_at().substring(5, 7);
        day = food7_model.getCreated_at().substring(8, 10);
        int koreanHour = Integer.parseInt(food7_model.getCreated_at().substring(11, 13));
        koreanHour += 9; //koreanHour가 21이면 30이된다 그러므로 24보다 크거나같으면 24를뺀다
        if (koreanHour >= 24) {
            koreanHour -= 24;
            int koreanDay = Integer.parseInt(day);
            koreanDay += 1;
            day = String.valueOf(koreanDay);
        }
        MyLog.d("한국시간 : " + koreanHour);
        hour = String.valueOf(koreanHour);
        minute = food7_model.getCreated_at().substring(14, 16);
        holder.Date_Id.setText(year + "년 " + month + "월 " + day + "일 " + hour + "시 " + minute + "분");
        //<=======시간표시 ========>
        holder.Dollar_Id.setText(food7_model.getPostPrice());

        //  holder.FoodImage_Id.setImageResource(food7_model.getInfoFirstImageFilename());
        if (StringLib.getInstance().isBlank(food7_model.getInfoFirstImageFilename())) {
            Picasso.with(context).load(R.drawable.ic_person).into(holder.FoodImage_Id);
        } else {
            Picasso.with(context)
                    .load(RemoteService.IMAGE_URL + food7_model.getInfoFirstImageFilename())
                    .into(holder.FoodImage_Id);
        }
        holder.historyCardHolder.setText(food7_model.getCardHorder());
        holder.histroyCardNumber.setText(food7_model.getCardNumber());
        holder.History_Id.setImageResource(R.drawable.history);
        holder.Dollar_Id.setText(food7_model.getPostPrice());
    }

    /**
     * 현재 아이템 리스트에 새로운 아이템 리스트를 추가한다.
     *
     * @param itemList 새로운 아이템 리스트
     */
    public void addItemList(ArrayList<OrderCheckItem> itemList) {
        this.modelArrayList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Restaurant_Id, Address_Id, Date_Id, Dollar_Id;
        ImageView FoodImage_Id, History_Id;
        MyTextView_Roboto_Regular histroyCardNumber, historyCardHolder;

        public ViewHolder(View itemView) {
            super(itemView);

            Restaurant_Id = itemView.findViewById(R.id.Restaurant_Id);
            Address_Id = itemView.findViewById(R.id.Address_Id);
            Date_Id = itemView.findViewById(R.id.Date_Id);
            Dollar_Id = itemView.findViewById(R.id.Dollar_Id);
            histroyCardNumber = itemView.findViewById(R.id.history_card_number);
            historyCardHolder = itemView.findViewById(R.id.history_card_holder);

            FoodImage_Id = itemView.findViewById(R.id.FoodImage_Id);

            History_Id = itemView.findViewById(R.id.History_Id);

        }
    }
}
