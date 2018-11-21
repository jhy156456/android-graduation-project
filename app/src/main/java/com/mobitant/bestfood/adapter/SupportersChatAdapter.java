package com.mobitant.bestfood.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobitant.bestfood.ChatTalkContentsActivity;
import com.mobitant.bestfood.MemberProfile;
import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;

import com.mobitant.bestfood.SoftwareBuyActivity;
import com.mobitant.bestfood.TextViewImmacBytes;
import com.mobitant.bestfood.item.User;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class SupportersChatAdapter extends RecyclerView.Adapter<SupportersChatAdapter.ViewHolder> {
    private Context context;
    private int resource;
    private ArrayList<User> itemList;
    User memberInfoItem;

    /**
     * 어댑터 생성자
     *
     * @param context  컨텍스트 객체
     * @param resource 아이템을 보여주기 위해 사용할 리소스 아이디
     * @param itemList 아이템 리스트
     */
    public SupportersChatAdapter(Context context, int resource, ArrayList<User> itemList) {
        this.context = context;
        this.resource = resource;
        this.itemList = itemList;

        memberInfoItem = ((MyApp) context.getApplicationContext()).getUserItem();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.supporters_fragment_second_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User currentUser = itemList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(currentUser.nickname,currentUser.memberIconFilename,currentUser.seq);
            }
        });
        holder.supportersNickName.setText(currentUser.nickname);
        holder.oneLineDescription.setText(currentUser.getOneLineDescription());
        if (StringLib.getInstance().isBlank(currentUser.memberIconFilename)) {
            Picasso.with(context).load(R.drawable.ic_person).into(holder.supportersMemberIconFileName);
        }            else if (currentUser.memberIconFilename.length()>=30){
            Picasso.with(context).load(currentUser.memberIconFilename).into(holder.supportersMemberIconFileName);
        }

        else {
            Picasso.with(context)
                    .load(RemoteService.MEMBER_ICON_URL + currentUser.memberIconFilename)
                    .into(holder.supportersMemberIconFileName);
        }
    }
private void showDialog(String nickName,String memberIconFileName,int userSeq){
    new AlertDialog.Builder(context)
            .setTitle(R.string.title_bestfood_image_register)
            .setSingleChoiceItems(R.array.user_click, -1,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent intent = new Intent(context.getApplicationContext(), MemberProfile.class);
                                intent.putExtra("data", userSeq); //흠 이렇게해도 되는건가.. 아닌것같다
                                intent.putExtra("userNickName", nickName); //흠 이렇게해도 되는건가.. 아닌것같다
                                intent.putExtra("MySeq", ((MyApp) context.getApplicationContext()).getMemberSeq());
                                intent.putExtra("callActivity", "SupportersActivity");

                                //멤버의 프로필을 보려면 그사람의 seq를 조회하고 프로필화면으로 들어갔을때
                                //그사람의 전체게시글,닉네임,설명 등을 확인해야할듯!!
                                //추가하자!
                                context.startActivity(intent);
                            } else {
                                Intent intent = new Intent(context, ChatTalkContentsActivity.class);
                                intent.putExtra("owner",((MyApp)context.getApplicationContext()).getMemberNickName());
                                intent.putExtra("participant",nickName);
                                intent.putExtra("participant_member_icon_file_name",memberIconFileName);
                                intent.putExtra("callActivity", "ChatSupportersFragment");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            dialog.dismiss();
                        }
                    }).show();
}
    /**
     * 현재 아이템 리스트에 새로운 아이템 리스트를 추가한다.
     *
     * @param itemList 새로운 아이템 리스트
     */
    public void addItemList(ArrayList<User> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextViewImmacBytes supportersNickName;
        CircularImageView supportersMemberIconFileName;
TextViewImmacBytes oneLineDescription;
        public ViewHolder(View itemView) {

            super(itemView);
            supportersNickName = (TextViewImmacBytes) itemView.findViewById(R.id.supporters_nickname);
            supportersMemberIconFileName = (CircularImageView) itemView.findViewById(R.id.supporters_member_icon_filename);
            oneLineDescription = (TextViewImmacBytes)itemView.findViewById(R.id.one_line_description);
        }
    }
}
