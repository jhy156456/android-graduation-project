package com.mobitant.bestfood.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.mobitant.bestfood.R;

import com.mobitant.bestfood.item.ImageItem;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class InfoImageAdapter  extends RecyclerView.Adapter<InfoImageAdapter.ViewHolder>{
    private final String TAG = this.getClass().getSimpleName();
    private ArrayList<ImageItem> imageList = new ArrayList<>();
    private Context context;
    private int resource;

    /**
     * 어댑터 생성자
     * @param context 컨텍스트 객체
     * @param resource 아이템을 보여주기 위해 사용할 리소스 아이디
     * @param imageList 아이템 리스트
     */

    public InfoImageAdapter(Context context, int resource, ArrayList<ImageItem> imageList) {
        this.context = context;
        this.resource = resource;
        this.imageList = imageList;

    }
    /**
     * 특정 아이템의 변경사항을 적용하기 위해 기본 아이템을 새로운 아이템으로 변경한다.
     * @param newItem 새로운 아이템
     */
    public void setItem(ImageItem newItem) {
        for (int i=0; i < imageList.size(); i++) {
            ImageItem item = imageList.get(i);

            if (item.seq == newItem.seq) {
                imageList.set(i, newItem);
                notifyItemChanged(i);
                break;
            }
        }
    }

    /**
     * 현재 아이템 리스트에 새로운 아이템 리스트를 추가한다.
     * @param imageList 새로운 아이템 리스트
     */
    public void addItemList(ArrayList<ImageItem> imageList) {
        this.imageList.addAll(imageList);
        notifyDataSetChanged();
    }


    /**
     * 뷰홀더(ViewHolder)를 생성하기 위해 자동으로 호출된다.
     * @param parent 부모 뷰그룹
     * @param viewType 새로운 뷰의 뷰타입
     * @return 뷰홀더 객체
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull InfoImageAdapter.ViewHolder holder, int position) {
        final ImageItem item = imageList.get(position);
        MyLog.d(TAG, "getView " + item);
        setImage(holder.image, item.fileName);
    }

    @Override
    public int getItemCount() {
        return this.imageList.size();
    }

    /**
     * 이미지를 설정한다.
     * @param imageView  이미지를 설정할 뷰
     * @param fileName 이미지 파일이름
     */
    private void setImage(ImageView imageView, String fileName) {
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.with(context).load(R.drawable.bg_bestfood_drawer).into(imageView);
        } else {
            Picasso.with(context).load(RemoteService.IMAGE_URL + fileName).into(imageView);
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.post_image);
        }
    }
}
