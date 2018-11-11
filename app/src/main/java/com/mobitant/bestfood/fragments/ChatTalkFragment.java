package com.mobitant.bestfood.fragments;

import android.content.Context;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;


import com.mobitant.bestfood.Constant;

import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.adapter.ChatListAdapter;
import com.mobitant.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.mobitant.bestfood.item.ChatTalkData;
import com.mobitant.bestfood.item.User;
import com.mobitant.bestfood.lib.MyLog;
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

public class ChatTalkFragment extends Fragment {
    /*
    채팅방에서 나가기버튼클릭하고 나왔는데 상대방이 채팅을또보낼경우
    채팅방2개가생긴다 왜냐하면 newRoom핸들러에서 채팅방목록을다시받고
    어댑터에 추가시켜주는데 이미어댑터에있던거임
    */
    private final String TAG = this.getClass().getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    public int mPageNo;
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private ArrayList<ChatTalkData> dataArrayList;
    private ChatListAdapter listAdapter;
    private Socket mSocket;
    private Boolean isConnected = true;
    EndlessRecyclerViewScrollListener scrollListener;

    public ChatTalkFragment() {
    }

    public static ChatTalkFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        ChatTalkFragment fragment = new ChatTalkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off("newRoom", onNewRoom);
        mSocket.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        dataArrayList = new ArrayList<>();
        return inflater.inflate(R.layout.supporters_chat_activity_delete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSocket();
        recyclerView = (RecyclerView) view.findViewById(R.id.chat_activity_listview);
        setRecyclerView();

        listMyChat(((MyApp) getActivity().getApplicationContext()).getMemberNickName(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        //이게 newRoom핸들러 호출되면 실행이되버린다;;
        MyLog.d("onResume() 호출됨");
        if (((MyApp) getActivity().getApplication()).getChatExitButton()) {
            MyLog.d("onResume()안의 if문장  호출됨");
            setRecyclerView();
            listMyChat(((MyApp) getActivity().getApplicationContext()).getMemberNickName(), 0);
            ((MyApp) getActivity().getApplication()).setChatExitButton(false);
            setSocket();
        }

    }

    private void setSocket() {
        IO.Options options = new IO.Options();
        options.path = "/socket.io";
        //((MyApp) getActivity().getApplicationContext()).getMemberNickName();
        options.query = "r_var=" + ((MyApp) getActivity().getApplicationContext()).getMemberNickName();
        options.multiplex = false;
        try {
            mSocket = IO.socket(Constant.NETWORK_URL + "room", options);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on("newRoom", onNewRoom);
        mSocket.on("lastChatReceive", lastChatReceive);
        mSocket.connect();
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {
//                        if(null!=mUsername) mSocket.emit("add user", mUsername);
                        Toast.makeText(getContext(),
                                "연결설정완료?", Toast.LENGTH_LONG).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onNewRoom = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String owner = "";
                    String participant = "";
                    String id = "";
                    String lastChat = "";
                    try {
                        owner = data.getString("owner");
                        participant = data.getString("participant");
                        id = data.getString("_id");
                        lastChat = data.getString("last_chat_contents");
                    } catch (JSONException e) {
                        return;
                    }
                    ChatTalkData curData = new ChatTalkData();
                    curData.setLast_chat_contents(lastChat);
                    curData.setOwner(owner);
                    curData.setParticipant(participant);
                    curData.setId(id);
                    listAdapter.addFirst(curData);
                    listAdapter.notifyItemInserted(0);
                    recyclerView.scrollToPosition(0);
                }
            });
        }
    };
    private Emitter.Listener lastChatReceive = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    MyLog.d("lastChatReceive의 run함수 호출");
                    JSONObject data = (JSONObject) args[0];
                    MyLog.d("data값 : " + data);
                    String last_chat_contents = "";
                    String id = "";
                    try {
                        last_chat_contents = data.getString("last_chat_contents");
                        id = data.getString("_id");
                    } catch (JSONException e) {
                        return;
                    }
                    for (int i = 0; i < listAdapter.getItemCount(); i++) {
                        MyLog.d("listAdapter.getItem(i).getId() : " + listAdapter.getItem(i).getId());
                        MyLog.d("채팅온 룸의 아이디 : " + id);
                        if (listAdapter.getItem(i).getId().equals(id)) {
                            listAdapter.modifyLastChatContents(i, last_chat_contents);
                        }
                    }
                }
            });
        }
    };

    public void setRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        listAdapter = new ChatListAdapter(getContext(), new ArrayList<ChatTalkData>());
        recyclerView.setAdapter(listAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listMyChat(((MyApp) getActivity().getApplicationContext()).getMemberNickName(), page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void listMyChat(String userNickName, int page) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<ChatTalkData>> call = remoteService.listMyChat(userNickName, page);
        call.enqueue(new Callback<ArrayList<ChatTalkData>>() {
            @Override
            public void onResponse(Call<ArrayList<ChatTalkData>> call,
                                   Response<ArrayList<ChatTalkData>> response) {
                ArrayList<ChatTalkData> list = response.body();
                if (response.isSuccessful() && list != null) {
                    //여기에 dataArrayList로놓고 =list로놓으면 page=1일떄 또 요청하면서 빈값이되어버림 ㅡㅡ
                    for (int i = 0; i < list.size(); i++) {
                        String userNickName = ((MyApp) getActivity().getApplicationContext()).getMemberNickName();
                        if (userNickName.equals(list.get(i).getParticipant()))
                            list.get(i).setNowAdapterRoomNickName("participant");
                        else list.get(i).setNowAdapterRoomNickName("owner");
                        if (list.get(i).getNowAdapterRoomNickName().equals("participant") && (!list.get(i).isParticipant_is_exit())) {
                            continue;
                        } else if (list.get(i).getNowAdapterRoomNickName().equals("owner") && (!list.get(i).isOwner_is_exit())) {
                            continue;
                        }
                        list.remove(i);
                    }
                    listAdapter.addItemList(list);

                   /* if (supportersChatAdapter.getItemCount() == 0) {
                        noDataText.setVisibility(View.VISIBLE);
                    } else {
                        noDataText.setVisibility(View.GONE);
                    }*/
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ChatTalkData>> call, Throwable t) {
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
