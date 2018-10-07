package com.mobitant.bestfood.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobitant.bestfood.R;
import com.mobitant.bestfood.adapter.RecycleAdapteProductGrid;
import com.mobitant.bestfood.item.ProductGridModellClass;

import java.util.ArrayList;

public class ContestRecentFragment extends Fragment {
    private ArrayList<ProductGridModellClass> productGridModellClasses;
    private RecyclerView recyclerview;
    private RecycleAdapteProductGrid mAdapter2;
    private Integer image1[]={R.drawable.iphnx,R.drawable.iphnx,R.drawable.iphnx,R.drawable.iphnx,R.drawable.iphnx,R.drawable.iphnx};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contest_fragment_popular, container, false);


        //      New Arrival Kitchen  Recyclerview Code is here

        recyclerview = (RecyclerView)view.findViewById(R.id.recyclerview);

        productGridModellClasses = new ArrayList<>();



        for (int i = 0; i < image1.length; i++) {
            ProductGridModellClass beanClassForRecyclerView_contacts = new ProductGridModellClass(image1[i]);
            productGridModellClasses.add(beanClassForRecyclerView_contacts);
        }



        mAdapter2 = new RecycleAdapteProductGrid(getActivity(),productGridModellClasses);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter2);




        return view;
    }
}
