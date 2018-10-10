package com.mobitant.bestfood.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.mobitant.bestfood.fragments.TabFragment1;
import com.mobitant.bestfood.fragments.TabFragment2;
import com.mobitant.bestfood.fragments.TabFragment3;
import com.mobitant.bestfood.item.OrderCheckItem;
import com.mobitant.bestfood.item.OrderItem;

public class PagerAdapter1 extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    SparseArray<Fragment> registeredFragments = new SparseArray<>();
    OrderCheckItem orderCheckItem;
    public PagerAdapter1(FragmentManager fm, int NumOfTabs,
                         OrderCheckItem orderCheckItem) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.orderCheckItem = new OrderCheckItem();
        this.orderCheckItem = orderCheckItem;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();

                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderCheckItem",orderCheckItem);
                tab3.setArguments(bundle);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
