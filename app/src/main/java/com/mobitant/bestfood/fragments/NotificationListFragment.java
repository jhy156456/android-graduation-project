package com.mobitant.bestfood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.adapter.NotificationAdapter;
import com.mobitant.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.NotificationItem;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListFragment extends android.app.Fragment implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    NotificationAdapter notificationAdapter;
    Context context;
    TextView noDataText;
    EndlessRecyclerViewScrollListener scrollListener;
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

    @Override
    public void onResume() {
        super.onResume();
        MyApp myApp = ((MyApp) getActivity().getApplication());
        if(myApp.getIsNewBestfood() == true){
            setRecyclerView();
            listInfo(0);
            myApp.setIsNewNotification(false);
        }
    }

    /**
     * onCreateView() 메소드 뒤에 호출되며 화면 뷰들을 설정한다.
     * @param view onCreateView() 메소드에 의해 반환된 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        noDataText = (TextView) view.findViewById(R.id.no_noti);
        recyclerView = (RecyclerView) view.findViewById(R.id.notification_list);

        setRecyclerView();
        listInfo(0);
    }
    /**
     * 서버에서 맛집 정보를 조회한다.
     * @param currentPage 현재 페이지
     */
    private void listInfo(final int currentPage) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<NotificationItem>> call = remoteService.listNotificationQuestionList(currentPage);
        //여기서 writer가 객체라서 NotificationItem에서 객체끼리 연결을해줘야 정상진행되는듯합니다^^
        call.enqueue(new Callback<ArrayList<NotificationItem>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationItem>> call,
                                   Response<ArrayList<NotificationItem>> response) {
                ArrayList<NotificationItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    notificationAdapter.addItemList(list);
                    if (notificationAdapter.getItemCount() == 0) {
                        noDataText.setVisibility(View.VISIBLE);
                    } else {
                        noDataText.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }
    public void setRecyclerView(){

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        notificationAdapter = new NotificationAdapter(context,
                R.layout.notification_item, new ArrayList<NotificationItem>());
        recyclerView.setAdapter(notificationAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listInfo(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

    }
    @Override
    public void onClick(View v) {

    }
}
