package com.mobitant.bestfood;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mobitant.bestfood.fragments.BestFoodKeepFragment;
import com.mobitant.bestfood.fragments.BestFoodListFragment;
import com.mobitant.bestfood.fragments.NotificationListFragment;
import com.mobitant.bestfood.fragments.NotificationRegisterFragment;
import com.mobitant.bestfood.lib.GoLib;
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
    MenuItem profileMenuItem,logoutMenuItem;
    NavigationView navigationView;
    User userItem;
    CircleImageView profileIconImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userItem = ((MyApp)getApplication()).getUserItem();


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

        setNavLogin();
        GoLib.getInstance()
                .goFragment(getSupportFragmentManager(), R.id.notification_change_fragment,
                        NotificationListFragment.newInstance());

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
            case R.id.go_home:
                GoLib.getInstance().goHomeActivity(this);
            case R.id.go_notification_write:
                GoLib.getInstance().goFragmentBack(getSupportFragmentManager(),
                        R.id.notification_change_fragment, NotificationRegisterFragment.newInstance());
        }
        return super.onOptionsItemSelected(item);
    }
    public void setNavLogin(){
        menuItem = menu.getItem(4);
        profileMenuItem = menu.getItem(6);
        logoutMenuItem = menu.getItem(5);
        if (((MyApp) getApplication()).getMemberNickname() == null || ((MyApp) getApplication()).getMemberNickname().equals("")) {
            menuItem.setTitle("로그인");
            profileMenuItem.setVisible(false);
            logoutMenuItem.setVisible(false);
        } else {
            profileMenuItem.setVisible(true);
            menuItem.setVisible(false);
            logoutMenuItem.setVisible(true);
        }


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
        } else {
            Picasso.with(this)
                    .load(RemoteService.MEMBER_ICON_URL + userItem.memberIconFilename)
                    .into(profileIconImage);
        }

        TextView nameText = (TextView) headerLayout.findViewById(R.id.name);

        if (userItem.name == null || userItem.name.equals("")) {
            nameText.setText(R.string.name_need);
        } else {
            nameText.setText(userItem.name);
        }
    }

    /**
     * 네비게이션 메뉴를 클릭했을 때 호출되는 메소드
     * @param item 메뉴 아이템 객체
     * @return 메뉴 클릭 이벤트의 처리 여부
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            GoLib.getInstance().goFragment(getSupportFragmentManager(),
                    R.id.content_main, BestFoodListFragment.newInstance());

        } else if (id == R.id.nav_keep) {
            GoLib.getInstance().goFragment(getSupportFragmentManager(),
                    R.id.content_main, BestFoodKeepFragment.newInstance());

        } else if (id == R.id.nav_register) {
            GoLib.getInstance().goBestFoodRegisterActivity(this,fromNotificationActivity);

        } else if (id == R.id.nav_profile) {
            GoLib.getInstance().goProfileActivity(this);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
