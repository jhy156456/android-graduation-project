package com.mobitant.bestfood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitant.bestfood.R;
import com.mobitant.bestfood.SoftwareBuyActivity;
import com.mobitant.bestfood.adapter.ItemData;
import com.mobitant.bestfood.adapter.ItemDataClass;
import com.mobitant.bestfood.adapter.SpinnerClassAdapter;
import com.mobitant.bestfood.adapter.SpinnerCousineAdapter;
import com.mobitant.bestfood.item.OrderItem;
import com.mobitant.bestfood.lib.MyLog;

import java.util.ArrayList;

import customfonts.EditText_Roboto_Regular;
import customfonts.MyEditText;

/**
 * Created by apple on 18/03/16.
 */
public class TabFragment2 extends Fragment implements View.OnClickListener {
    TextView textView;
    OrderItem mOrderItem;
    EditText_Roboto_Regular cardHolder, cardNumber;
    String dateMonth="";
    String dateYear="";
    String date;
    public static OrderItem fragment2OrderItem;
    Spinner sp, spinner;
    ArrayList<ItemData> monthList;
    ArrayList<ItemDataClass> yearList;
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
        sp = (Spinner) view.findViewById(R.id.spinner_month);
        spinner = (Spinner) view.findViewById(R.id.spinner_year);
        fragment2OrderItem = new OrderItem();
        setSpnnier();

        textView = view.findViewById(R.id.order_stage2);
        mOrderItem = new OrderItem();
        cardHolder = (EditText_Roboto_Regular) view.findViewById(R.id.card_holder);
        //cardDate = view.findViewById(R.id.date2);
        cardNumber = (EditText_Roboto_Regular)view.findViewById(R.id.cardno2);
        textView.setOnClickListener(this);

    }

    private void setSpnnier() {
        monthList = new ArrayList<>();
        monthList.add(new ItemData("1월"));
        monthList.add(new ItemData("2월"));
        monthList.add(new ItemData("3월"));
        monthList.add(new ItemData("4월"));
        monthList.add(new ItemData("5월"));
        monthList.add(new ItemData("6월"));
        monthList.add(new ItemData("7월"));
        monthList.add(new ItemData("8월"));
        monthList.add(new ItemData("9월"));
        monthList.add(new ItemData("10월"));
        monthList.add(new ItemData("11월"));
        monthList.add(new ItemData("12월"));

        SpinnerCousineAdapter adapter = new SpinnerCousineAdapter(this.getActivity(), R.layout.spinner_selecting_adults, R.id.spinner_data, monthList);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateMonth = monthList.get(position).getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        yearList = new ArrayList<>();
        yearList.add(new ItemDataClass("2017"));
        yearList.add(new ItemDataClass("2018"));
        yearList.add(new ItemDataClass("2019"));
        yearList.add(new ItemDataClass("2020"));
        yearList.add(new ItemDataClass("2021"));
        yearList.add(new ItemDataClass("2022"));
        yearList.add(new ItemDataClass("2023"));
        yearList.add(new ItemDataClass("2024"));
        yearList.add(new ItemDataClass("2025"));
        yearList.add(new ItemDataClass("2026"));
        yearList.add(new ItemDataClass("2027"));
        yearList.add(new ItemDataClass("2028"));
        SpinnerClassAdapter adapters = new SpinnerClassAdapter(this.getActivity(), R.layout.spinner_selecting_adults, R.id.spinner_data, yearList);
        spinner.setAdapter(adapters);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateYear = yearList.get(position).getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setBuyerInfo() {
        fragment2OrderItem.setCard_holder(cardHolder.getText().toString());
        fragment2OrderItem.setCard_number(cardNumber.getText().toString());
        fragment2OrderItem.setExp_date(dateMonth+" "+dateYear+"년");
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