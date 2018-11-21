package com.mobitant.bestfood;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.mobitant.bestfood.adapter.Food7_Adapter;
import com.mobitant.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.mobitant.bestfood.fragments.BestFoodListFragment;
import com.mobitant.bestfood.item.Food7_Model;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.ImageItem;
import com.mobitant.bestfood.item.OrderCheckItem;
import com.mobitant.bestfood.item.User;
import com.mobitant.bestfood.lib.DialogLib;
import com.mobitant.bestfood.lib.GoLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private final String TAG = this.getClass().getSimpleName();

    EndlessRecyclerViewScrollListener scrollListener;


    private RecyclerView recyclerView;
    private Food7_Adapter food7_adapter;
    // <====================네비게이션 필요한 메뉴들 시작======================>
    Menu menu;
    MenuItem menuItem;
    MenuItem profileMenuItem, logoutMenuItem;
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    View headerLayout;
    CircleImageView profileIconImage;
    // <====================네비게이션 필요한 메뉴들 끝======================>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food7);
        setToolbar();
        // <====================네비게이션 필요한 메뉴들 시작======================>
        setNavigationView();
        // <====================네비게이션 필요한 메뉴들 끝======================>

        recyclerView = findViewById(R.id.RecyclerView);
        setRecyclerView();
        getOrderHistroy(((MyApp)getApplicationContext()).getMemberNickName(),0);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homeactivity_toolbar_menu, menu);
        return true;
    }
    /**
     * 툴바를 설정한다.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("구매내역");
        }
    }
    /**
     * 왼쪽 화살표 메뉴(android.R.id.home)를 클릭했을 때와
     * 오른쪽 상단 메뉴를 클릭했을 때의 동작을 지정한다.
     * 여기서는 모든 버튼이 액티비티를 종료한다.
     * @param item 메뉴 아이템 객체
     * @return 메뉴를 처리했다면 true, 그렇지 않다면 false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_home:
                finish();
                break;
        }

        return true;
    }
    // <====================네비게이션 필요한 메뉴들 시작======================>

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setNavLogin();
        setProfileView();
    }


    public void setNavigationView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();//동적 메뉴아이템을위함
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);//동적 메뉴아이템을위함
    }


    /**
     * 네비게이션 메뉴를 클릭했을 때 호출되는 메소드
     *
     * @param item 메뉴 아이템 객체
     * @return 메뉴 클릭 이벤트의 처리 여부
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            GoLib.getInstance().goFragment(getSupportFragmentManager(),
                    R.id.home_linearlayout, BestFoodListFragment.newInstance());
            //GoLib.getInstance().goBestFoodMainActivity(this);
        } else if (id == R.id.nav_notice) {
            GoLib.getInstance().goRealNotificationActivity(this);
        } else if (id == R.id.nav_keep) {
            if (((MyApp) getApplication()).getMemberNickname() == null || ((MyApp) getApplication()).equals("")) {
                DialogLib.getInstance().inputPostDialog(this);
            } else {
                GoLib.getInstance().goKeepActivity(this);
            }
        } else if (id == R.id.nav_login) {
            GoLib.getInstance().goLoginActivity(this);
        } else if (id == R.id.nav_logout) {
            ((MyApp) getApplicationContext()).editor.remove("ID");
            ((MyApp) getApplicationContext()).editor.remove("PW");
            ((MyApp) getApplicationContext()).editor.remove("Auto_Login_enabled");
            ((MyApp) getApplicationContext()).editor.remove("KakaoEmail");
            ((MyApp) getApplicationContext()).editor.remove("KakaoNickName");
            ((MyApp) getApplicationContext()).editor.remove("Auto_Login_enabled_Kakao");
            ((MyApp) getApplicationContext()).editor.clear();
            ((MyApp) getApplicationContext()).editor.commit();
            requestLogout();
            ((MyApp) getApplicationContext()).setUserItem(new User());
            setNavLogin();
            setProfileView();
        } else if (id == R.id.nav_order) {
            GoLib.getInstance().goOrderHistoryActivity(this);
        } else if (id == R.id.nav_profile) {
            GoLib.getInstance().goProfileActivity(this);
        } else if (id == R.id.nav_question) {
            if (((MyApp) getApplicationContext()).getMemberNickname() == null || ((MyApp) getApplicationContext()).equals("")) {
                DialogLib.getInstance().inputPostDialog(this);
            } else {
                GoLib.getInstance().goNotificationActivity(this);
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setNavLogin() {
        menuItem = menu.getItem(3);
        logoutMenuItem = menu.getItem(4);
        profileMenuItem = menu.getItem(5);
        if (((MyApp) getApplication()).getMemberNickname() == null || ((MyApp) getApplication()).getMemberNickname().equals("")) { // 비회원
            menuItem.setVisible(true);
            menuItem.setTitle("로그인");
            profileMenuItem.setVisible(false);
            logoutMenuItem.setVisible(false);
        } else { //홈액티비티가 아니기때문에 카카오로그인여부,소모임회원여부,서버요청필요없이 띄워주면됨
            profileMenuItem.setVisible(true);
            menuItem.setVisible(false);
            logoutMenuItem.setVisible(true);
        }
    }

    /**
     * 프로필 이미지와 프로필 이름을 설정한다.
     */
    private void setProfileView() {
        profileIconImage = (CircleImageView) headerLayout.findViewById(R.id.profile_icon);
        profileIconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                GoLib.getInstance().goProfileActivity(OrderHistoryActivity.this);
            }
        });

        if (StringLib.getInstance().isBlank(((MyApp)getApplicationContext()).getMemberIconFilename())) {
            Picasso.with(this).load(R.drawable.ic_person).into(profileIconImage);
        } else {
            Picasso.with(this)
                    .load(RemoteService.MEMBER_ICON_URL + ((MyApp)getApplicationContext()).getMemberIconFilename())
                    .into(profileIconImage);
        }

        TextView nameText = (TextView) headerLayout.findViewById(R.id.header_name);

        if (((MyApp)getApplicationContext()).getMemberNickName() == null || ((MyApp)getApplicationContext()).getMemberNickName().equals("")) {
            nameText.setText("로그인해주세요");
            MyLog.d("로그인해주세요");
        } else {
            nameText.setText(((MyApp)getApplicationContext()).getMemberNickName());
            MyLog.d("currentUser.name : " +((MyApp)getApplicationContext()).getMemberNickName());
        }
    }

    private void requestLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OrderHistoryActivity.this, "정상적으로 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    // <====================네비게이션 필요한 메뉴들 끝======================>
    private void setRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderHistoryActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        food7_adapter = new Food7_Adapter(OrderHistoryActivity.this,  new ArrayList<OrderCheckItem>());
        recyclerView.setAdapter(food7_adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
               getOrderHistroy(((MyApp)getApplicationContext()).getMemberNickName(), page);
            }
        };
       recyclerView.addOnScrollListener(scrollListener);
    }

    private void getOrderHistroy(String memberNickName,final int currentPage) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<OrderCheckItem>> call = remoteService.getOrderHistory(memberNickName, currentPage);
        call.enqueue(new Callback<ArrayList<OrderCheckItem>>() {
            @Override
            public void onResponse(Call<ArrayList<OrderCheckItem>> call,
                                   Response<ArrayList<OrderCheckItem>> response) {
                ArrayList<OrderCheckItem> list = response.body();
                if (response.isSuccessful() && list != null) {
                   food7_adapter.addItemList(list);

                    /*if (infoListAdapter.getItemCount() == 0) {
                        noDataText.setVisibility(View.VISIBLE);
                    } else {
                        noDataText.setVisibility(View.GONE);
                    }*/
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OrderCheckItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

}
