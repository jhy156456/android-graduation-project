package com.mobitant.bestfood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.ChatContentsItem;
import com.mobitant.bestfood.item.ChatTalkData;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyEditText;
import customfonts.MyTextView_Roboto_Regular;
import de.hdodenhof.circleimageview.CircleImageView;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<ChatContentsItem> mMessages;
    private int[] mUsernameColors;
    private Context context;

    public MessageAdapter(Context context, ArrayList<ChatContentsItem> messages) {
        this.context = context;
        mMessages = messages;
        mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        switch (viewType) {
            case ChatContentsItem.TYPE_MESSAGE:
                layout = R.layout.supporters_chat_send_item;
                break;
            case ChatContentsItem.TYPE_RECEIVE:
                layout = R.layout.supporters_chat_receive_item;
                break;
            case ChatContentsItem.TYPE_ACTION:
                layout = R.layout.supporters_chat_receive_item;
                break;
        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    public void addFirstItem(ChatContentsItem chatContentsItem) {
        this.mMessages.add(0, chatContentsItem);
    }

    public void addItem(ChatContentsItem chatContentsItem) {
        this.mMessages.add(chatContentsItem);
    }

    public void addItemList(ArrayList<ChatContentsItem> itemList) {
        this.mMessages.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ChatContentsItem message = mMessages.get(position);

        if (message.getType() == 0) {//보낸메세지
            viewHolder.setSendMessage(message.getChat());
        } else {//받은메세지
            viewHolder.setReceiveMessage(message.getChat());
            viewHolder.setUsername(message.getUser());
            if (StringLib.getInstance().isBlank(message.getSenderMemberIconFileName())) {
                Picasso.with(context).load(R.drawable.ic_person).into(viewHolder.circle_image);
            } else {
                Picasso.with(context)
                        .load(RemoteService.MEMBER_ICON_URL + message.getSenderMemberIconFileName())
                        .into(viewHolder.circle_image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (mMessages.get(position).getSender().equals(((MyApp)context.getApplicationContext()).getMemberNickName())) {//본인이 보낸 메세지
            mMessages.get(position).setType(ChatContentsItem.TYPE_MESSAGE);
        } else {
            mMessages.get(position).setType(ChatContentsItem.TYPE_RECEIVE);
        }
        return mMessages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MyTextView_Roboto_Regular mUsernameView;
        private MyTextView_Roboto_Regular mReceiveMessageView;
        private MyTextView_Roboto_Regular mSendMessageView;
        private CircleImageView circle_image;

        public ViewHolder(View itemView) {
            super(itemView);
            circle_image = (CircleImageView) itemView.findViewById(R.id.circle_image);
            mUsernameView = (MyTextView_Roboto_Regular) itemView.findViewById(R.id.username);
            mReceiveMessageView = (MyTextView_Roboto_Regular) itemView.findViewById(R.id.receive_message);
            mSendMessageView = (MyTextView_Roboto_Regular) itemView.findViewById(R.id.send_message);
        }

        public void setUsername(String username) {
            if (null == mUsernameView) return;
            mUsernameView.setText(username);
            // mUsernameView.setTextColor(getUsernameColor(username));
        }

        public void setSendMessage(String message) {
            if (null == mSendMessageView) return;
            mSendMessageView.setText(message);
        }

        public void setReceiveMessage(String message) {
            if (null == mReceiveMessageView) return;
            mReceiveMessageView.setText(message);
        }

        /*private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }*/
    }
}
