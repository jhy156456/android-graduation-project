package com.mobitant.bestfood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.ChatContentsItem;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyEditText;
import customfonts.MyTextView_Roboto_Regular;


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

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ChatContentsItem message = mMessages.get(position);

        if (message.getType() == 0) {//보낸메세지
            viewHolder.setSendMessage(message.getChat());
        } else {
            viewHolder.setReceiveMessage(message.getChat());
            viewHolder.setUsername(message.getUser());
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (mMessages.get(position).getSender().equals("asdf")){//본인이 보낸 메세지
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

        public ViewHolder(View itemView) {
            super(itemView);

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
