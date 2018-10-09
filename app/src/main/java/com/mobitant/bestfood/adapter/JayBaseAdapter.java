package com.mobitant.bestfood.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobitant.bestfood.R;
import com.mobitant.bestfood.fragments.Bean;

import java.util.ArrayList;


public class JayBaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<Bean> bean;
    Typeface fonts1, fonts2;

    public JayBaseAdapter(Context context, ArrayList<Bean> bean) {
        this.context = context;
        this.bean = bean;
    }

    @Override
    public int getCount() {
        return bean.size();
    }

    @Override
    public Object getItem(int position) {
        return bean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        fonts1 = Typeface.createFromAsset(context.getAssets(),
                "fonts/MavenPro-Regular.ttf");

//        fonts2 = Typeface.createFromAsset(context.getAssets(),
//                "fonts/Lato-Regular.ttf");

        ViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list, null);

            viewHolder = new ViewHolder();

            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.discription = (TextView) convertView.findViewById(R.id.description);
            viewHolder.postOS = (TextView) convertView.findViewById(R.id.post_os);

            //viewHolder.text = (TextView)convertView.findViewById(R.id.text);


            //폰트입히기
            viewHolder.title.setTypeface(fonts1);
            viewHolder.discription.setTypeface(fonts1);
            // viewHolder.text.setTypeface(fonts1);
            viewHolder.postOS.setTypeface(fonts1);
            convertView.setTag(viewHolder);
//폰트입히기끝

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        Bean bean = (Bean) getItem(position);

        viewHolder.image.setImageResource(bean.getImage());
        viewHolder.title.setText(bean.getTitle());
        viewHolder.discription.setText(bean.getDiscription());
        viewHolder.postOS.setText(bean.getDate());

        return convertView;
    }

    private class ViewHolder {
        ImageView image;
        TextView title;
        TextView discription;
        TextView postOS;
        ImageView min;
        TextView text;
        ImageView plus;

    }
}




