package com.mobitant.bestfood;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.mobitant.bestfood.fragments.BestFoodKeepFragment;
import com.mobitant.bestfood.fragments.BestFoodListFragment;
import com.mobitant.bestfood.fragments.LoginNickNameSettingFragment;
import com.mobitant.bestfood.lib.DialogLib;
import com.mobitant.bestfood.lib.GoLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.item.User;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener {
    private final String TAG = this.getClass().getSimpleName();
    public static final int fromHomeActivity = 1006;
    LinearLayout linearLayout, contentLayout, supportersLayout;
    DrawerLayout drawer;
    View headerLayout;
    User currentUser;
    CircleImageView profileIconImage;
    Menu menu;
    MenuItem menuItem;
    MenuItem profileMenuItem, logoutMenuItem, orderMenuItem;
    SliderLayout mDemoSlider;
    NavigationView navigationView;
    TextView nameText;
    Context context;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        context = this;
        ((MyApp) getApplicationContext()).setting = getSharedPreferences("setting", 0);
        ((MyApp) getApplicationContext()).editor = ((MyApp) getApplicationContext()).setting.edit();
        currentUser = ((MyApp) getApplicationContext()).getMemberInfoItem();
        setSlider();
        setToolBar();
        setNavigationView();
        //setNavLogin();onCreate랑 onResume이 앱이 시작될때 호출된다면 여기에 setNav를 할 필요가 없지않을까?
        setHomeMenu();
    }

    private void setSlider() {
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        mainSlider();
    }

    private void setNavigationView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);//툴바왼쪽3줄 생성
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);
    }

    private void setToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setHomeMenu() {
        linearLayout = (LinearLayout) findViewById(R.id.buy);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });
        contentLayout = (LinearLayout) findViewById(R.id.content_layout);
        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });
        supportersLayout = (LinearLayout) findViewById(R.id.go_supporters);
        supportersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MyApp) context.getApplicationContext()).getMemberNickname() == null
                        || ((MyApp) context.getApplicationContext()).equals("")) {
                    DialogLib.getInstance().goSupportersDialog(context);
                } else {
                    Intent intent = new Intent(context, SupportersActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                //finish();
            }
        });
    }

    public void mainSlider() {
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.s1);
        file_maps.put("2", R.drawable.s2);
        file_maps.put("3", R.drawable.s3);


        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }


    private void loginProcess(String email) {
        //정보 받아와서 setUseritem하기 위함임 밑에 m.subscription으로 로그인해서 set하는건 어떻게하는건가..알아야하는데..
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<User> call = remoteService.selectMemberInfo(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                User item = response.body();
                MyLog.d("홈액티비티 로그인프로세스 리스폰스값 : " + item);
                if (response.isSuccessful() && !StringLib.getInstance().isBlank(item.name)) { //널검사 , 응답성공검사
                    MyLog.d(TAG, "success " + response.body().toString());
                    ((MyApp) getApplicationContext()).setUserItem(item);
                    currentUser = item;
                    MyLog.d("홈액티비티 커렌트유저 : " + currentUser);
                    MyLog.d("홈액티비티 아이템  : " + item);
                    setProfileView();
                } else {
                    Toast.makeText(HomeActivity.this, "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    MyLog.d(TAG, "not success");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }

        });

    }

    /**
     * 오른쪽 상단 메뉴를 구성한다.
     * 닫기 메뉴만이 설정되어 있는 menu_close.xml를 지정한다.
     *
     * @param menu 메뉴 객체
     * @return 메뉴를 보여준다면 true, 보여주지 않는다면 false
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homeactivity_toolbar_menu, menu);
        return true;
    }


    /**
     * 프로필 정보는 별도 액티비티에서 변경될 수 있으므로
     * 변경을 바로 감지하기 위해 화면이 새로 보여질 대마다 setProfileView() 를 호출한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        currentUser = ((MyApp) getApplicationContext()).getMemberInfoItem();
        setProfileView();
        setNavLogin();

        /*
        setProfileView();이거는 서버요청하고 멤버아이콘파일내임을 가지고서 프로필을 갱신해야하므로
        setNavLogin이끝나는곳에 넣어줌 흠근데.. 프로필수정하고 홈으로왔을때 다시 실행시킬 수도 있기때문에..!
        여기도놓고 카카오로그인함수//일반유저로그인함수 요청끝나는부분에 추가하는게 좋을듯합니다~
        */

    }
