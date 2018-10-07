package com.mobitant.bestfood.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitant.bestfood.R;
import com.mobitant.bestfood.SoftwareBuyActivity;
import com.mobitant.bestfood.item.OrderItem;
import com.mobitant.bestfood.lib.MyLog;

/**
 * Created by apple on 18/03/16.
 */
public class TabFragment2 extends Fragment implements View.OnClickListener {
TextView textView;
OrderItem orderItem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //orderItem = (OrderItem) getArguments().getSerializable("orderItem");
        View layout = inflater.inflate(R.layout.buy_fragmenttab2, container, false);
        return layout;
    }
/*
이걸 해야 뉴인스턴스가 가능하고 안에 데이터를 넘겨줄 수 있다..... 원리가 뭐지?
 */
    public static TabFragment2 newInstance() {
        TabFragment2 f = new TabFragment2();
        return f;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.order_stage2);

//        MyLog.d("가져온것 : " + orderItem.getBuyer_nickname());
  //      Toast.makeText(getActivity(), "가져온것 : " + orderItem.getBuyer_nickname(), Toast.LENGTH_SHORT).show();
        textView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.order_stage2){
            SoftwareBuyActivity.viewPager.setCurrentItem(2);
        }
    }
}