package com.mobitant.bestfood.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.adapter.InfoImageAdapter;
import com.mobitant.bestfood.item.NotificationCommentItem;
import com.mobitant.bestfood.item.NotificationItem;
import com.mobitant.bestfood.item.ImageItem;
import com.mobitant.bestfood.item.SingerItem;

import com.mobitant.bestfood.lib.DialogLib;
import com.mobitant.bestfood.lib.EtcLib;

import com.mobitant.bestfood.lib.KeepLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.MyToast;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 맛집 정보를 보는 액티비티이다.
 */
public class NotificationDetailFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    public static final String SEQ = "SEQ";

    public static NotificationDetailFragment newInstance() {
        NotificationDetailFragment f = new NotificationDetailFragment();
        return f;
    }

    Context context;
    int memberSeq;
    int foodInfoSeq;
    String infoItemId;
    NotificationItem item;
    ImageView profileIconImage;
    RecyclerView imageItemList;
    RecyclerView viewdlistView;
    SingerAdapter adapter;
    ScrollView scrollView;
    ImageView keepImage;
    InfoImageAdapter infoImageAdapter;
    ArrayList<ImageItem> images = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager, postLayoutManager;
    View view;

    /**
     * fragment_bestfood_list.xml 기반으로 뷰를 생성한다.
     *
     * @param inflater           XML를 객체로 변환하는 LayoutInflater 객체
     * @param container          null이 아니라면 부모 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     * @return 생성한 뷰 객체
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();
        View layout = inflater.inflate(R.layout.notification_fragment_info, container, false);
        return layout;
    }

    /**
     * onCreateView() 메소드 뒤에 호출되며 화면 뷰들을 설정한다.
     *
     * @param view               onCreateView() 메소드에 의해 반환된 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        memberSeq = ((MyApp) getActivity().getApplication()).getMemberSeq();
        infoItemId = getArguments().getString("SEQ");
        this.view = view;
        selectFoodInfo(infoItemId, memberSeq);


    }

    /*
    댓글 내용을 설정한다.
     */
    public void setComment() {
        viewdlistView = (RecyclerView) view.findViewById(R.id.viwedlistView);
        adapter = new SingerAdapter();
        for (int i = 0; i < item.getCommentItems().size(); i++) {
            adapter.addItem(new SingerItem(item.getCommentItems().get(i).getWriter(), item.getCommentItems().get(i).getContents(),
                    item.getCommentItems().get(i).getComment_like(), item.getCommentItems().get(i).getMemberIconFileName(), item.getCommentItems().get(i).id));
        }
        postLayoutManager = new LinearLayoutManager(getActivity());
        viewdlistView.setLayoutManager(postLayoutManager);
        viewdlistView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    private void setView(RecyclerView.ViewHolder holder) {
        //getSupportActionBar().setTitle(item.name);

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
        // ((BViewHolder) holder).textView.setText(item.getName());

        if (!StringLib.getInstance().isBlank(item.getTitle())) {
            ((SingerAdapter.AViewHolder) holder).nameText.setText(item.getTitle());
        }

        if (!StringLib.getInstance().isBlank(item.tel)) {
            ((SingerAdapter.AViewHolder) holder).tel.setText(EtcLib.getInstance().getPhoneNumberText(item.tel));
            ((SingerAdapter.AViewHolder) holder).tel.setOnClickListener(this);
        } else {
            ((SingerAdapter.AViewHolder) holder).tel.setVisibility(View.GONE);
        }

        if (!StringLib.getInstance().isBlank(item.getContents())) {
            ((SingerAdapter.AViewHolder) holder).description.setText(item.getContents());
        } else {
            ((SingerAdapter.AViewHolder) holder).description.setText(R.string.no_text);
        }
    }

    /**
     * 서버에서 맛집 정보를 조회한다.
     *
     * @param memberSeq 사용자 시퀀스
     */
    private void selectFoodInfo(String infoItemId, int memberSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<NotificationItem> call = remoteService.selectNotificationInfo(infoItemId, memberSeq);

        call.enqueue(new Callback<NotificationItem>() {
            @Override
            public void onResponse(Call<NotificationItem> call, Response<NotificationItem> response) {
                NotificationItem infoItem = response.body();
                if (response.isSuccessful() && infoItem != null && infoItem.seq > 0) {
                    item = infoItem;
                    setComment();

                } else {
                    //loadingText.setVisibility(View.VISIBLE);
                    //((TextView) findViewById(R.id.loading_text)).setText(R.string.loading_not);
                }
            }

            @Override
            public void onFailure(Call<NotificationItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    /**
     * 리사이클러뷰를 설정한다
     */
    private void setRecyclerView() {

        layoutManager = new LinearLayoutManager(getActivity());
        imageItemList.setLayoutManager(layoutManager);


        infoImageAdapter = new InfoImageAdapter(getActivity(),
                R.layout.row_post_image_list, new ArrayList<ImageItem>());
        images = item.totalImageFilename;
        infoImageAdapter.addItemList(images);
        imageItemList.setAdapter(infoImageAdapter);

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
            if (position == 0) { //제목,내용,이미지 포지션
                setView(holder);
                setRecyclerView();
            } else if (position < (allHeight - 1)) { //댓글위치의 포지션
                MyLog.d(TAG, "댓글 사이즈 " + item.getCommentItems().size());

                SingerItem singerItem = items.get(position - 1);

                ((BViewHolder) holder).textView.setText(singerItem.getName());
                ((BViewHolder) holder).textView2.setText(singerItem.getMobile());
                ((BViewHolder) holder).textView3.setText(String.valueOf(singerItem.getAge()));
                ((BViewHolder) holder).imageView.setImageResource(singerItem.getResId());
                //로그인되어있는 사용자와 댓글입력한 사용자의 닉네임이 같을때
                if(((MyApp) getActivity().getApplication()).getMemberNickName()==singerItem.getName()){
                    ((BViewHolder) holder).removeComment.setOnClickListener(new View.OnClickListener() {
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
                        String name = ((MyApp) getActivity().getApplication()).getMemberNickName();
                        commentItem.setWriter(name);
                        String contents = ((SingerAdapter.CViewHolder) holder).viewedEditText.getText().toString();
                        commentItem.setContents(contents);
                        commentItem.setPostId(item.id);//mongoDB의 _id필드를 말하는듯하다.
                        int comment_like = 0;
                        commentItem.setComment_like(comment_like);
                        commentItem.setMemberIconFileName( ((MyApp)getActivity().getApplication()).getMemberIconFilename());
                        insertCommentItem(commentItem, name, contents, comment_like);
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
        private void insertCommentItem(NotificationCommentItem commentItem, String name, String contents, int comment_like) {
            MyLog.d(TAG, commentItem.toString());

            RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

            Call<String> call = remoteService.insertComment(commentItem);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String commentId = response.body();
                        MyToast.s(context, "댓글이 등록되었습니다.");

                        adapter.addItem(new SingerItem(name, contents, comment_like,
                                ((MyApp)getActivity().getApplication()).getMemberIconFilename(), commentId));
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

            public AViewHolder(View itemView) {
                super(itemView);

                imageItemList = (RecyclerView) itemView.findViewById(R.id.image_list);
                nameText = (TextView) itemView.findViewById(R.id.name);
                tel = (TextView) itemView.findViewById(R.id.tel);
                description = (TextView) itemView.findViewById(R.id.description);
            }
        }

        public class BViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            TextView textView2;
            TextView textView3;
            ImageView imageView;
            TextView removeComment;

            public BViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textView);
                removeComment = (TextView) itemView.findViewById(R.id.remove_comment);
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
                viwedButton = (Button) itemView.findViewById(R.id.viwedbutton);
                viewedEditText = (EditText) itemView.findViewById(R.id.viwededitText);
            }
        }
    }

}