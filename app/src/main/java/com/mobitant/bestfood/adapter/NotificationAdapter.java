package com.mobitant.bestfood.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.fragments.NotificationDetailFragment;
import com.mobitant.bestfood.item.NotificationItem;
import com.mobitant.bestfood.lib.GoLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.item.User;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter <NotificationAdapter.ViewHolder>{
    private Context context;
    private int resource;
    private ArrayList<NotificationItem> itemList;
    User memberInfoItem;
    /**
     * 어댑터 생성자
     * @param context 컨텍스트 객체
     * @param resource 아이템을 보여주기 위해 사용할 리소스 아이디
     * @param itemList 아이템 리스트
     */
    public NotificationAdapter(Context context, int resource, ArrayList<NotificationItem> itemList) {
        this.context = context;
        this.resource = resource;
        this.itemList = itemList;

        memberInfoItem = ((MyApp) context.getApplicationContext()).getUserItem();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    /**
     * 현재 아이템 리스트에 새로운 아이템 리스트를 추가한다.
     * @param itemList 새로운 아이템 리스트
     */
    public void addItemList(ArrayList<NotificationItem> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        NotificationItem item = itemList.get(position);

        holder.textView.setText(item.getTitle());
        holder.created_at.setText(item.getCreate_at());
        MyLog.d("날짜" + item.getCreate_at());
        Bundle bundle = new Bundle();
        bundle.putString("SEQ",item.id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoLib.getInstance().goFragmentDetail(((FragmentActivity)context).getSupportFragmentManager(),
                        R.id.notification_change_fragment, NotificationDetailFragment.newInstance(),bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView created_at;
        public ViewHolder(View itemView) {

            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.notification_title);
            created_at=(TextView)itemView.findViewById(R.id.notification_create_at);
        }
    }
}
