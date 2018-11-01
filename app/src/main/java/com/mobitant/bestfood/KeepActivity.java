package com.mobitant.bestfood;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mobitant.bestfood.adapter.KeepListAdapter;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.KeepItem;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeepActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    Context context;
    int memberSeq;
    RecyclerView keepRecyclerView;
    TextView noDataText;
    KeepListAdapter keepListAdapter;
    ArrayList<KeepItem> keepList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_bestfood_keep);
setToolbar();



        keepRecyclerView = (RecyclerView) findViewById(R.id.keep_list);
        noDataText = (TextView) findViewById(R.id.no_keep);

        keepListAdapter = new KeepListAdapter(context,
                R.layout.row_bestfood_keep, keepList, memberSeq);
        StaggeredGridLayoutManager layoutManager
                = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        keepRecyclerView.setLayoutManager(layoutManager);
        keepRecyclerView.setAdapter(keepListAdapter);

        listKeep(memberSeq);
    }
    /**
     * 툴바를 설정한다.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.nav_keep);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem mItem) {
        //원래 MenuItem item 이였는데 지금보고있는 게시물의 변수명인 item과 같아서 오류가났었다
        //그래서 item을 메뉴아이템인 mItem으로 변경했다.
        switch (mItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(mItem);
    }
    @Override
    public void onResume() {
        super.onResume();

        MyApp myApp = ((MyApp)getApplication());
        FoodInfoItem currentInfoItem = myApp.getFoodInfoItem();

        if (keepListAdapter != null && currentInfoItem != null) {
            keepListAdapter.setItem(currentInfoItem);
            myApp.setFoodInfoItem(null);

            if (keepListAdapter.getItemCount() == 0) {
                noDataText.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 서버에서 즐겨찾기한 맛집 정보를 조회한다.
     * @param memberSeq 사용자 시퀀스
     */
    private void listKeep(int memberSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<KeepItem>> call
                = remoteService.listKeep(memberSeq);
        call.enqueue(new Callback<ArrayList<KeepItem>>() {
            @Override
            public void onResponse(Call<ArrayList<KeepItem>> call,
                                   Response<ArrayList<KeepItem>> response) {
                ArrayList<KeepItem> list = response.body();

                if (list == null) {
                    list = new ArrayList<>();
                }

                noDataText.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    MyLog.d(TAG, "list size " + list.size());
                    if (list.size() == 0) {
                        noDataText.setVisibility(View.VISIBLE);
                    } else {
                        keepListAdapter.setItemList(list);
                    }
                } else {
                    MyLog.d(TAG, "not success");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<KeepItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }
}
