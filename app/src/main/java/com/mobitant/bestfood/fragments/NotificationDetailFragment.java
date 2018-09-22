package com.mobitant.bestfood.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mobitant.bestfood.MemberProfile;
import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.adapter.InfoImageAdapter;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.ImageItem;
import com.mobitant.bestfood.item.SingerItem;
import com.mobitant.bestfood.lib.DialogLib;
import com.mobitant.bestfood.lib.EtcLib;
import com.mobitant.bestfood.lib.GoLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 맛집 정보를 보는 액티비티이다.
 */
public class NotificationDetailFragment extends AppCompatActivity implements  View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    public static final String INFO_SEQ = "INFO_SEQ";
    Context context;
    int memberSeq;
    int foodInfoSeq;
    FoodInfoItem item;
    ImageView profileIconImage;
    RecyclerView imageItemList;
    RecyclerView viewdlistView;
    SingerAdapter adapter;
    ScrollView scrollView;
    ImageView keepImage;
    InfoImageAdapter infoImageAdapter;
    ArrayList<ImageItem> images = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager,postLayoutManager;


    /**
     * 맛집 정보를 보여주기 위해 사용자 시퀀스와 맛집 정보 시퀀스를 얻고
     * 이를 기반으로 서버에서 맛집 정보를 조회하는 메소드를 호출한다.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestfood_info);
        context = this;
        memberSeq = ((MyApp) getApplication()).getMemberSeq();
        foodInfoSeq = getIntent().getIntExtra(INFO_SEQ, 0);
        setComment();

        setToolbar();

    }

    /*
    댓글 내용을 설정한다.
     */
    public void setComment() {
        viewdlistView = (RecyclerView) findViewById(R.id.viwedlistView);
        adapter = new SingerAdapter();
        adapter.addItem(new SingerItem("소녀시대", "010-1000-1000", 20, R.drawable.singer));
        adapter.addItem(new SingerItem("걸스데이", "010-2000-2000", 22, R.drawable.singer2));
        adapter.addItem(new SingerItem("여자친구", "010-3000-3000", 21, R.drawable.singer3));
        adapter.addItem(new SingerItem("티아라", "010-4000-4000", 24, R.drawable.singer4));
        adapter.addItem(new SingerItem("AOA", "010-5000-5000", 25, R.drawable.singer5));
        postLayoutManager = new LinearLayoutManager(this);
        viewdlistView.setLayoutManager(postLayoutManager);


        viewdlistView.setAdapter(adapter);
    }

    /**
     * 리사이클러뷰를 설정한다
     */
    private void setRecyclerView() {

        layoutManager = new LinearLayoutManager(this);
        imageItemList.setLayoutManager(layoutManager);


        infoImageAdapter = new InfoImageAdapter(this,
                R.layout.row_post_image_list, new ArrayList<ImageItem>());
        imageItemList.setAdapter(infoImageAdapter);

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
            actionBar.setTitle("");
        }
    }

    /**
     * 오른쪽 상단 메뉴를 구성한다.
     * 닫기 메뉴만이 설정되어 있는 menu_close.xml를 지정한다.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close_buy, menu);
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
            case android.R.id.home:
                finish();
                break;
            case R.id.action_close:
                finish();
                break;
            case R.id.go_home:
                GoLib.getInstance().goHomeActivity(this);
            case R.id.action_buy:
                GoLib.getInstance().goBuyActivity(this);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 서버에서 맛집 정보를 조회한다.
     *
     * @param foodInfoSeq 맛집 정보 시퀀스
     * @param memberSeq   사용자 시퀀스
     */
    private void selectFoodInfo(int foodInfoSeq, int memberSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<FoodInfoItem> call = remoteService.selectFoodInfo(foodInfoSeq, memberSeq);

        call.enqueue(new Callback<FoodInfoItem>() {
            @Override
            public void onResponse(Call<FoodInfoItem> call, Response<FoodInfoItem> response) {
                FoodInfoItem infoItem = response.body();
                if (response.isSuccessful() && infoItem != null && infoItem.seq > 0) {

                    images = infoItem.totalImageFilename;
                    infoImageAdapter.addItemList(images);
                    item = infoItem;
                    //loadingText.setVisibility(View.GONE);
                    setView();
                } else {
                    //loadingText.setVisibility(View.VISIBLE);
                    //((TextView) findViewById(R.id.loading_text)).setText(R.string.loading_not);
                }
            }

            @Override
            public void onFailure(Call<FoodInfoItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    /**
     * 서버에서 조회한 맛집 정보를 화면에 설정한다.
     */
    private void setView() {
        getSupportActionBar().setTitle(item.name);
        // scrollView = (ScrollView) findViewById(R.id.scroll_view);
//프로필 이미지 추가
        /*
        if (StringLib.getInstance().isBlank(item.postMemberIconFilename)) {
            Picasso.with(this).load(R.drawable.ic_person).into(profileIconImage);
        } else {
            Picasso.with(this)
                    .load(RemoteService.MEMBER_ICON_URL + item.postMemberIconFilename)
                    .into(profileIconImage);
        }
*/
        TextView nameText = (TextView) findViewById(R.id.name);
        if (!StringLib.getInstance().isBlank(item.name)) {
            nameText.setText(item.name);
        }
        TextView nickNameText = (TextView) findViewById(R.id.nickname);
        nickNameText.setText(item.post_nickname);
        nickNameText.setTextColor(Color.parseColor("#000000"));
        nickNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemberProfile.class);
                intent.putExtra("data", item.memberSeq); //흠 이렇게해도 되는건가.. 아닌것같다
                intent.putExtra("MySeq", ((MyApp) getApplication()).getMemberSeq());
                //멤버의 프로필을 보려면 그사람의 seq를 조회하고 프로필화면으로 들어갔을때
                //그사람의 전체게시글,닉네임,설명 등을 확인해야할듯!!
                //추가하자!
                startActivityForResult(intent, 101);
            }
        });
        keepImage = (ImageView) findViewById(R.id.keep);
        keepImage.setOnClickListener(this);
        if (item.isKeep) {
            keepImage.setImageResource(R.drawable.ic_keep_on);
        } else {
            keepImage.setImageResource(R.drawable.ic_keep_off);
        }

        TextView tel = (TextView) findViewById(R.id.tel);
        if (!StringLib.getInstance().isBlank(item.tel)) {
            tel.setText(EtcLib.getInstance().getPhoneNumberText(item.tel));
            tel.setOnClickListener(this);
        } else {
            tel.setVisibility(View.GONE);
        }

        TextView description = (TextView) findViewById(R.id.description);
        if (!StringLib.getInstance().isBlank(item.description)) {
            description.setText(item.description);
        } else {
            description.setText(R.string.no_text);
        }
    }


    Handler keepHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            item.isKeep = !item.isKeep;
            if (item.isKeep) {
                keepImage.setImageResource(R.drawable.ic_keep_on);
            } else {
                keepImage.setImageResource(R.drawable.ic_keep_off);
            }
        }
    };

    /**
     * 즐겨찾기 버튼과 위치보기 버튼을 클릭했을 때의 동작을 정의한다.
     *
     * @param v 클릭한 뷰에 대한 정보
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.keep) {
            if (item.isKeep) {
                DialogLib.getInstance()
                        .showKeepDeleteDialog(context, keepHandler, memberSeq, item.seq);
                keepImage.setImageResource(R.drawable.ic_keep_off);
            } else {
                DialogLib.getInstance()
                        .showKeepInsertDialog(context, keepHandler, memberSeq, item.seq);
                keepImage.setImageResource(R.drawable.ic_keep_on);
            }
        }
    }

    /**
     * 화면이 일시정지 상태로 될 때 호출되며 현재 아이템의 변경 사항을 저장한다.
     * 이는 BestFoodListFragment나 BestFoodKeepFragment에서 변경된 즐겨찾기 상태를 반영하는
     * 용로도 사용된다.
     */
    @Override
    protected void onPause() {
        super.onPause();
        ((MyApp) getApplication()).setFoodInfoItem(item);
    }

    class SingerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<SingerItem> items = new ArrayList<>();
        public static final int VIEW_TYPE_A = 0;
        public static final int VIEW_TYPE_B = 1;
        public static final int VIEW_TYPE_C = 2;
        int allHeight = 0; //댓글입력하면 아이템갯수가 변하니까 position과 맞춰주기위한 변수


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            if (viewType == VIEW_TYPE_A) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.header, viewGroup, false);
                return new AViewHolder(v);
            } else if (viewType ==VIEW_TYPE_B) { //댓글위치의 position
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.singer_item, viewGroup, false);
                return new BViewHolder(v);
            } else if (viewType==VIEW_TYPE_C) { //등록버튼 position
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.footer, viewGroup, false);
                return new CViewHolder(v);
            }
            return null;
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (position == 0) {
                selectFoodInfo(foodInfoSeq, memberSeq);
                setRecyclerView();
            } else if (position < (allHeight - 1)) {
                SingerItem item = items.get(position - 1);

                ((BViewHolder) holder).textView.setText(item.getName());
                ((BViewHolder) holder).textView2.setText(item.getMobile());
                ((BViewHolder) holder).textView3.setText(String.valueOf(item.getAge()));
                ((BViewHolder) holder).imageView.setImageResource(item.getResId());
            } else {

                ((CViewHolder)holder).viwedButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "닉네임";
                        String mobile =  ((CViewHolder)holder).viewedEditText.getText().toString();
                        int age = 20;
                        adapter.addItem(new SingerItem(name, mobile, age, R.drawable.singer3));
                        adapter.notifyDataSetChanged();
                    }
                });
            }

        }

        public void addItem(SingerItem item) {
            items.add(item);
        }

        @Override
        public int getItemCount() {
            allHeight = items.size() + 2;
            return allHeight;
        }
        public int getItemViewType(int position) {
            if (position == 0) {
                return VIEW_TYPE_A;
            } else if (position < (allHeight - 1)) {
                return VIEW_TYPE_B;
            } else {
                return VIEW_TYPE_C;
            }
        }
        public Object getItem(int position) {
            Object gogoItem = null;
            if (position == 0) {
            } else {
                gogoItem = (SingerItem) items.get(position - 1);
            }
            return gogoItem;
        }

        public class AViewHolder extends RecyclerView.ViewHolder {
            public AViewHolder(View itemView) {
                super(itemView);

                imageItemList = (RecyclerView) itemView.findViewById(R.id.image_list);
            }
        }
        public class BViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            TextView textView2;
            TextView textView3;
            ImageView imageView;
            public BViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textView);
                textView2 = (TextView) itemView.findViewById(R.id.textView2);
                textView3 = (TextView) itemView.findViewById(R.id.textView3);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
            }
        }
        public class CViewHolder extends RecyclerView.ViewHolder {
            Button viwedButton;
            EditText viewedEditText;
            public CViewHolder(View itemView) {
                super(itemView);
                viwedButton =(Button)itemView.findViewById(R.id.viwedbutton);
                viewedEditText = (EditText) itemView.findViewById(R.id.viwededitText);
            }
        }
    }

}