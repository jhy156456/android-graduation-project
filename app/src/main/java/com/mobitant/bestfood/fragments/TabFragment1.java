package com.mobitant.bestfood.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobitant.bestfood.R;
import com.mobitant.bestfood.SoftwareBuyActivity;

/**
 * Created by apple on 18/03/16.
 */
public class TabFragment1 extends Fragment implements View.OnClickListener {
TextView textView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragmenttab1, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.order_stage1);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.order_stage1){
            SoftwareBuyActivity.viewPager.setCurrentItem(1);
        }
    }

}