// <====================네비게이션 필요한 메뉴들 시작======================>

    /**
     * 프로필 이미지와 프로필 이름을 설정한다.
     */
    private void setProfileView() {
        profileIconImage = (CircleImageView) headerLayout.findViewById(R.id.profile_icon);
        profileIconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                GoLib.getInstance().goProfileActivity(HomeActivity.this);
            }
        });
        MyLog.d("커렌트유저 : " + currentUser);
        if (StringLib.getInstance().isBlank(currentUser.memberIconFilename)) {
            Picasso.with(this).load(R.drawable.ic_person).into(profileIconImage);
        } else if (currentUser.memberIconFilename.length()>=30){
            Picasso.with(this).load(currentUser.memberIconFilename).into(profileIconImage);
        } else
        {
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
        orderMenuItem = menu.getItem(6);
        if (((MyApp) getApplicationContext()).setting.getBoolean("Auto_Login_enabled", false)) {
            //자동로그인이 선택된적이 있다면
            //아래문장은 홈액티비티 이후엔 실행하지 않도록만들자
            //이 아래의 elseif문장을 없애고
            loginProcess(((MyApp) getApplicationContext()).setting.getString("ID", ""));
            profileMenuItem.setVisible(true);
            menuItem.setVisible(false);
            logoutMenuItem.setVisible(true);
            orderMenuItem.setVisible(true);
        } else if (((MyApp) getApplicationContext()).setting.getBoolean("Auto_Login_enabled_Kakao", false)) {
            MyLog.d("카카오 일로오세욤");
            //아래문장은 홈액티비티 이후엔 실행하지 않도록 하자..
            isPastKaKaoLogin(((MyApp) getApplicationContext()).setting.getString("KakaoId",""),
                    ((MyApp) getApplicationContext()).setting.getString("KakaoNickName", ""));
            profileMenuItem.setVisible(true);
            menuItem.setVisible(false);
            logoutMenuItem.setVisible(true);
            orderMenuItem.setVisible(true);
        } else if (currentUser.nickname == null || currentUser.nickname.equals("")) { // 비회원
            menuItem.setVisible(true);
            menuItem.setTitle("로그인");
            profileMenuItem.setVisible(false);
            logoutMenuItem.setVisible(false);
            orderMenuItem.setVisible(false);
        } else { //자동로그인 클릭 안했을경우
            profileMenuItem.setVisible(true);
            menuItem.setVisible(false);
            logoutMenuItem.setVisible(true);
            orderMenuItem.setVisible(true);
        }

    }

    // <====================네비게이션 필요한 메뉴들 끝======================>
    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    protected void onNewIntent(Intent intent) {
        MyLog.d("onNewIntent() called.");

        if (intent != null) {
            processIntent(intent);
        }

        super.onNewIntent(intent);
    }


    /**
     * 수신자로부터 전달받은 Intent 처리
     *
     * @param intent
     */
    private void processIntent(Intent intent) {
        String from = intent.getStringExtra("from");
        if (from == null) {
            MyLog.d("from is null.");
            return;
        }

        String command = intent.getStringExtra("command");
        String type = intent.getStringExtra("type");
        String data = intent.getStringExtra("data");

        MyLog.d("DATA : " + command + ", " + type + ", " + data);
        MyLog.d("[" + from + "]로부터 수신한 데이터 : " + data);
    }


    private void requestLogout() {

        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HomeActivity.this, "정상적으로 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void isPastKaKaoLogin(String email,String name) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<User> call = remoteService.isPastKaKaoLogin(email,name);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                if (response.isSuccessful()) {
                    ((MyApp) getApplicationContext()).setUserItem(response.body());
                    currentUser = response.body();
                    if(currentUser == null) currentUser = new User();
                    MyLog.d("커렌트유저 : " + currentUser);
                    setProfileView();
                } else { // 등록 실패
                    MyLog.d(TAG, "response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }
}
