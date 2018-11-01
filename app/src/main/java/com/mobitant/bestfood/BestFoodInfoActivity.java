package com.mobitant.bestfood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.mobitant.bestfood.adapter.InfoImageAdapter;
import com.mobitant.bestfood.fragments.NotificationDetailFragment;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.ImageItem;
import com.mobitant.bestfood.item.NotificationCommentItem;
import com.mobitant.bestfood.item.NotificationItem;
import com.mobitant.bestfood.item.OrderCheckItem;
import com.mobitant.bestfood.item.SingerItem;
import com.mobitant.bestfood.lib.DialogLib;
import com.mobitant.bestfood.lib.EtcLib;
import com.mobitant.bestfood.lib.GoLib;
import com.mobitant.bestfood.lib.KeepLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.MyToast;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 맛집 정보를 보는 액티비티이다.
 */
public class BestFoodInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    public static final String INFO_SEQ = "INFO_SEQ";

    Context context;
    int memberSeq;
    int foodInfoSeq;
    OrderCheckItem orderCheckItem;

    FoodInfoItem item;
    String postNickName, postMemberIconFilename;
    ImageView profileIconImage;
    RecyclerView imageItemList;
    RecyclerView viewdlistView;
    SingerAdapter adapter;
    ScrollView scrollView;
    ImageView keepImage; // 핸들러처리때문에 전역변수로 있는게 맞는듯.
    InfoImageAdapter infoImageAdapter;
    ArrayList<ImageItem> images = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager, postLayoutManager;


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
        selectFoodInfo(foodInfoSeq, memberSeq);
        setToolbar();

    }

    /*
    댓글 내용을 설정한다.
     */
    public void setComment() {
        viewdlistView = (RecyclerView) findViewById(R.id.viwedlistView);
        adapter = new SingerAdapter();
        for(int i=0; i<item.getCommentItems().size();i++){
            adapter.addItem(new SingerItem(item.getCommentItems().get(i).getWriter(),item.getCommentItems().get(i).getContents(),
                    item.getCommentItems().get(i).getComment_like(),item.getCommentItems().get(i).getMemberIconFileName(),item.id));
        }
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
        images = item.totalImageFilename;

        infoImageAdapter.addItemList(images);
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
    public boolean onOptionsItemSelected(MenuItem mItem) {
        //원래 MenuItem item 이였는데 지금보고있는 게시물의 변수명인 item과 같아서 오류가났었다
        //그래서 item을 메뉴아이템인 mItem으로 변경했다.
        switch (mItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_close:
                finish();
                break;
            case R.id.go_home:
                GoLib.getInstance().goHomeActivity(this);
            case R.id.action_buy:
                orderCheckItem = new OrderCheckItem();
                orderCheckItem.setPostSeq(item.seq);
                orderCheckItem.setInfoTitle(item.name);
                orderCheckItem.setPostNickName(item.post_nickname);// 구매화면 전환해서 정보를 보여주기위함
                orderCheckItem.setPostMemberIconFilename(item.postMemberIconFilename);// 구매화면 전환해서 정보를 보여주기위함
                if(item.totalImageFilename.size() == 0){
                    orderCheckItem.setInfoFirstImageFilename("");
                }else orderCheckItem.setInfoFirstImageFilename(item.totalImageFilename.get(0).fileName);
                orderCheckItem.setInfoContent(item.description);
                GoLib.getInstance().goBuyActivity(this, orderCheckItem);
        }

        return super.onOptionsItemSelected(mItem);
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
                    item = infoItem;
                    setComment();
                    //loadingText.setVisibility(View.GONE);
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
    private void setView(RecyclerView.ViewHolder holder) {

        getSupportActionBar().setTitle(item.name);
        // scrollView = (ScrollView) findViewById(R.id.scroll_view);


        if (!StringLib.getInstance().isBlank(item.name)) {
            ((SingerAdapter.AViewHolder) holder).nameText.setText(item.name);

        }
        ((SingerAdapter.AViewHolder) holder).hits.setText("조회수 : "+item.hits);

        //멤버 프로필 이미지 설정
        if (StringLib.getInstance().isBlank(item.postMemberIconFilename)) {
            Picasso.with(this).load(R.drawable.ic_person).into(((SingerAdapter.AViewHolder) holder).profileIconImage);
        } else {
            Picasso.with(this)
                    .load(RemoteService.MEMBER_ICON_URL + item.postMemberIconFilename)
                    .into(((SingerAdapter.AViewHolder) holder).profileIconImage);
        }

        //멤버 프로필 이미지 설정 끝


        ((SingerAdapter.AViewHolder) holder).nickNameText.setText(item.post_nickname);
        ((SingerAdapter.AViewHolder) holder).nickNameText.setTextColor(Color.parseColor("#000000"));
        ((SingerAdapter.AViewHolder) holder).nickNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemberProfile.class);
                intent.putExtra("data", item.memberSeq); //흠 이렇게해도 되는건가.. 아닌것같다
                intent.putExtra("MySeq", ((MyApp) getApplication()).getMemberSeq());
                intent.putExtra("callActivity", "BestFoodInfoActivity");

                //멤버의 프로필을 보려면 그사람의 seq를 조회하고 프로필화면으로 들어갔을때
                //그사람의 전체게시글,닉네임,설명 등을 확인해야할듯!!
                //추가하자!
                startActivity(intent);
            }
        });
        //핸들러때문에 전역변수로 사용하자?
        keepImage.setOnClickListener(this);
        if (item.isKeep) {
            keepImage.setImageResource(R.drawable.ic_keep_on);
        } else {
            keepImage.setImageResource(R.drawable.ic_keep_off);
        }


        if (!StringLib.getInstance().isBlank(item.tel)) {
            ((SingerAdapter.AViewHolder) holder).tel.setText(EtcLib.getInstance().getPhoneNumberText(item.tel));
            ((SingerAdapter.AViewHolder) holder).tel.setOnClickListener(this);
        } else {
            ((SingerAdapter.AViewHolder) holder).tel.setVisibility(View.GONE);
        }


        if (!StringLib.getInstance().isBlank(item.description)) {
            ((SingerAdapter.AViewHolder) holder).description.setText(item.description);
        } else {
            ((SingerAdapter.AViewHolder) holder).description.setText(R.string.no_text);
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
                //keepImage.setImageResource(R.drawable.ic_keep_off);
            } else {
                DialogLib.getInstance()
                        .showKeepInsertDialog(context, keepHandler, memberSeq, item.seq);
                //keepImage.setImageResource(R.drawable.ic_keep_on);
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
            } else if (viewType == VIEW_TYPE_B) { //댓글위치의 position
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.singer_item, viewGroup, false);
                return new BViewHolder(v);
            } else if (viewType == VIEW_TYPE_C) { //등록버튼 position
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.footer, viewGroup, false);
                return new CViewHolder(v);
            }
            return null;
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (position == 0) {
                setView(holder);
                setRecyclerView();
            } else if (position < (allHeight - 1)) {
                SingerItem singerItem = items.get(position - 1);
                ((BViewHolder) holder).textView.setText(singerItem.getName());
                ((BViewHolder) holder).textView2.setText(singerItem.getMobile());
                ((BViewHolder) holder).textView3.setText(String.valueOf(singerItem.getAge()));

                if (StringLib.getInstance().isBlank(singerItem.memberIconFileName)) {
                    Picasso.with(context).load(R.drawable.ic_person).into(((BViewHolder) holder).imageView);
                } else {
                    Picasso.with(context)
                            .load(RemoteService.MEMBER_ICON_URL + singerItem.memberIconFileName)
                            .into(((BViewHolder) holder).imageView);
                }

                //로그인되어있는 사용자와 댓글입력한 사용자의 닉네임이 같을때
                if(((MyApp)getApplication()).getMemberNickName()==singerItem.getName()){
                    ((SingerAdapter.BViewHolder) holder).removeComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyLog.d("아이템 아이디 : " + item.id);
                            new AlertDialog.Builder(context)
                                    .setTitle("댓글삭제")
                                    .setMessage("삭제하시겠습니까?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            KeepLib.getInstance().deleteComment(item.id, singerItem.getId());
                                            deleteItem(singerItem.getId()); // _아이디로 어레이리스트삭제
                                            adapter.notifyDataSetChanged();
                                            MyToast.s(context, "댓글이 삭제되었습니다.");
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }
                    });
                }else{
                    ((BViewHolder) holder).removeComment.setVisibility(View.GONE);
                }















            } else { //등록버튼 포지션

                ((CViewHolder) holder).viwedButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NotificationCommentItem commentItem = new NotificationCommentItem();
                        String name = ((MyApp) getApplication()).getMemberNickName();
                        commentItem.setWriter(name);
                        String contents =  ((SingerAdapter.CViewHolder)holder).viewedEditText.getText().toString();
                        commentItem.setMemberIconFileName(((MyApp) getApplication()).getMemberIconFilename());
                        commentItem.setContents(contents);
                        commentItem.setPostId(item.id);//mongoDB의 _id필드를 말하는듯하다.
                        int comment_like = 0;
                        insertCommentItem(commentItem,name,contents,comment_like);
                        //서버에 요청한후 그다음 화면갱신
                        //댓글 삭제,수정을위해 mongoDB의 ID값을 저장해놓고 있어야하기때문에 서버에 저장한 후에
                        //아이디값을 리턴받아서 안드로이드에 띄워줌
                    }
                });
            }

        }


        /**
         * 사용자가 입력한 정보를 서버에 저장한다.
         */
        private void insertCommentItem(NotificationCommentItem commentItem, String name, String contents,int comment_like) {
            MyLog.d(TAG, commentItem.toString());

            RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

            Call<String> call = remoteService.insertSoftWareComment(commentItem);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String commentId = response.body();
                        MyToast.s(context, "댓글이 등록되었습니다.");

                        adapter.addItem(new SingerItem(name, contents, comment_like,
                                ((MyApp) getApplication()).getMemberIconFilename(),commentId));
                        adapter.notifyDataSetChanged();

                    } else { // 등록 실패
                        int statusCode = response.code();
                        ResponseBody errorBody = response.errorBody();
                        MyLog.d(TAG, "fail " + statusCode + errorBody.toString());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    MyLog.d(TAG, "no internet connectivity");
                }
            });
        }
        public void addItem(SingerItem item) {
            items.add(item);
        }

        public void deleteItem(String id) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getId().equals(id)) {
                    items.remove(i);
                }
            }
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
            TextView nameText;
            TextView tel;
            TextView description;
            TextView nickNameText;
            TextView hits;
            ImageView profileIconImage;

            public AViewHolder(View itemView) {
                super(itemView);

                imageItemList = (RecyclerView) itemView.findViewById(R.id.image_list);
                nameText = (TextView) itemView.findViewById(R.id.name);
                hits = (TextView)itemView.findViewById(R.id.hits);
                tel = (TextView) itemView.findViewById(R.id.tel);
                description = (TextView) itemView.findViewById(R.id.description);
                nickNameText = (TextView) itemView.findViewById(R.id.nickname);
                keepImage = (ImageView) itemView.findViewById(R.id.keep);
                profileIconImage = (ImageView) itemView.findViewById(R.id.post_profile_icon);
            }
        }

        public class BViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            TextView textView2;
            TextView textView3;
            ImageView imageView,upLike,downLike;
            TextView removeComment;


            public BViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textView);
                textView2 = (TextView) itemView.findViewById(R.id.textView2);
                removeComment = (TextView) itemView.findViewById(R.id.remove_comment);
                textView3 = (TextView) itemView.findViewById(R.id.textView3);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                upLike = (ImageView)itemView.findViewById(R.id.up_like);
                downLike = (ImageView)itemView.findViewById(R.id.down_like);
            }
        }

        public class CViewHolder extends RecyclerView.ViewHolder {
            Button viwedButton;
            EditText viewedEditText;

            public CViewHolder(View itemView) {
                super(itemView);
                viwedButton = (Button) itemView.findViewById(R.id.viwedbutton);
                viewedEditText = (EditText) itemView.findViewById(R.id.viwededitText);
            }
        }
    }

}