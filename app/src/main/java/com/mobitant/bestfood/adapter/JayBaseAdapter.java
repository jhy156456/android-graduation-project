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
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class JayBaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<Bean> bean;
    Typeface fonts1, fonts2;

    public JayBaseAdapter(Context context, ArrayList<Bean> bean) {
        this.context = context;
        this.bean = bean;
    }
public void DataChange(Bean bean,int index){
        this.bean.set(index,bean);
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

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.list_title);
            viewHolder.discription = (TextView) convertView.findViewById(R.id.list_description);
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



        if(position ==0) { //게시글 이미지 띄워주기 위함 IMAGE_URL ,MEMBER_ICON_URL 이 다름
            if (StringLib.getInstance().isBlank(bean.getImageFileName())) {
                Picasso.with(context).load(R.drawable.bg_bestfood_drawer).into(viewHolder.imageView);
            } else {
                Picasso.with(context).load(RemoteService.IMAGE_URL + bean.getImageFileName()).into(viewHolder.imageView);
            }
        }
        else { // 멤버 프로필 띄워주기 위함
            if (StringLib.getInstance().isBlank(bean.getImageFileName())) {
                Picasso.with(context).load(R.drawable.bg_bestfood_drawer).into(viewHolder.imageView);
            } else {
                Picasso.with(context).load(RemoteService.MEMBER_ICON_URL + bean.getImageFileName()).into(viewHolder.imageView);
            }
        }
        if(position ==3){//카드번호 아이템일경우 텍스트설정
            viewHolder.title.setText("카드번호 : "+bean.getTitle());
            viewHolder.discription.setText("소유주 : "+bean.getDiscription());
        }else{
            viewHolder.title.setText(bean.getTitle());
            viewHolder.discription.setText(bean.getDiscription());
        }

        viewHolder.postOS.setText(bean.getDate());

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView title;
        TextView discription;
        TextView postOS;

    }
}




