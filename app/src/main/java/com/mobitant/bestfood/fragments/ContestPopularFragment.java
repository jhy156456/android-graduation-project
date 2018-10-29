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
import android.widget.Toast;

import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.adapter.RecycleAdapteProductGrid;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.ProductGridModellClass;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContestPopularFragment extends Fragment {

    private ArrayList<ProductGridModellClass> productGridModellClasses;
    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView recyclerview;
    private RecycleAdapteProductGrid mAdapter2;


    int memberSeq;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contest_fragment_popular, container, false);


        memberSeq = ((MyApp)this.getActivity().getApplication()).getMemberSeq();
        listInfo(memberSeq, "reg_date", 0,1001);
        //위의 reg_date는 조회순정렬을 만들고 바꿔주자

        recyclerview = (RecyclerView)view.findViewById(R.id.recyclerview);
/*
        productGridModellClasses = new ArrayList<>();

        for (int i = 0; i < image1.length; i++) {
            ProductGridModellClass beanClassForRecyclerView_contacts = new ProductGridModellClass(image1[i]);
            productGridModellClasses.add(beanClassForRecyclerView_contacts);
        }

        mAdapter2 = new RecycleAdapteProductGrid(getActivity(),productGridModellClasses);
        */
        mAdapter2 = new RecycleAdapteProductGrid(getActivity(),new ArrayList<FoodInfoItem>());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter2);


        return view;
    }

    /**
     * 서버에서 맛집 정보를 조회한다.
     * @param memberSeq 사용자 시퀀스
     * @param orderType 맛집 정보 정렬 순서
     * @param currentPage 현재 페이지
     */
    private void listInfo(int memberSeq,String orderType, final int currentPage,int from) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<FoodInfoItem>> call = remoteService.listFoodInfo(memberSeq,orderType, currentPage,from);
        call.enqueue(new Callback<ArrayList<FoodInfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<FoodInfoItem>> call,
                                   Response<ArrayList<FoodInfoItem>> response) {
                ArrayList<FoodInfoItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    mAdapter2.addItemList(list);

                    if (mAdapter2.getItemCount() == 0) {
                        Toast.makeText(getContext(),"데이터 없다",Toast.LENGTH_LONG).show();
                    } else {
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FoodInfoItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }
}
