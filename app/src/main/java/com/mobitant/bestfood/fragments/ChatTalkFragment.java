package com.mobitant.bestfood.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.adapter.ChatListAdapter;
import com.mobitant.bestfood.item.ChatTalkData;

import java.util.ArrayList;

public class ChatTalkFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public int mPageNo;
    private SwipeMenuListView listView;
    private ArrayList<ChatTalkData> dataArrayList;
    private ChatListAdapter listAdapter;
    private ChatTalkData data;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        dataArrayList = new ArrayList<>();
        return inflater.inflate(R.layout.supporters_chat_activity_delete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (SwipeMenuListView) view.findViewById(R.id.chat_activity_listview);
        dataArrayList.add(data = new ChatTalkData("Eugene sent you a msg", "6386 keshlerin Canyon"));
        dataArrayList.add(data = new ChatTalkData("Hellie Jefferson", "937 Feeney Stravenue"));
        dataArrayList.add(data = new ChatTalkData("Eugene sent you a msg", "6386 keshlerin Canyon"));
        dataArrayList.add(data = new ChatTalkData("Hellie sent you a msg", "937 Feeney Stravenue"));
        dataArrayList.add(data = new ChatTalkData("Eugene sent you a msg", "6386 keshlerin Canyon"));
        dataArrayList.add(data = new ChatTalkData("Hellie sent you a msg", "937 Feeney Stravenue"));
        dataArrayList.add(data = new ChatTalkData("Eugene sent you a msg", "6386 keshlerin Canyon"));
        dataArrayList.add(data = new ChatTalkData("Hellie sent you a msg", "937 Feeney Stravenue"));

        listAdapter = new ChatListAdapter(this.getActivity(), dataArrayList);
        listView.setAdapter(listAdapter);

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        Toast.makeText(getContext(), "Delete", Toast.LENGTH_SHORT).show();
                        Log.e("item", String.valueOf(listView.getAdapter().getItem(position)));
                        Log.e("name", String.valueOf(dataArrayList.get(position).getName()));

                        dataArrayList.remove(position);

                        listAdapter.notifyDataSetChanged();

                        break;
                    case 1:
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
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

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getActivity().getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.parseColor("#F45557")));
            // set item width
            deleteItem.setWidth(150);
            deleteItem.setTitle("Delete");
            deleteItem.setTitleColor(Color.WHITE);
            deleteItem.setTitleSize(15);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };
}
