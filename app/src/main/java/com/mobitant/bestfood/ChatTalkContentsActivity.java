package com.mobitant.bestfood;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mobitant.bestfood.adapter.MessageAdapter;
import com.mobitant.bestfood.item.ChatContentsItem;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatTalkContentsActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private TextView tvMain;
    private EditText_Helvatica_Meidum mInputMessageView;
    private Button btnSubmit;
    private Socket mSocket;
    private ArrayList<ChatContentsItem> contentsItems;
    private String roomId;
    private RecyclerView mMessagesView;
    private Boolean isConnected = true;
    private RecyclerView.Adapter mAdapter;
    Context context;


    //내가들어가볼 룸 아이디 : 5bdd5a72748b3c2e7cf71fd2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomId = getIntent().getExtras().getString("roomId");
        setSocket();
        contentsItems = new ArrayList<>();
        setContentView(R.layout.supporters_test_chat_layout);
        context = this;
        mMessagesView = (RecyclerView) findViewById(R.id.messages);
        mInputMessageView = (EditText_Helvatica_Meidum) findViewById(R.id.message_input);

        MyLog.d("roomID : " + roomId);
        selectFoodInfo(roomId);

   /*     tvMain = findViewById(R.id.tvMain);
        etMsg = findViewById(R.id.etMsg);
        btnSubmit = findViewById(R.id.btnSubmit);*/


        ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });


    }

    private void setSocket() {
        IO.Options options = new IO.Options();
        //((MyApp) getActivity().getApplicationContext()).getMemberNickName();
        options.path = "/socket.io";
        options.query = "r_var=" + roomId;
        options.multiplex=false;
        try {
            mSocket = IO.socket(Constant.CHAT_SERVER_URL + "chat",options);
            MyLog.d("액티비티 옵션 : " +options.query);
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
        MyLog.d("애드메세지가두번?");
        if (sendOrReceive == 1) {// 보낸메세지
            contentsItems.add(new ChatContentsItem.Builder(ChatContentsItem.TYPE_MESSAGE)
                    .sender(sender).receiver(receiver).message(message).build());
            mAdapter.notifyItemInserted(contentsItems.size() - 1);
            mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);

        } else if (sendOrReceive == 2) { //받은메세지
            contentsItems.add(new ChatContentsItem.Builder(ChatContentsItem.TYPE_RECEIVE)
                    .sender(sender).receiver(receiver).message(message).build());
            mAdapter.notifyItemInserted(contentsItems.size() - 1);
            mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
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


    private void selectFoodInfo(String roomId) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<ChatContentsItem>> call = remoteService.getChatContents(roomId);

        call.enqueue(new Callback<ArrayList<ChatContentsItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ChatContentsItem>> call, Response<ArrayList<ChatContentsItem>> response) {
                ArrayList<ChatContentsItem> list = response.body();
                if (response.isSuccessful() && list != null) {
                    contentsItems = list;
                    mAdapter = new MessageAdapter(getApplicationContext(), contentsItems);
                    mMessagesView.setLayoutManager(new LinearLayoutManager(context));
                    mMessagesView.setAdapter(mAdapter);
                    mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
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
}
