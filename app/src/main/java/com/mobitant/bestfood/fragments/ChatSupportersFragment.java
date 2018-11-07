package com.mobitant.bestfood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobitant.bestfood.R;
import com.mobitant.bestfood.adapter.InfoListAdapter;
import com.mobitant.bestfood.adapter.SupportersChatAdapter;
import com.mobitant.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.User;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatSupportersFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    public int mPageNo;
    SupportersChatAdapter supportersChatAdapter;
    LinearLayoutManager layoutManager;
    RecyclerView supportersList;
    EndlessRecyclerViewScrollListener scrollListener;

    public ChatSupportersFragment() {
    }

    public static ChatSupportersFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        ChatSupportersFragment fragment = new ChatSupportersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.supporters_fragment_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        supportersList = (RecyclerView)view.findViewById(R.id.fragment_second_recyclerview);
        setRecyclerView();

        listSupporters("Supporters",0);

    }
    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(this.getActivity());
        supportersList.setLayoutManager(layoutManager);


        supportersChatAdapter = new SupportersChatAdapter(this.getActivity(),
                R.layout.supporters_fragment_second_item, new ArrayList<User>());
        supportersList.setAdapter(supportersChatAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listSupporters("Supporters",page);
            }
        };
        supportersList.addOnScrollListener(scrollListener);
    }
    /**

     */
    private void listSupporters(String userType,int page) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<User>> call = remoteService.listSupporters(userType,page);
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call,
                                   Response<ArrayList<User>> response) {

                ArrayList<User> list = response.body();
                if (response.isSuccessful() && list != null) {
                    supportersChatAdapter.addItemList(list);

                   /* if (supportersChatAdapter.getItemCount() == 0) {
                        noDataText.setVisibility(View.VISIBLE);
                    } else {
                        noDataText.setVisibility(View.GONE);
                    }*/
                }
            }
            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
