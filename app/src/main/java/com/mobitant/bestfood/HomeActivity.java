package com.mobitant.bestfood;

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

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener {
    private final String TAG = this.getClass().getSimpleName();
    public static final int fromHomeActivity = 1006;
    LinearLayout linearLayout,contentLayout,supportersLayout;
    DrawerLayout drawer;
    View headerLayout;
    User currentUser;
    CircleImageView profileIconImage;
    Menu menu;
    MenuItem menuItem;
    MenuItem profileMenuItem, logoutMenuItem;
    SliderLayout mDemoSlider;
    NavigationView navigationView;
    TextView nameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        currentUser = ((MyApp) getApplication()).getMemberInfoItem();
requestLogout();
        //슬라이더시작
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        mainSlider();

//슬라이더끝

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);


        ((MyApp) getApplication()).setting = getSharedPreferences("setting", 0);
        ((MyApp) getApplication()).editor = ((MyApp) getApplication()).setting.edit();


        setNavLogin();

        linearLayout = (LinearLayout) findViewById(R.id.buy);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        contentLayout = (LinearLayout) findViewById(R.id.content_layout);
        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ContestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        supportersLayout =(LinearLayout)findViewById(R.id.go_supporters);
        supportersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SupportersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


    }

public void mainSlider(){
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
    public void setNavLogin() {
        menuItem = menu.getItem(4);
        profileMenuItem = menu.getItem(6);
        logoutMenuItem = menu.getItem(5);

        if (((MyApp) getApplication()).setting.getBoolean("Auto_Login_enabled", false)) {
            loginProcess(((MyApp) getApplication()).setting.getString("ID", ""));
            profileMenuItem.setVisible(true);
            menuItem.setVisible(false);
            logoutMenuItem.setVisible(true);
        } else if (currentUser.nickname == null || currentUser.nickname.equals("")) { // 비회원
            menuItem.setVisible(true);
            menuItem.setTitle("로그인");
            profileMenuItem.setVisible(false);
            logoutMenuItem.setVisible(false);
        } else{ //자동로그인 클릭 안했을경우
            profileMenuItem.setVisible(true);
            menuItem.setVisible(false);
            logoutMenuItem.setVisible(true);
        }


    }





    private void loginProcess(String email) {

        //정보 받아와서 setUseritem하기 위함임 밑에 m.subscription으로 로그인해서 set하는건 어떻게하는건가..알아야하는데..
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<User> call = remoteService.selectMemberInfo(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                User item = response.body();

                if (response.isSuccessful() && !StringLib.getInstance().isBlank(item.name)) { //널검사 , 응답성공검사
                    MyLog.d(TAG, "success " + response.body().toString());
                    ((MyApp)getApplication()).setUserItem(item);
                    currentUser = item;
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
     * 왼쪽 화살표 메뉴(android.R.id.home)를 클릭했을 때와
     * 오른쪽 상단 닫기 메뉴를 클릭했을 때의 동작을 지정한다.
     * 여기서는 모든 버튼이 액티비티를 종료한다.
     *
     * @param item 메뉴 아이템 객체
     * @return 메뉴를 처리했다면 true, 그렇지 않다면 false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //close();
                break;

            case R.id.action_submit:
                //save();
                break;
        }

        return true;
    }

    /**
     * 프로필 정보는 별도 액티비티에서 변경될 수 있으므로
     * 변경을 바로 감지하기 위해 화면이 새로 보여질 대마다 setProfileView() 를 호출한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        currentUser = ((MyApp) getApplication()).getMemberInfoItem();
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
                GoLib.getInstance().goProfileActivity(HomeActivity.this);
            }
        });

        if (StringLib.getInstance().isBlank(currentUser.memberIconFilename)) {
            Picasso.with(this).load(R.drawable.ic_person).into(profileIconImage);
        } else {
            Picasso.with(this)
                    .load(RemoteService.MEMBER_ICON_URL + currentUser.memberIconFilename)
                    .into(profileIconImage);
        }

        nameText = (TextView) headerLayout.findViewById(R.id.name);

        if (currentUser.nickname == null || currentUser.nickname.equals("")) {
            nameText.setText("로그인해주세요");
        } else {
            nameText.setText(currentUser.name);
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
            GoLib.getInstance().goNotificationActivity(this);
        } else if (id == R.id.nav_keep) {

            GoLib.getInstance().goKeepActivity(this);

        } else if (id == R.id.nav_register) {
            if (currentUser.nickname == null || currentUser.nickname.equals("")) {


            }
            GoLib.getInstance().goBestFoodRegisterActivity(this,fromHomeActivity);

        } else if (id == R.id.nav_login) {
            GoLib.getInstance().goLoginActivity(this);
        }else if(id==R.id.nav_logout) {
            ((MyApp)getApplication()).editor.remove("ID");
            ((MyApp)getApplication()).editor.remove("PW");
            ((MyApp)getApplication()).editor.remove("Auto_Login_enabled");
            ((MyApp)getApplication()).editor.clear();
            ((MyApp)getApplication()).editor.commit();

            currentUser = new User();
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            ((MyApp)getApplication()).setUserItem(currentUser);
            setNavLogin();
            setProfileView();
        }

        else if (id == R.id.nav_profile) {
            GoLib.getInstance().goProfileActivity(this);
        }else if (id==R.id.nav_question){
            if (((MyApp)getApplication()).getMemberNickname() == null || ((MyApp)getApplication()).equals("")) {
                DialogLib.getInstance().inputPostDialog(this);
            }else{
                GoLib.getInstance().goNotificationActivity(this);
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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
                        Toast.makeText(HomeActivity.this, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
