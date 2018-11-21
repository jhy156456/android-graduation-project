package com.mobitant.bestfood;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.mobitant.bestfood.fragments.BestFoodListFragment;
import com.mobitant.bestfood.fragments.ChatTalkFragment;
import com.mobitant.bestfood.fragments.FirstFragment;
import com.mobitant.bestfood.fragments.ChatSupportersFragment;
import com.mobitant.bestfood.item.User;
import com.mobitant.bestfood.lib.DialogLib;
import com.mobitant.bestfood.lib.GoLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SupportersActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    public static final int fromHomeActivity = 1007;
    private TabLayout mTabLayout;
    // <==== Toolbar와 NavigationMenu를 위한 전역변수 선언===>
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawer;
    Menu menu;
    MenuItem menuItem;
    MenuItem profileMenuItem, logoutMenuItem;
    CircleImageView profileIconImage;
    View headerLayout;
    User currentUser;
    TextView nameText;
    // <==== Toolbar와 NavigationMenu를 위한 전역변수 선언===>
    private int[] mTabsIcons = {
            R.drawable.message,
            R.drawable.chat};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supporters_activity_main);
        currentUser = ((MyApp) getApplication()).getMemberInfoItem();
        setToolbar();
        setNavigationView();
        //setNavLogin(); onCreate랑 onResume이 앱이 시작될때 호출된다면 여기에 setNav를 할 필요가 없지않을까?
        setViewPagerAndTabLayout();

    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = ((MyApp) getApplication()).getMemberInfoItem();
        setNavLogin();
        setProfileView();
    }

    // <====================네비게이션 필요한 메뉴들 시작 ======================>

    /**
     * 폰에서 뒤로가기 버튼을 클릭했을 때 호출하는 메소드이며
     * 네비게이션 메뉴가 보여진 상태일 경우, 네비게이션 메뉴를 닫는다.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 오른쪽 상단 메뉴를 구성한다.
     * 닫기 메뉴만이 설정되어 있는 menu_close.xml를 지정한다.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homeactivity_toolbar_menu, menu);
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
            ((MyApp) getApplicationContext()).editor.remove("KakaoEmail");
            ((MyApp) getApplicationContext()).editor.remove("KakaoNickName");
            ((MyApp) getApplicationContext()).editor.remove("Auto_Login_enabled_Kakao");
            ((MyApp) getApplicationContext()).editor.clear();
            ((MyApp) getApplicationContext()).editor.commit();
            currentUser = new User();
            requestLogout();
            ((MyApp) getApplicationContext()).setUserItem(currentUser);
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
    private void requestLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SupportersActivity.this, "정상적으로 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
                GoLib.getInstance().goProfileActivity(getApplicationContext());
            }
        });
        if (StringLib.getInstance().isBlank(currentUser.memberIconFilename)) {
            Picasso.with(this).load(R.drawable.ic_person).into(profileIconImage);
        } else {
            Picasso.with(this)
                    .load(RemoteService.MEMBER_ICON_URL + currentUser.memberIconFilename)
                    .into(profileIconImage);
        }
        nameText = (TextView) headerLayout.findViewById(R.id.header_name);
        if (currentUser.nickname == null || currentUser.nickname.equals("")) {
            nameText.setText("로그인해주세요");
            MyLog.d("로그인해주세요");
        } else {
            nameText.setText(currentUser.nickname);
            MyLog.d("currentUser.name : " + currentUser.name);
        }
    }

    private void setNavigationView() {
        drawer = (DrawerLayout) findViewById(R.id.supporters_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        /*
        setToolbar함수에서 ToolBar toolbar변수를 새로생성하고 R.id값을 부여해서
        왼쪽3줄버튼이 생성되지않았었다..
        */
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);
    }

    /**
     * 툴바를 설정한다.
     */
    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("서포터즈");
        }
    }

    // <====================네비게이션 필요한 메뉴들 시작 끝======================>


    private void setViewPagerAndTabLayout() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        if (viewPager != null)
            viewPager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }
            mTabLayout.getTabAt(0).getCustomView().setSelected(true);
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public final int PAGE_COUNT = 2;
        private final String[] mTabsTitle = {"Supporters", "Messages"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            View view = LayoutInflater.from(SupportersActivity.this).inflate(R.layout.fuck_toolbar, null);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(mTabsIcons[position]);
            return view;
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return ChatSupportersFragment.newInstance(1);
                case 1:
                    return ChatTalkFragment.newInstance(2);
                //case 2:
                // return ChatSupportersFragment.newInstance(3);
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsTitle[position];
        }
    }


}