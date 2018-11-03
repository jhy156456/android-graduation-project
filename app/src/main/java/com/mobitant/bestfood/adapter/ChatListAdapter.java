package com.mobitant.bestfood.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.ChatTalkData;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by one on 28/7/16.
 */
public class ChatListAdapter extends ArrayAdapter<ChatTalkData> {

    Activity context;
    int resource, textViewResourceId;
    List<ChatTalkData> items, tempItems, suggestions;
    private Dialog pinDialog;
    private EditText popup_title, popup_review;
    private TextView submit;
    private RatingBar rating;
    Integer[] imageId = {
            R.drawable.circle_images,
            R.drawable.circle_images,
            R.drawable.circle_images


    };


    public ChatListAdapter(Activity mainActivity, ArrayList<ChatTalkData> dataArrayList) {
        super(mainActivity, 0, dataArrayList);

        this.context = mainActivity;
        this.items = dataArrayList;
    }


    private class ViewHolder {

        TextView description, name;
        ImageView image;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatListAdapter.ViewHolder holder = null;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(
                    R.layout.supporters_chat_talk_list_item, parent, false);

            holder = new ChatListAdapter.ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(holder);

        } else {
            holder = (ChatListAdapter.ViewHolder) convertView.getTag();
        }

        ChatTalkData productItems = items.get(position);


        holder.name.setText(productItems.getName());
        holder.description.setText(productItems.getDescription());

        holder.image.setImageResource(imageId[0]);

        return convertView;

    }


}