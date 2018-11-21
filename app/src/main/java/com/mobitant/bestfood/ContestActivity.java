package com.mobitant.bestfood;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.adapter.CategoryPagerAdapterProductGrid;
import com.mobitant.bestfood.fragments.BestFoodListFragment;
import com.mobitant.bestfood.fragments.NotificationRegisterFragment;
import com.mobitant.bestfood.item.User;
import com.mobitant.bestfood.lib.DialogLib;
import com.mobitant.bestfood.lib.GoLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContestActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private TabLayout tabLayout;
    private Typeface mTypeface;
    private Typeface mTypefaceBold;
    public static final int fromContest = 1001;
    CategoryPagerAdapterProductGrid adapter;
    ViewPager viewPager1;
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
        setContentView(R.layout.activity_v_commerce_product_list);

        setToolBar();
        // <====================네비게이션 필요한 메뉴들 시작======================>
        setNavigationView();
        // <====================네비게이션 필요한 메뉴들 끝======================>
        tabLayout = findViewById(R.id.tab_layout);

//        View root = tabLayout.getChildAt(0);
//        if (root instanceof LinearLayout) {
//            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//            GradientDrawable drawable = new GradientDrawable();
//            drawable.setColor(getResources().getColor(R.color.line));
//            drawable.setSize(2, 1);
//            ((LinearLayout) root).setDividerPadding(10);
//            ((LinearLayout) root).setDividerDrawable(drawable);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText("조회"));
        tabLayout.addTab(tabLayout.newTab().setText("즐겨찾기"));
        tabLayout.addTab(tabLayout.newTab().setText("등록"));
      /*  Typeface mTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
                }
            }
        }*/
        setCustomFontAndStyle(tabLayout, 0);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager1 = (ViewPager) findViewById(R.id.pager);

        setAdapter();

        //폰트 입히기위해 필요한듯? Flipy앱에서 가져옴
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                TextView tabTextView = new TextView(this);
                tab.setCustomView(tabTextView);
                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.setText(tab.getText());
                tabTextView.setTextColor(Color.parseColor("#acacac"));
                // First tab is the selected tab, so if i==0 then set BOLD typeface
                if (i == 0) {
                    tabTextView.setTypeface(null, Typeface.BOLD);
                    tabTextView.setTextColor(Color.parseColor("#000000"));
                }

            }

        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager1.setCurrentItem(tab.getPosition());
                TextView textView = (TextView) tab.getCustomView();
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setTypeface(null, Typeface.BOLD);
                //setCustomFontAndStyle(tabLayout, tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView();
                text.setTextColor(Color.parseColor("#acacac"));
                text.setTypeface(null, Typeface.NORMAL);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setAdapter() {
        adapter = new CategoryPagerAdapterProductGrid(getSupportFragmentManager(), 3);
        viewPager1.setAdapter(adapter);
        viewPager1.setOffscreenPageLimit(1);
        viewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


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
                break;
            case R.id.go_notification_write:
                GoLib.getInstance().goBestFoodRegisterActivity(this, fromContest);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (((MyApp) getApplicationContext()).isNewContest()) {
            setAdapter();
            ((MyApp) getApplicationContext()).setNewContest(false);
        }
        setNavLogin();
        setProfileView();
    }

    private void setCustomFontAndStyle(TabLayout tabLayout, Integer position) {
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        mTypefaceBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    if (j == position) {
                        ((TextView) tabViewChild).setTypeface(mTypefaceBold, Typeface.NORMAL);
                    } else {
                        ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
                    }
                }
            }
        }

    }

    /**
     * 툴바를 설정한다.
     */
    private void setToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("같이해요");
        }
    }
    // <====================네비게이션 필요한 메뉴들 시작======================>



    public void setNavigationView() {
        drawer = (DrawerLayout) findViewById(R.id.contest_drawer_layout);
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
                GoLib.getInstance().goProfileActivity(ContestActivity.this);
            }
        });

        if (StringLib.getInstance().isBlank(((MyApp)getApplicationContext()).getMemberIconFilename())) {
            Picasso.with(this).load(R.drawable.ic_person).into(profileIconImage);
        } else if (((MyApp)getApplicationContext()).getMemberIconFilename().length()>=30){
            Picasso.with(this).load(((MyApp)getApplicationContext()).getMemberIconFilename()).into(profileIconImage);
        }

        else {
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
                        Toast.makeText(ContestActivity.this, "정상적으로 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    // <====================네비게이션 필요한 메뉴들 끝======================>





}
