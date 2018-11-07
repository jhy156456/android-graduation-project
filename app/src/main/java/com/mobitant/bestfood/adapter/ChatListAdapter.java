package com.mobitant.bestfood.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobitant.bestfood.ChatTalkContentsActivity;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.TextViewImmacBytes;
import com.mobitant.bestfood.item.ChatTalkData;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.lib.MyLog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by one on 28/7/16.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    Context context;
    ArrayList<ChatTalkData> items;
    Integer[] imageId = {
            R.drawable.circle_images,
            R.drawable.circle_images,
            R.drawable.circle_images
    };

    public ChatListAdapter(Context context, ArrayList<ChatTalkData> dataArrayList) {

        this.context = context;
        this.items = dataArrayList;
    }


    /**
     * 현재 아이템 리스트에 새로운 아이템 리스트를 추가한다.
     *
     * @param itemList 새로운 아이템 리스트
     */
    public void addItemList(ArrayList<ChatTalkData> itemList) {
        MyLog.d("에드아이템리스트 : " + itemList);
        this.items.addAll(itemList);
        notifyDataSetChanged();
    }
    public void addFirst(ChatTalkData chatTalkData){
        this.items.add(0,chatTalkData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.supporters_chat_activity_delete_talk_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatTalkData productItems = items.get(position);
        MyLog.d("프로덕트아이템즈 : " + productItems);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatTalkData chatTalkData = (ChatTalkData) items.get(position);
                Intent intent = new Intent(context, ChatTalkContentsActivity.class);
                intent.putExtra("roomId",chatTalkData.id);
                intent.putExtra("owner", chatTalkData.getOwner()); //흠 이렇게해도 되는건가.. 아닌것같다
                intent.putExtra("participant", chatTalkData.getParticipant());
                intent.putExtra("callActivity", "ChatTalkFragment");

                //멤버의 프로필을 보려면 그사람의 seq를 조회하고 프로필화면으로 들어갔을때
                //그사람의 전체게시글,닉네임,설명 등을 확인해야할듯!!
                //추가하자!
                context.startActivity(intent);
            }
        });
        MyLog.d("어디가먼저?");
        holder.name.setText(productItems.getOwner());
        holder.description.setText(productItems.getParticipant());
        holder.image.setImageResource(imageId[0]);

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView description, name;
        ImageView image;

        public ViewHolder(View itemView) {

            super(itemView);
            name = (TextView) itemView.findViewById(R.id.card_name);
            description = (TextView) itemView.findViewById(R.id.card_description);
            image = (ImageView) itemView.findViewById(R.id.card_image);

        }
    }
}