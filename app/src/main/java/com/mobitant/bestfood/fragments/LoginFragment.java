package com.mobitant.bestfood.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobitant.bestfood.HomeActivity;
import com.mobitant.bestfood.MainActivity;
import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.MemberInfoItem;
import com.mobitant.bestfood.lib.EtcLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.model.Response;
import com.mobitant.bestfood.model.User;
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

    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
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

        mBtLogin.setOnClickListener(view -> login());
        mTvRegister.setOnClickListener(view -> goToRegister());
        mTvForgotPassword.setOnClickListener(view -> showDialog());
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

            loginProcess(email,password);
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
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(Response response) {

        mProgressBar.setVisibility(View.GONE);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.TOKEN,response.getToken());
        editor.putString(Constants.EMAIL,response.getMessage());
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
                Response response = gson.fromJson(errorBody,Response.class);
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

            Snackbar.make(getView(),message,Snackbar.LENGTH_SHORT).show();
        }
    }

    private void goToRegister(){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        RegisterFragment fragment = new RegisterFragment();
        ft.replace(R.id.fragmentFrame,fragment,RegisterFragment.TAG);
        ft.commit();
    }

    private void showDialog(){

        ResetPasswordDialog fragment = new ResetPasswordDialog();

        fragment.show(getFragmentManager(), ResetPasswordDialog.TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

}
