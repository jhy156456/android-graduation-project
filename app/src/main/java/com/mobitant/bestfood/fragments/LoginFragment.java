package com.mobitant.bestfood.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.auth.AuthType;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import com.mobitant.bestfood.HomeActivity;
import com.mobitant.bestfood.MainActivity;
import com.mobitant.bestfood.MainActivity2;
import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;

import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.model.Response;
import com.mobitant.bestfood.item.User;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;
import com.mobitant.bestfood.utils.Constants;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.mobitant.bestfood.utils.Validation.validateEmail;
import static com.mobitant.bestfood.utils.Validation.validateFields;

public class LoginFragment extends Fragment {

    public static final String TAG = LoginFragment.class.getSimpleName();

    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtLogin;
    private TextView mTvRegister;
    private TextView mTvForgotPassword;
    private TextInputLayout mTiEmail;
    private TextInputLayout mTiPassword;
    private ProgressBar mProgressBar;
    private SessionCallback mKakaocallback;
    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;
    CheckBox autoLogin;
    private LoginButton kakaoButton;
    private SessionCallback callback;
    private User kakaoUser;
    public static int kakaoOnSucessCount;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        callback = new SessionCallback();//아래와 이코드가 setonclick안에있으면 callback이 진행되지않았음...
        Session.getCurrentSession().addCallback(callback);
        kakaoOnSucessCount = 0;
        mSubscriptions = new CompositeSubscription();
        initViews(view);
        initSharedPreferences();
        return view;
    }


    private void initViews(View v) {

        mEtEmail = (EditText) v.findViewById(R.id.et_email);
        mEtPassword = (EditText) v.findViewById(R.id.et_password);
        mBtLogin = (Button) v.findViewById(R.id.btn_login);
        mTiEmail = (TextInputLayout) v.findViewById(R.id.ti_email);
        mTiPassword = (TextInputLayout) v.findViewById(R.id.ti_password);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress);
        mTvRegister = (TextView) v.findViewById(R.id.tv_register);
        mTvForgotPassword = (TextView) v.findViewById(R.id.tv_forgot_password);
        kakaoButton = (LoginButton) v.findViewById(R.id.com_kakao_login);

        kakaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Session.getCurrentSession().checkAndImplicitOpen();
            }
        });

        //자동로그인 구현시작
        //fragment라 this.getActivity()추가
        autoLogin = (CheckBox) v.findViewById(R.id.select_autologin);
        ((MyApp) getActivity().getApplicationContext()).setting = this.getActivity().getSharedPreferences("setting", 0);
        ((MyApp) getActivity().getApplicationContext()).editor = ((MyApp) getActivity().getApplicationContext()).setting.edit();
        //자동로그인 구현 끝

        mBtLogin.setOnClickListener(view -> login());
        mTvRegister.setOnClickListener(view -> goToRegister());
        mTvForgotPassword.setOnClickListener(view -> showDialog());
        //Enter key Action
        mEtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    login();
                    return true;
                }
                return false;
            }
        });

        mEtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        login();
                        return true;
                }
                return false;
            }
        });
    }

    private void initSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }


    private void login() {

        setError();
        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();
        int err = 0;
        if (!validateEmail(email)) {
            err++;
            mTiEmail.setError("이메일이 유효하지 않습니다.");
        }
        if (!validateFields(password)) {
            err++;
            mTiPassword.setError("비밀번호를 입력하세요");
        }
        if (err == 0) {
            if (autoLogin.isChecked() == true) {
                ((MyApp) getActivity().getApplicationContext()).editor.putString("ID", email);
                ((MyApp) getActivity().getApplicationContext()).editor.putString("PW", password);
                ((MyApp) getActivity().getApplicationContext()).editor.putBoolean("Auto_Login_enabled", true);
                ((MyApp) getActivity().getApplicationContext()).editor.commit();
            }
            loginProcess(email, password);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            showSnackBarMessage("Enter Valid Details !");
        }
    }

    private void setError() {
        mTiEmail.setError(null);
        mTiPassword.setError(null);
    }

    private void loginProcess(String email, String password) {

        //정보 받아와서 setUseritem하기 위함임 밑에 m.subscription으로 로그인해서 set하는건 어떻게하는건가..알아야하는데..
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<User> call = remoteService.selectMemberInfo(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                User item = response.body();

                if (response.isSuccessful() && !StringLib.getInstance().isBlank(item.name)) { //널검사 , 응답성공검사
                    MyLog.d(TAG, "success " + response.body().toString());
                    ((MyApp) getActivity().getApplicationContext()).setUserItem(item);
                    MyLog.d("id값 : " + item.id);
                    //setMemberInfoItem(item); //조회 했다면 메인액티비티2(로그인화면)으로
                } else {
                    MyLog.d(TAG, "not success");
                    // goProfileActivity(item); // 조회하지 못하였다면 프로필액티비티로
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }

        });
        mSubscriptions.add(ServiceGenerator.getRetrofit(email, password).login()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {
        mProgressBar.setVisibility(View.GONE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.TOKEN, response.getToken());
        editor.putString(Constants.EMAIL, response.getMessage());
        editor.apply();
        mEtEmail.setText(null);
        mEtPassword.setText(null);
        getActivity().finish();

    }

    private void handleError(Throwable error) {
        mProgressBar.setVisibility(View.GONE);
        if (error instanceof HttpException) {
            Gson gson = new GsonBuilder().create();
            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
                showSnackBarMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showSnackBarMessage("Network Error !");
        }
    }

    private void showSnackBarMessage(String message) {

        if (getView() != null) {

            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void goToRegister() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        RegisterFragment fragment = new RegisterFragment();
        ft.replace(R.id.fragmentFrame, fragment, RegisterFragment.TAG);
        ft.commit();
    }

    private void showDialog() {
        ResetPasswordDialog fragment = new ResetPasswordDialog();
        fragment.show(getFragmentManager(), ResetPasswordDialog.TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*
        아래코드를 추가하니까 카카오 로그인 후 onsucess가 2번호출되는게 없어졌다
        원리는모르겠고.. 분명히 서버요청도 1번만하게해서 프래그먼트 변경도 1번만실행될텐데
        왜 앱이 중지되는지도 모르겠다 ㅠㅠ
        */
        Session.getCurrentSession().removeCallback(callback);
        mSubscriptions.unsubscribe();
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
        }

        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int ErrorCode = errorResult.getErrorCode();
                    int ClientErrorCode = -777;
                    if (ErrorCode == ClientErrorCode) {
                        Toast.makeText(getActivity(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("TAG", "오류로 카카오로그인 실패 ");
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onSuccess(MeV2Response result) {
             /*       로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.*/
                    MyLog.d("프로필 닉네임 : " + result.getNickname());
                    MyLog.d("프로필 이메일 : " + result.getKakaoAccount().getEmail());
                    MyLog.d("프로필 이미지URL : " + result.getProfileImagePath());
                    MyLog.d("카카오아이디 : " + result.getId());
                    String name = result.getNickname();
                    kakaoUser = new User();
                    kakaoUser.setName(name);
                    kakaoUser.setKakaoId(String.valueOf(result.getId()));
                    if(result.getKakaoAccount().getEmail() ==null) kakaoUser.setEmail(" ");
                    else kakaoUser.setEmail(result.getKakaoAccount().getEmail());
                    kakaoUser.setKakaoUser(false);
                    if(result.getProfileImagePath()==null) kakaoUser.memberIconFilename =" ";
                    else kakaoUser.memberIconFilename = result.getProfileImagePath();
                    if(kakaoOnSucessCount==0) {
                        MyLog.d("서버요청 if문");
                        kakaoOnSucessCount++;
                        isPastKaKaoLogin(String.valueOf(result.getId()),kakaoUser.getName(),fragmentHandler);
                    }
                }
            });
        }
    }
    Handler fragmentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment fragment = LoginNickNameSettingFragment.newInstance(kakaoUser);
            fragment.getArguments();
            ft.replace(R.id.fragmentFrame, fragment).addToBackStack(null);
            ft.commit();
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyLog.d("여기서 불름?");
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            MyLog.d("안에서 불름?");
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void isPastKaKaoLogin(String email, String name, final Handler handler) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<User> call = remoteService.isPastKaKaoLogin(email, name);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                User loadingUser = response.body();
                if (response.isSuccessful()) {
                    MyLog.d("로딩유저 : " + loadingUser);
                    if (loadingUser == null) {//과거에 로그인한적이 없어염->닉네임 설정칸으로갑시다
                        MyLog.d("과거 카카오로그인 x");
                        handler.sendEmptyMessage(0);
                    } else { //과거에 로그인한적 있어염 -> 그대로 자동로그인값 활성화시키고 종료시키자
                        MyLog.d("과거로그인");
                        ((MyApp) getActivity().getApplicationContext()).setUserItem((User) response.body());
                        setAutoLogin((User) response.body());
                    }

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
    public void setAutoLogin(User userItem) {
        ((MyApp) getActivity().getApplicationContext()).editor.putString("KakaoId", userItem.getKakaoId());
        ((MyApp) getActivity().getApplicationContext()).editor.putString("KakaoNickName", userItem.name);
        ((MyApp) getActivity().getApplicationContext()).editor.putBoolean("Auto_Login_enabled_Kakao", true);
        ((MyApp) getActivity().getApplicationContext()).editor.commit();
        getActivity().finish();
    }
}

