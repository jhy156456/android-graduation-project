package com.mobitant.bestfood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitant.bestfood.Constant;
import com.mobitant.bestfood.MainActivity;
import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.adapter.InfoListAdapter;
import com.mobitant.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.lib.DialogLib;
import com.mobitant.bestfood.lib.GoLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import customfonts.EditText_Roboto_Regular;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 맛집 정보 리스트를 보여주는 프래그먼트
 */
public class BestFoodListFragment extends Fragment implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    public static final int fromBestFoodListFragment = 1002;
    Context context;
    int memberSeq;
    RecyclerView bestFoodList;
    TextView noDataText;
    TextView orderRecent;
    TextView orderFavorite;
    TextView orderHits;
    TextView inputPost;
    ImageView searchKeyWord;
    EditText_Roboto_Regular searchKey;
    String keyWord;
    InfoListAdapter infoListAdapter;
    LinearLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;
    int listTypeValue = 1; //아마 2가 격자형식이였을듯?
    String orderType;
    /**
     * BestFoodListFragment 인스턴스를 생성한다.
     * @return BestFoodListFragment 인스턴스
     */
    public static BestFoodListFragment newInstance() {
        BestFoodListFragment f = new BestFoodListFragment();
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
        memberSeq = ((MyApp)this.getActivity().getApplication()).getMemberSeq();
        keyWord = "";
        View layout = inflater.inflate(R.layout.fragment_bestfood_list, container, false);
        return layout;
    }

    /**
     * 프래그먼트가 일시 중지 상태가 되었다가 다시 보여질 때 호출된다.
     * BestFoodInfoActivity가 실행된 후,
     * 즐겨찾기 상태가 변경되었을 경우 이를 반영하는 용도로 사용한다.
     */
    @Override
    public void onResume() {
        super.onResume();

        MyApp myApp = ((MyApp) getActivity().getApplication());
        FoodInfoItem currentInfoItem = myApp.getFoodInfoItem();

        if (infoListAdapter != null && currentInfoItem != null) {
            infoListAdapter.setItem(currentInfoItem);
            myApp.setFoodInfoItem(null);
        }
        if(myApp.getIsNewBestfood() == true){
            setRecyclerView();
            listInfo(keyWord,memberSeq,orderType,0,fromBestFoodListFragment);
            myApp.setIsNewBestfood(false);
        }
    }

    /**
     * onCreateView() 메소드 뒤에 호출되며 화면 뷰들을 설정한다.
     * @param view onCreateView() 메소드에 의해 반환된 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_list);

        orderType = Constant.ORDER_TYPE_RECENT;

        bestFoodList = (RecyclerView) view.findViewById(R.id.list);
        noDataText = (TextView) view.findViewById(R.id.no_data);
        searchKeyWord = (ImageView)view.findViewById(R.id.search_key_word);
searchKey = (EditText_Roboto_Regular)view.findViewById(R.id.search_key);
        orderRecent = (TextView) view.findViewById(R.id.order_recent);
        orderFavorite = (TextView) view.findViewById(R.id.order_favorite);
        orderHits = (TextView) view.findViewById(R.id.order_hits);
        inputPost = (TextView)view.findViewById(R.id.input_post);

        orderRecent.setOnClickListener(this);
        orderFavorite.setOnClickListener(this);
        orderHits.setOnClickListener(this);
        inputPost.setOnClickListener(this);
        searchKeyWord.setOnClickListener(this);
      //listType.setOnClickListener(this);

        setRecyclerView();
        listInfo(keyWord,memberSeq, orderType, 0,fromBestFoodListFragment);
    }




    /**
     * 리사이클러뷰를 설정하고 스크롤 리스너를 추가한다.
     */
    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity());
        bestFoodList.setLayoutManager(layoutManager);


        infoListAdapter = new InfoListAdapter(context,
                R.layout.row_bestfood_list, new ArrayList<FoodInfoItem>());
        bestFoodList.setAdapter(infoListAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listInfo(keyWord,memberSeq,orderType, page,fromBestFoodListFragment);
            }
        };
        bestFoodList.addOnScrollListener(scrollListener);
    }

    /**
     * 서버에서 맛집 정보를 조회한다.
     * @param memberSeq 사용자 시퀀스
     * @param orderType 맛집 정보 정렬 순서
     * @param currentPage 현재 페이지
     */
    private void listInfo(String keyWord,int memberSeq,String orderType, final int currentPage,int fromBestFoodListFragment) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<FoodInfoItem>> call = remoteService.listFoodInfo(keyWord,memberSeq,orderType, currentPage,fromBestFoodListFragment);
        call.enqueue(new Callback<ArrayList<FoodInfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<FoodInfoItem>> call,
                                   Response<ArrayList<FoodInfoItem>> response) {
                ArrayList<FoodInfoItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    infoListAdapter.addItemList(list);

                    if (infoListAdapter.getItemCount() == 0) {
                        noDataText.setVisibility(View.VISIBLE);
                    } else {
                        noDataText.setVisibility(View.GONE);
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

    /**
     * 각종 버튼에 대한 클릭 처리를 정의한다.
     * @param v 클릭한 뷰에 대한 정보
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.order_recent) {
            orderType = Constant.ORDER_TYPE_RECENT;
            setOrderTextColor(R.color.text_color_green,
                    R.color.text_color_black, R.color.text_color_black);

        } else if (v.getId() == R.id.order_favorite) {
            orderType = Constant.ORDER_TYPE_FAVORITE;
            setOrderTextColor(R.color.text_color_black,
                    R.color.text_color_green, R.color.text_color_black);
        } else if (v.getId() == R.id.order_hits) {
            orderType = Constant.ORDER_TYPE_HITS;
            setOrderTextColor(R.color.text_color_black,
                    R.color.text_color_black, R.color.text_color_green);
        }
        else if(v.getId() == R.id.input_post){
            if (((MyApp) context.getApplicationContext()).getMemberNickname() == null
                    || ((MyApp) context.getApplicationContext()).equals("")) {
                    DialogLib.getInstance().inputPostDialog(context);
            }else{
                GoLib.getInstance().goBestFoodRegisterActivity(context,fromBestFoodListFragment);
            }
        }else if(v.getId() == R.id.search_key_word){
            keyWord =  searchKey.getText().toString();
        }
        setRecyclerView();
        listInfo(keyWord, memberSeq, orderType, 0,fromBestFoodListFragment);
    }


    /**
     * 맛집 정보 정렬 방식의 텍스트 색상을 설정한다.
     * @param color1 거리순 색상
     * @param color2 인기순 색상
     * @param color3 최근순 색상
     */
    private void setOrderTextColor(int color1, int color2, int color3) {
        orderRecent.setTextColor(ContextCompat.getColor(context, color1));
        orderFavorite.setTextColor(ContextCompat.getColor(context, color2));
        orderHits.setTextColor(ContextCompat.getColor(context, color3));
    }

    /**
     * 리사이클러뷰의 리스트 형태를 변경한다.
     */

}
