package com.mobitant.bestfood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import customfonts.MyEditText;

/**
 * Created by apple on 18/03/16.
 */
public class TabFragment2 extends Fragment implements View.OnClickListener {
    TextView textView;
    OrderItem mOrderItem;
    MyEditText cardHolder,cardNumber,cardDate;
    public static OrderItem fragment2OrderItem;

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
        fragment2OrderItem = new OrderItem();
        textView = view.findViewById(R.id.order_stage2);
        mOrderItem = new OrderItem();
        cardHolder = view.findViewById(R.id.card_holder);
        cardDate = view.findViewById(R.id.date2);
cardNumber = view.findViewById(R.id.cardno2);
        textView.setOnClickListener(this);

    }

public void setBuyerInfo(){
fragment2OrderItem.setCard_holder(cardHolder.getText().toString());
fragment2OrderItem.setCard_number(cardNumber.getText().toString());
fragment2OrderItem.setExp_date(cardDate.getText().toString());
}

    @Override
    public void onClick(View v) {
        setBuyerInfo();

        callback.setFragment2Value(fragment2OrderItem);
        if (v.getId() == R.id.order_stage2) {
            SoftwareBuyActivity.viewPager.setCurrentItem(2);
        }
    }


    public void setset(OrderItem orderItem) { //SofatwareBuyActivity에서 호출(화면에 띄워진 값 바꿔주기위함)

        this.mOrderItem = orderItem;
        cardHolder.setText(mOrderItem.getBuyer_nickname());
    }

    public static interface sendValue {
        public void setFragment2Value(OrderItem fragment1OrderItem);
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