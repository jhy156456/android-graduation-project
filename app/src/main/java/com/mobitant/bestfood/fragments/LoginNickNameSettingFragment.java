package com.mobitant.bestfood.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.User;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.model.Response;
import com.mobitant.bestfood.remote.ServiceGenerator;

import java.io.IOException;
import java.io.Serializable;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.mobitant.bestfood.utils.Validation.validateEmail;
import static com.mobitant.bestfood.utils.Validation.validateFields;

public class LoginNickNameSettingFragment extends android.app.Fragment  {
    private EditText mEtNickName;
    private TextInputLayout mTiNickName;
    TextView isDuplicated;
    CheckBox selectBuyer, selectSeller, selectSupporters;
    String nickName;
    User mKakaoUser;
    private ProgressBar mProgressbar;
    private Button mBtRegister;
    public static final String TAG = RegisterFragment.class.getSimpleName();
    private CompositeSubscription mSubscriptions;

    /**
     * FoodInfoItem 객체를 인자로 저장하는
     * BestFoodRegisterInputFragment 인스턴스를 생성해서 반환한다.
     * @return BestFoodRegisterImageFragment 인스턴스
     */
    public static LoginNickNameSettingFragment newInstance(User userItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("User", userItem);
        LoginNickNameSettingFragment f = new LoginNickNameSettingFragment();
        f.setArguments(bundle);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nick_name_setting, container, false);
        mSubscriptions = new CompositeSubscription();

        if (getArguments() != null) {
            mKakaoUser = (User) getArguments().getSerializable("User");
        }
        MyLog.d("m카카오유저값 : " + mKakaoUser);
        initViews(view);
        return view;
    }
    private void initViews(View v) {
        mEtNickName = (EditText) v.findViewById(R.id.et_nickname);
        mProgressbar = (ProgressBar) v.findViewById(R.id.progress);
        isDuplicated = (TextView) v.findViewById(R.id.button2);
        selectBuyer = (CheckBox) v.findViewById(R.id.select_buyer);
        selectSeller = (CheckBox) v.findViewById(R.id.select_seller);
        selectSupporters = (CheckBox) v.findViewById(R.id.select_supporters);
        mBtRegister = (Button) v.findViewById(R.id.btn_register);
        mEtNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nickName = mEtNickName.getText().toString();
                if (!nickName.equals("") && nickName != null) {//이걸안해주면 닉네임을 쓰고 빈칸으로 지우면서 빈칸으로만들어놓으면
                    //아마도... user/check/빈칸이 되기때문에 라우팅을 다른곳으로 하는것같음
                    MyLog.d("nickName값 : " + nickName);
                    checkNicName(nickName);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mBtRegister.setOnClickListener(view -> register());
    }

    private void checkNicName(String nickName) {
        mSubscriptions.add(ServiceGenerator.getRetrofit().duplicateCheck(nickName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleCheckResponse, this::handleCheckError));
    }
    private void handleCheckResponse(Response response) {
        isDuplicated.setText(response.getMessage());
    }

    private void handleCheckError(Throwable error) {

        // mProgressbar.setVisibility(View.GONE);

        if (error instanceof HttpException) {
            Gson gson = new GsonBuilder().create();
            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
                isDuplicated.setText(response.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showSnackBarMessage("Network Error !");
        }
    }


    private void handleResponse(Response response) {
        mProgressbar.setVisibility(View.GONE);
        showSnackBarMessage(response.getMessage());
        ((MyApp) getActivity().getApplicationContext()).editor.putString("KakaoEmail", mKakaoUser.getEmail());
        ((MyApp) getActivity().getApplicationContext()).editor.putString("KakaoNickName", mKakaoUser.name);
        ((MyApp) getActivity().getApplicationContext()).editor.putBoolean("Auto_Login_enabled_Kakao", true);
        ((MyApp) getActivity().getApplicationContext()).editor.commit();
        getActivity().finish();
    }
    private void handleError(Throwable error) {

        mProgressbar.setVisibility(View.GONE);
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

        }
    }
    private void showSnackBarMessage(String message) {

        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }
    private void register() {
        String nickName = mEtNickName.getText().toString();
        String memberType = "";
        int err = 0;
        if (selectSupporters == null || selectSeller == null || selectBuyer == null) {
            err++;
            Toast.makeText(getContext(), "유형을 선택하세요!", Toast.LENGTH_LONG).show();
        }
        if (err == 0) {
            if (selectBuyer.isChecked()) memberType = "Buyer";
            else if (selectSeller.isChecked()) memberType = "Seller";
            else if (selectSupporters.isChecked()) memberType = "Supporters";
            User user = new User();
            user.setName(mKakaoUser.name);
            user.setUserType(memberType);
            user.setEmail(mKakaoUser.getEmail());
            user.setPassword("kakao");
            user.setNickName(nickName);
            user.setPhone(getPhoneNumber());
            mProgressbar.setVisibility(View.VISIBLE);
            registerProcess(user);
        } else {
            showSnackBarMessage("Enter Valid Details !");
        }
    }
    private void registerProcess(User user) {
        mSubscriptions.add(ServiceGenerator.getRetrofit().register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }
    private String getPhoneNumber(){
        // 1. mobile
        String mobile = null;
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_NUMBERS)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        if (telephonyManager.getLine1Number() != null) {
            mobile = telephonyManager.getLine1Number();
        }
        return mobile;
    }
}
