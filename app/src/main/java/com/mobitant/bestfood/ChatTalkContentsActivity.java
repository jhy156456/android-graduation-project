package com.mobitant.bestfood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitant.bestfood.adapter.MessageAdapter;
import com.mobitant.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.mobitant.bestfood.item.ChatContentsItem;
import com.mobitant.bestfood.item.ChatTalkData;
import com.mobitant.bestfood.lib.KeepLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.MyToast;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import customfonts.EditText_Helvatica_Meidum;
import customfonts.EditText_Roboto_Regular;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatTalkContentsActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    private LinearLayout chatRoomExit;
    private EditText_Helvatica_Meidum mInputMessageView;

    private Socket mSocket;
    private ArrayList<ChatContentsItem> contentsItems;
    private String roomId;
    private RecyclerView mMessagesView;
    private Boolean isConnected = true;
    private MessageAdapter mAdapter;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayoutManager layoutManager;
    Context context;
    private String whoAmI;


    //내가들어가볼 룸 아이디 : 5bdd5a72748b3c2e7cf71fd2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentsItems = new ArrayList<>();
        setContentView(R.layout.supporters_test_chat_layout);
        context = this;
        mMessagesView = (RecyclerView) findViewById(R.id.messages);
        mInputMessageView = (EditText_Helvatica_Meidum) findViewById(R.id.message_input);
        chatRoomExit = (LinearLayout) findViewById(R.id.chat_room_exit);
        ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);
        ImageView chatRoomBack = (ImageView)findViewById(R.id.chat_room_back);

        setView();



        chatRoomBack.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        chatRoomExit.setOnClickListener(this);
    }

    private void setRecyclerView(){
        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true);
        //layoutManager.setReverseLayout(true);
        mMessagesView.setLayoutManager(layoutManager);

        mAdapter = new MessageAdapter(context, new ArrayList<ChatContentsItem>());
        mMessagesView.setAdapter(mAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                selectFoodInfo(roomId, page);
            }
        };
        mMessagesView.addOnScrollListener(scrollListener);
        mMessagesView.scrollToPosition(0);
    }
    private void setView(){
        roomId = getIntent().getExtras().getString("roomId");
        MyLog.d("roomID : " + roomId);
        String owner = getIntent().getExtras().getString("owner");
        String participant = getIntent().getExtras().getString("participant");
        String participantMemberIconFileName = getIntent().getExtras().getString("participant_member_icon_file_name");
        String callActivity = getIntent().getExtras().getString("callActivity");
        if(participant.equals(((MyApp) getApplicationContext()).getMemberNickName())){
            whoAmI = "participant";
        }else{
            whoAmI = "owner";
        }

        if(callActivity.equals("ChatSupportersFragment")){
            ChatTalkData chatTalkData = new ChatTalkData();
            chatTalkData.setOwner(owner);
            chatTalkData.setOwnerMemberIconFileName(((MyApp)getApplicationContext()).getMemberIconFilename());
            chatTalkData.setParticipant(participant);
            chatTalkData.setParticipantMemberIconFileName(participantMemberIconFileName);
            newRoomFromAndroid(chatTalkData);
        }else{
            setRecyclerView();
            selectFoodInfo(roomId,0);
            setSocket();
        }

    }

    public void newRoomFromAndroid(ChatTalkData chatTalkData){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.newRoomFromAndroid(chatTalkData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    roomId = response.body();
                    setSocket();
                    setRecyclerView();
                    selectFoodInfo(roomId,0);
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
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_button) {
            attemptSend();
        } else if (v.getId() == R.id.chat_room_exit) {
            new AlertDialog.Builder(context)
                    .setTitle("나가기")
                    .setMessage("채팅방에서 나가시겠습니까?\n(상대방도 채팅방에서 나갈경우 대화내용이 전부 사라집니다.)")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyLog.d("whoAmI값 : " + whoAmI);
                            if(whoAmI.equals("participant")) chatRoomParticipantExit();
                            else chatRoomOwnerExit();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }else if(v.getId()==R.id.chat_room_back){
            finish();
        }
    }
    private void chatRoomParticipantExit() {
        RemoteService remoteService =
                ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.chatRoomParticipantExit(roomId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
        ((MyApp)getApplicationContext()).setChatExitButton(true);
        finish();
    }
    private void chatRoomOwnerExit() {
        RemoteService remoteService =
                ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.chatRoomOwnerExit(roomId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
        ((MyApp)getApplicationContext()).setChatExitButton(true);
        finish();
    }
    private void setSocket() {
        IO.Options options = new IO.Options();
        //((MyApp) getActivity().getApplicationContext()).getMemberNickName();
        options.path = "/socket.io";
        options.query = "r_var=" + roomId;
        options.multiplex = false;
        try {
            mSocket = IO.socket(Constant.NETWORK_URL + "chat", options);
            MyLog.d("액티비티 옵션 : " + options.query);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on("chat", onNewMessage);
        mSocket.connect();
    }

    private void attemptSend() {
        ChatContentsItem curItem = new ChatContentsItem();
        String message = mInputMessageView.getText().toString().trim();
        curItem.setChat(message);
        curItem.setUser(((MyApp) getApplicationContext()).getMemberNickName());
        curItem.setSender("asdf");
        curItem.setReceiver("qwer");
        mInputMessageView.setText("");
        sendChat(curItem);

    }

    private void addMessage(String sender, String receiver, String message, int sendOrReceive) {
        MyLog.d("addMessage 호출됨");
        if (sendOrReceive == 1) {// 보낸메세지
            mAdapter.addFirstItem(new ChatContentsItem.Builder(ChatContentsItem.TYPE_MESSAGE)
                    .sender(sender).receiver(receiver).message(message).build());
            mAdapter.notifyItemInserted(0);
            mMessagesView.scrollToPosition(0);

        } else if (sendOrReceive == 2) { //받은메세지
            MyLog.d("메세지 추가할 내용 : " + sender+"//"+receiver+"//"+message);
            MyLog.d("콘텐스아이템즈사이즈 : " + contentsItems.size());
            mAdapter.addFirstItem(new ChatContentsItem.Builder(ChatContentsItem.TYPE_RECEIVE)
                    .sender(sender).receiver(receiver).message(message).build());
            mAdapter.notifyItemInserted(0);
            // layoutManager.setReverseLayout(true);에의하여..
            //우선서버에서는 최신순인가?? sort({ "createdAt": -1})로다가.. 최근부터 차례대로 20개를가져온다
            //그럼이게 순서가0부터쌓이게되는데 setReverse에의하여.. 위에서부터찍지않고 아래애서부터찍긴한다
            //그런데.. 스크롤리스너가 먹히지않는것같음지금
            mMessagesView.scrollToPosition(0);
        }
    }

    public void sendChat(ChatContentsItem newChatItem) {
        RemoteService remoteService =
                ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.sendChat(roomId, newChatItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {
//                        if(null!=mUsername) mSocket.emit("add user", mUsername);
                        Toast.makeText(getApplicationContext(),
                                "???", Toast.LENGTH_LONG).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String chat = "";
                    String sender = "";
                    String receiver = "";
                    try {
                        chat = data.getString("chat");
                        sender = data.getString("sender");
                        receiver = data.getString("receiver");
                    } catch (JSONException e) {
                        return;
                    }
                    MyLog.d("이게두번?");
                    addMessage(sender, receiver, chat, 2);
                }
            });
        }
    };


    private void selectFoodInfo(String roomId,int page) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<ChatContentsItem>> call = remoteService.getChatContents(roomId,page);

        call.enqueue(new Callback<ArrayList<ChatContentsItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ChatContentsItem>> call, Response<ArrayList<ChatContentsItem>> response) {
                contentsItems = response.body();
                if (response.isSuccessful() && contentsItems != null) {
                    ((MessageAdapter) mAdapter).addItemList(contentsItems);
                } else {
                    //loadingText.setVisibility(View.VISIBLE);
                    //((TextView) findViewById(R.id.loading_text)).setText(R.string.loading_not);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ChatContentsItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off("chat", onNewMessage);
    }
}
