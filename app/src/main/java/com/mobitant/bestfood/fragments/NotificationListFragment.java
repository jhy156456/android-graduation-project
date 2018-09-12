package com.mobitant.bestfood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.adapter.NotificationAdapter;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.NotificationItem;

import java.util.ArrayList;

public class NotificationListFragment extends Fragment implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    NotificationAdapter adapter;
    Context context;
    /**
     * BestFoodListFragment 인스턴스를 생성한다.
     * @return BestFoodListFragment 인스턴스
     */
    public static NotificationListFragment newInstance() {
        NotificationListFragment f = new NotificationListFragment();
        return f;
    }

    /**
     * fragment_bestfood_list.xml 기반으로 뷰를 생성한다.
     * @param inflater XML를 객체로 변환하는 LayoutInflater 객체
     * @param container null이 아니라면 부모 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     * @return 생성한 뷰 객체
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();
        View layout = inflater.inflate(R.layout.notification_fragment_content_list, container, false);
        return layout;
    }
    /**
     * onCreateView() 메소드 뒤에 호출되며 화면 뷰들을 설정한다.
     * @param view onCreateView() 메소드에 의해 반환된 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.notification_list);
        setRecyclerView();

    }

    public void setRecyclerView(){

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NotificationAdapter(context,
                R.layout.notification_item, new ArrayList<NotificationItem>());
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {

    }
}
