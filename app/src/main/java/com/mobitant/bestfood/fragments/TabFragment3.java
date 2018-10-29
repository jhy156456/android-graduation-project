package com.mobitant.bestfood.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.SoftwareBuyActivity;
import com.mobitant.bestfood.adapter.JayBaseAdapter;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.OrderCheckItem;
import com.mobitant.bestfood.item.OrderItem;
import com.mobitant.bestfood.lib.BaseFragment;
import com.mobitant.bestfood.lib.GoLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.item.User;
import com.mobitant.bestfood.lib.MyToast;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import customfonts.MyTextView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabFragment3 extends BaseFragment implements View.OnClickListener {


    private final String TAG = this.getClass().getSimpleName();
    private View view;
    public OrderItem orderItem;
    private ListView listview;
    OrderCheckItem orderCheckItem; //어댑터에서 전달받은 객체
    Typeface fonts1, fonts2;
    User buyerUser;
    MyTextView textView;
    private ArrayList<Bean> Bean;
    private JayBaseAdapter baseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.buy_fragmenttab3, container, false);
        orderItem = new OrderItem();
        listview = (ListView) view.findViewById(R.id.listview);
        buyerUser = ((MyApp) getActivity().getApplicationContext()).getUserItem();
        textView = view.findViewById(R.id.order_pay);
        textView.setOnClickListener(this);
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
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.order_pay) {
            progressON("저장중...");
            orderCheckItem.setBuyerMemberSeq(buyerUser.seq);
            orderCheckItem.setCardHorder(orderItem.getCard_holder());
            orderCheckItem.setBuyerMemberNickName(orderItem.getBuyer_nickname());
            save();
            this.getActivity().finish();
            progressOFF();
        }
    }

    /**
     * 사용자가 입력한 정보를 서버에 저장한다.
     */
    private void save() {


        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertOrderCheckItem(orderCheckItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    progressOFF();
                    MyToast.s(getContext(),"구매신청완료");
                } else { // 등록 실패
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    MyLog.d(TAG, "fail " + statusCode + errorBody.toString());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }

}