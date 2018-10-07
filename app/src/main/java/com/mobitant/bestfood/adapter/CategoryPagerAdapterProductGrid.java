package com.mobitant.bestfood.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mobitant.bestfood.fragments.ContestHitsFragment;
import com.mobitant.bestfood.fragments.ContestPopularFragment;
import com.mobitant.bestfood.fragments.ContestRecentFragment;


/**
 * Created by wolfsoft5 on 7/4/18.
 */

public class CategoryPagerAdapterProductGrid extends FragmentPagerAdapter {

        int mNoOfTabs;

        public CategoryPagerAdapterProductGrid(FragmentManager fm, int NumberOfTabs)

        {
                super(fm);
                this.mNoOfTabs = NumberOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
                switch (position) {

                        case 0:
                                ContestPopularFragment tab1 = new ContestPopularFragment();
                                return tab1;
                        case 1:
                                ContestHitsFragment tab2 = new ContestHitsFragment();
                                return tab2;
                        case 2:
                                ContestRecentFragment tab3 = new ContestRecentFragment();
                                return tab3;
                        default:
                                return null;
                }
        }

        @Override
        public int getCount() {
                return mNoOfTabs;

        }
}

