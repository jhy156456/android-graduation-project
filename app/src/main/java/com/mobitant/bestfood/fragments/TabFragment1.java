package com.mobitant.bestfood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.SoftwareBuyActivity;
import com.mobitant.bestfood.item.OrderItem;
import com.mobitant.bestfood.model.User;

import customfonts.MyEditText;

/**
 * Created by apple on 18/03/16.
 */
public class TabFragment1 extends Fragment implements View.OnClickListener {
TextView textView;
MyEditText nickName,name,phoneNumber,email;


public static OrderItem fragment1OrderItem;
User currentUser;


/*
   public static TabFragment1 newInstance(OrderItem fragment1OrderItem) {
       TabFragment1 fragment = new TabFragment1();
       Bundle cBundle = new Bundle();

       cBundle.putSerializable("fragment1OrderItem", fragment1OrderItem);
       fragment.setArguments(cBundle);
       return fragment;
   }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       currentUser =  ((MyApp)getActivity().getApplication()).getUserItem();


       fragment1OrderItem = new OrderItem();//안하면 여기에쓸때 널값에러났음


        View layout = inflater.inflate(R.layout.buy_fragmenttab1, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.order_stage1);
        nickName = view.findViewById(R.id.nickname2);
        name = view.findViewById(R.id.name2);
        phoneNumber = view.findViewById(R.id.phonenumber2);
        email = view.findViewById(R.id.email2);

        setView();

        setBuyerInfo();
        textView.setOnClickListener(this);
    }
    public void setView(){
       nickName.setText(currentUser.nickname);
       name.setText(currentUser.name);
       phoneNumber.setText(currentUser.phone);
       email.setText(currentUser.getEmail());
    }

    public void setBuyerInfo(){
       fragment1OrderItem.setBuyer_nickname(nickName.getText().toString());
       fragment1OrderItem.setBuyer_phone(phoneNumber.getText().toString());
       fragment1OrderItem.setRealName(name.getText().toString());
    }


    @Override //버튼으로 프래그먼트 전환
    public void onClick(View v) {
        setBuyerInfo();

        callback.setValue(fragment1OrderItem);
        if(v.getId()==R.id.order_stage1){
            SoftwareBuyActivity.viewPager.setCurrentItem(1);
        }
    }



    public static interface sendValue {
        public void setValue(OrderItem fragment1OrderItem);
    }
    public sendValue callback;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof sendValue) {
            callback = (sendValue) context;
        }
    }




}