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
        setListView();
        listMyChat("asdf", 0);
    }

    private void setSocket() {
        IO.Options options = new IO.Options();
        options.path = "/socket.io";
        //((MyApp) getActivity().getApplicationContext()).getMemberNickName();
        options.query = "r_var=" + "asdf";
        options.multiplex=false;
        try {

            mSocket = IO.socket(Constant.CHAT_SERVER_URL + "room", options);
            MyLog.d("프래그먼트 옵션 : " +options.query);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on("newRoom", onNewRoom);
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyLog.d("여기들어옴?");
                    JSONObject data = (JSONObject) args[0];
                    MyLog.d("data값 : " + data);
                    String owner = "";
                    String participant = "";
                    try {
                        owner = data.getString("owner");
                        participant = data.getString("participant");
                    } catch (JSONException e) {
                        return;
                    }
                    MyLog.d("방생성완료 // 생성자 : " + owner + "//참가자 : " + participant);
                    ChatTalkData curData = new ChatTalkData();
                    curData.setOwner(owner);
                    curData.setParticipant(participant);
                    listAdapter.addFirst(curData);
                    listAdapter.notifyItemInserted(dataArrayList.size() - 1);
                    recyclerView.scrollToPosition(0);
                }
            });
        }
    };

    public void setListView() {
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        listAdapter = new ChatListAdapter(this.getActivity(), new ArrayList<ChatTalkData>());
        recyclerView.setAdapter(listAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listMyChat("asdf", page);
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
                    MyLog.d("이즈 석세스풀");
                    //여기에 dataArrayList로놓고 =list로놓으면 page=1일떄 또 요청하면서 빈값이되어버림 ㅡㅡ
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
