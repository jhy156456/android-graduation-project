package com.mobitant.bestfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.mobitant.bestfood.fragments.BestFoodKeepFragment;
import com.mobitant.bestfood.fragments.BestFoodListFragment;
import com.mobitant.bestfood.fragments.NotificationListFragment;
import com.mobitant.bestfood.fragments.NotificationRegisterFragment;
import com.mobitant.bestfood.lib.DialogLib;
import com.mobitant.bestfood.lib.GoLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.item.User;
import com.mobitant.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int fromNotificationActivity = 1004;
    DrawerLayout drawer;
    View headerLayout;
    Menu menu;
    MenuItem menuItem;
    MenuItem profileMenuItem, logoutMenuItem;
    NavigationView navigationView;
    Toolbar toolbar;
    User userItem;
    CircleImageView profileIconImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);


        userItem = ((MyApp) getApplication()).getUserItem();

        setToolBar();
        setNavigationView();


        setNavLogin();
        GoLib.getInstance()
                .goFragment(getSupportFragmentManager(), R.id.notification_change_fragment,
                        NotificationListFragment.newInstance());

    }
    /**
     * 툴바를 설정한다.
     */
    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("문의하기");
        }
    }
    // <====================네비게이션 필요한 메뉴들 시작======================>


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
     * 오른쪽 상단 메뉴를 구성한다.
     * 닫기 메뉴만이 설정되어 있는 menu_close.xml를 지정한다.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notificationactivity_toolbar_menu, menu);
        return true;
    }

    /**
     * 왼쪽 화살표 메뉴(android.R.id.home)를 클릭했을 때와
     * 오른쪽 상단 닫기 메뉴를 클릭했을 때의 동작을 지정한다.
     * 여기서는 모든 버튼이 액티비티를 종료한다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.go_home:
                GoLib.getInstance().goHomeActivity(this);*/
            case R.id.go_notification_write:
                GoLib.getInstance().goFragmentBack(getSupportFragmentManager(),
                        R.id.notification_change_fragment, NotificationRegisterFragment.newInstance());
        }
        return super.onOptionsItemSelected(item);
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
            ((MyApp) getApplicationContext()).editor.remove("KakaoId");
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
                GoLib.getInstance().goProfileActivity(NotificationActivity.this);
            }
        });

        if (StringLib.getInstance().isBlank(userItem.memberIconFilename)) {
            Picasso.with(this).load(R.drawable.ic_person).into(profileIconImage);
        }
        else if (userItem.memberIconFilename.length()>=30){
            Picasso.with(this).load(userItem.memberIconFilename).into(profileIconImage);
        }
        else {
            Picasso.with(this)
                    .load(RemoteService.MEMBER_ICON_URL + userItem.memberIconFilename)
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
                        Toast.makeText(NotificationActivity.this, "정상적으로 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    /**
     * 프로필 정보는 별도 액티비티에서 변경될 수 있으므로
     * 변경을 바로 감지하기 위해 화면이 새로 보여질 대마다 setProfileView() 를 호출한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setNavLogin();
        setProfileView();
    }
// <====================네비게이션 필요한 메뉴들 끝======================>



    /**
     * 다른 액티비티를 실행한 결과를 처리하는 메소드
     * (실제로는 프래그먼트로 onActivityResult 호출을 전달하기 위한 목적으로 작성)
     *
     * @param requestCode 액티비티를 실행하면서 전달한 요청 코드
     * @param resultCode  실행한 액티비티가 설정한 결과 코드
     * @param data        결과 데이터
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
