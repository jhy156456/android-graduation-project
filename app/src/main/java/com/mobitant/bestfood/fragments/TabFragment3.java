package com.mobitant.bestfood.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mobitant.bestfood.adapter.JayBaseAdapter;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.OrderItem;

import java.util.ArrayList;

public class TabFragment3 extends Fragment {

    String sellerNickName,sellerPostMemberIconFilename;
    private View view;
    public OrderItem orderItem;
    private ListView listview;

    Typeface fonts1,fonts2;

    private int[] IMAGE = {R.drawable.box, R.drawable.ball, R.drawable.bag,
            R.drawable.box, R.drawable.ball};
    private String[] TITLE = {"Teak & Steel Petanque Set", "Lemon Peel Baseball", "Seil Marschall Hiking Pack", "Teak & Steel Petanque Set", "Lemon Peel Baseball"};
    private String[] DESCRIPTION = {"One Size", "One Size", "Size L", "One Size", "One Size"};
    private String[] DATE = {"$ 220.00","$ 49.00","$ 320.00","$ 220.00","$ 49.00"};

    private ArrayList<Bean> Bean;
    private JayBaseAdapter baseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.buy_fragmenttab3, container, false);
        orderItem = new OrderItem();
        listview = (ListView)view.findViewById(R.id.listview);

        sellerNickName = getArguments().getString("sellerPostNickName");
        sellerPostMemberIconFilename = getArguments().getString("sellerPostMemberIconFilename");


        Bean = new ArrayList<Bean>();

        for (int i= 0; i< TITLE.length; i++){
            Bean bean = new Bean(IMAGE[i], TITLE[i], DESCRIPTION[i], DATE[i]);
            Bean.add(bean);
        }

        baseAdapter = new JayBaseAdapter(getActivity(), Bean) {
        };

        listview.setAdapter(baseAdapter);

        return  view;
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
        //cardHolder.setText(mOrderItem.getBuyer_nickname());
    }

}