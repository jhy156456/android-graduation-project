package com.mobitant.bestfood.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.adapter.JayBaseAdapter;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.OrderCheckItem;
import com.mobitant.bestfood.item.OrderItem;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.item.User;

import java.util.ArrayList;

public class TabFragment3 extends Fragment {


    private View view;
    public OrderItem orderItem;
    private ListView listview;
    OrderCheckItem orderCheckItem; //어댑터에서 전달받은 객체
    Typeface fonts1, fonts2;
    User buyerUser;

    /*
    private int[] IMAGE = {R.drawable.box, R.drawable.ball, R.drawable.bag,
            R.drawable.box, R.drawable.ball};
    private String[] TITLE = {"Teak & Steel Petanque Set", "Lemon Peel Baseball", "Seil Marschall Hiking Pack", "Teak & Steel Petanque Set", "Lemon Peel Baseball"};
    private String[] DESCRIPTION = {"One Size", "One Size", "Size L", "One Size", "One Size"};
    private String[] DATE = {"$ 220.00","$ 49.00","$ 320.00","$ 220.00","$ 49.00"};
*/


    private ArrayList<Bean> Bean;
    private JayBaseAdapter baseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.buy_fragmenttab3, container, false);
        orderItem = new OrderItem();
        listview = (ListView) view.findViewById(R.id.listview);
        buyerUser = ((MyApp) getActivity().getApplicationContext()).getUserItem();


        orderCheckItem = (OrderCheckItem) getArguments().getSerializable("orderCheckItem");
        Bean = new ArrayList<Bean>();

//구매아이템
        Bean bean = new Bean(orderCheckItem.getInfoFirstImageFilename(), orderCheckItem.getInfoTitle()
                , orderCheckItem.getInfoContent(), orderCheckItem.getPostRegisterDate());
        Bean.add(bean);
//판매자정보
        bean = new Bean(orderCheckItem.getPostMemberIconFilename(), orderCheckItem.getPostNickName(),
                orderCheckItem.getPostRealName(), orderCheckItem.getPostPhoneNumber());
        Bean.add(bean);
//구매자정보
        bean = new Bean(buyerUser.memberIconFilename,buyerUser.nickname,
                buyerUser.name,buyerUser.phone);
        Bean.add(bean);

        //구매자 카드번호
        bean = new Bean("",orderItem.getCard_number(),
                orderItem.getCard_holder(),orderItem.getExp_date());

        Bean.add(bean);


        baseAdapter = new JayBaseAdapter(getActivity(), Bean) {
        };

        listview.setAdapter(baseAdapter);

        return view;
    }


/*
public TabFragment3(String sellerNickName, String sellerPostMemberIconFilename){
        this.sellerNickName = sellerNickName;
        this.sellerPostMemberIconFilename = sellerPostMemberIconFilename;
}
*/

    public static TabFragment3 newInstance() {
        TabFragment3 f = new TabFragment3();
        return f;
    }

    public void setset(OrderItem orderItem) {

        this.orderItem = orderItem;

        Bean bean;
        bean = new Bean("",this.orderItem.getCard_number(),
                this.orderItem.getCard_holder(),this.orderItem.getExp_date());

        baseAdapter.DataChange(bean,3);
        baseAdapter.notifyDataSetChanged();


        //cardHolder.setText(mOrderItem.getBuyer_nickname());
    }

}