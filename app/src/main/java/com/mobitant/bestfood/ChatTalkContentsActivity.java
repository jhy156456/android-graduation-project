package com.mobitant.bestfood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitant.bestfood.item.ChatContentsItem;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.MyToast;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatTalkContentsActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private TextView tvMain;
    private EditText etMsg;
    private Button btnSubmit;
    private Socket mSocket;
    private ArrayList<ChatContentsItem> contentsItems;
    private String roomId;
    private Boolean isConnected = true;

    //내가들어가볼 룸 아이디 : 5bdd5a72748b3c2e7cf71fd2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supporters_test_chat_layout);
        tvMain = findViewById(R.id.tvMain);
        etMsg = findViewById(R.id.etMsg);
        btnSubmit = findViewById(R.id.btnSubmit);


        roomId = "5bdd5a72748b3c2e7cf71fd2";
        selectFoodInfo(roomId);


        btnSubmit.setOnClickListener((view)->{
            ChatContentsItem curItem= new ChatContentsItem();
            curItem.setChat(etMsg.getText().toString());
            sendChat(curItem);
        });

        try {
            mSocket = IO.socket(Constant.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on("chat", onNewMessage);
        mSocket.connect();
    }
public void sendChat(ChatContentsItem newChatItem){
    RemoteService remoteService =
            ServiceGenerator.createService(RemoteService.class);

    Call<String> call = remoteService.sendChat(roomId,newChatItem);
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
                    MyLog.d("호출되나? ");
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
                    String chat ="";
                    try {
                        chat = data.getString("chat");
                    } catch (JSONException e) {
                        return;
                    }
                    MyLog.d("chat값 : " + chat);
                    tvMain.setText(tvMain.getText().toString()+ "     "+ chat);
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
                    MyLog.d("받은내용 : " + contentsItems);
                    for (int i = 0; i < contentsItems.size(); i++) {
                        MyLog.d("채팅들 : " + contentsItems.get(i).getChat());
                        tvMain.setText(tvMain.getText().toString() + "     " + contentsItems.get(i).getChat());
                    }
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
