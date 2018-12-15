package com.mobitant.bestfood;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mobitant.bestfood.adapter.PagerAdapter1;
import com.mobitant.bestfood.fragments.TabFragment1;
import com.mobitant.bestfood.fragments.TabFragment2;
import com.mobitant.bestfood.fragments.TabFragment3;
import com.mobitant.bestfood.item.OrderCheckItem;
import com.mobitant.bestfood.item.OrderItem;
import com.mobitant.bestfood.lib.MyLog;

public class SoftwareBuyActivity extends AppCompatActivity implements TabFragment1.sendValue,TabFragment2.sendValue {
    private OrderItem mOrderItem;
    public static Context mContext;
    public static ViewPager viewPager;
    public TabFragment2 tabFragment2;
    public TabFragment1 tabFragment1;
    public TabFragment3 tabFragment3;
    PagerAdapter1 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.buy_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mOrderItem = new OrderItem();

        getSellerInfo();
//BestfoodInfoActivity 에서 판매자 정보 가져오기 위함
        OrderCheckItem orderCheckItem = new OrderCheckItem();
        orderCheckItem = (OrderCheckItem) getIntent().getSerializableExtra("orderCheckItem");

//판매자 정보 가져오기 위함

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("주문서"));
        tabLayout.addTab(tabLayout.newTab().setText("결제"));
        tabLayout.addTab(tabLayout.newTab().setText("확인"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);

        //어댑터 생성하면서 프래그먼트3에 띄울 화면을 번들로 전달한다.
        adapter = new PagerAdapter1(getSupportFragmentManager(), tabLayout.getTabCount()
                ,orderCheckItem);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);//이게뭐냐
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
/*
                if (tab.getPosition() == 1) {
                    tabFragment2 = (TabFragment2) adapter.getRegisteredFragment(1);
                    tabFragment2.setset(mOrderItem);
                }else
*/
                if(tab.getPosition() ==2){
                    tabFragment3 = (TabFragment3) adapter.getRegisteredFragment(2);
                    tabFragment3.setset(mOrderItem);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //각 프래그먼트 아래버튼을 누르면 값을 넘기기쉬운데
                //슬라이드로 넘겼을때도 값이 넘어가야하므로 탭이 언셀렉티드되면 값을넘기는것도 추가시킨다.

                /*
                if (tab.getPosition() == 0) {
                    tabFragment1 = (TabFragment1) adapter.getRegisteredFragment(0);
                    tabFragment1.setBuyerInfo();
                    tabFragment1.callback.setValue(tabFragment1.fragment1OrderItem);
                } else
                */
                if (tab.getPosition() ==1){
                    tabFragment2 = (TabFragment2)adapter.getRegisteredFragment(1);
                    tabFragment2.setBuyerInfo();
                    tabFragment2.callback.setFragment2Value(tabFragment2.fragment2OrderItem);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });
    }


    public void getSellerInfo() {

    }

    /**
     * 오른쪽 상단 메뉴를 구성한다.
     * 닫기 메뉴만이 설정되어 있는 menu_close.xml를 지정한다.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_close, menu);
        return true;
    }

    /**
     * 왼쪽 화살표 메뉴(android.R.id.home)를 클릭했을 때와
     * 오른쪽 상단 닫기 메뉴를 클릭했을 때의 동작을 지정한다.
     * 여기서는 모든 버튼이 액티비티를 종료한다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setInfo(OrderItem orderItem) {
        mOrderItem = orderItem;
    }

    public OrderItem getmOrderItem() {
        return mOrderItem;
    }


    @Override
    public void setFragment1Value(OrderItem fragment1OrderItem) {
        mOrderItem = fragment1OrderItem;
    }

    @Override
    public void setFragment2Value(OrderItem fragment1OrderItem) {
mOrderItem = fragment1OrderItem;
    }
}