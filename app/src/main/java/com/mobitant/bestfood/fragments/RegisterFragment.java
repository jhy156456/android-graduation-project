package com.mobitant.bestfood.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.model.Response;
import com.mobitant.bestfood.item.User;
import com.mobitant.bestfood.remote.ServiceGenerator;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.mobitant.bestfood.utils.Validation.validateEmail;
import static com.mobitant.bestfood.utils.Validation.validateFields;

public class RegisterFragment extends Fragment {

    public static final String TAG = RegisterFragment.class.getSimpleName();

    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private EditText mEtNickName;
    private Button mBtRegister;
    private TextView mTvLogin;
    private TextInputLayout mTiName;
    private TextInputLayout mTiEmail;
    private TextInputLayout mTiPassword;
    private TextInputLayout mTiNickName;
    private ProgressBar mProgressbar;
    EditText sextypeEdit;
    EditText birthEdit;
    TextView isDuplicated;
    private EditText phoneEdit;
    CheckBox selectBuyer, selectSeller, selectSupporters;
    String nickName;
    private CompositeSubscription mSubscriptions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mSubscriptions = new CompositeSubscription();
        initViews(view);
        return view;
    }

    private void initViews(View v) {

        mEtName = (EditText) v.findViewById(R.id.et_name);
        mEtEmail = (EditText) v.findViewById(R.id.et_email);
        mEtPassword = (EditText) v.findViewById(R.id.et_password);
        mBtRegister = (Button) v.findViewById(R.id.btn_register);
        mTvLogin = (TextView) v.findViewById(R.id.tv_login);
        mTiName = (TextInputLayout) v.findViewById(R.id.ti_name);
        mTiEmail = (TextInputLayout) v.findViewById(R.id.ti_email);
        mTiPassword = (TextInputLayout) v.findViewById(R.id.ti_password);
        mProgressbar = (ProgressBar) v.findViewById(R.id.progress);
        mEtNickName = (EditText) v.findViewById(R.id.et_nickname);
        isDuplicated = (TextView) v.findViewById(R.id.button2);
        selectBuyer = (CheckBox) v.findViewById(R.id.select_buyer);
        selectSeller = (CheckBox) v.findViewById(R.id.select_seller);
        selectSupporters = (CheckBox) v.findViewById(R.id.select_supporters);


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
        mTvLogin.setOnClickListener(view -> goToLogin());
    }


    private void register() {

        setError();

        String name = mEtName.getText().toString();
        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();
        String nickName = mEtNickName.getText().toString();
        String memberType="";
        int err = 0;

        if (!validateFields(name)) {
            err++;
            mTiName.setError("Name should not be empty !");
        }

        if (!validateEmail(email)) {

            err++;
            mTiEmail.setError("Email should be valid !");
        }

        if (!validateFields(password)) {

            err++;
            mTiPassword.setError("Password should not be empty !");
        }
        if (!validateFields(nickName)) {

            err++;
            mTiNickName.setError("닉네임을 입력하세요!");
        }
        if(selectSupporters==null||selectSeller==null||selectBuyer==null){
            err++;
            Toast.makeText(getContext(),"유형을 선택하세요!",Toast.LENGTH_LONG).show();
        }
        if (err == 0) {
            if(selectBuyer.isChecked()) memberType = "Buyer";
            else if(selectSeller.isChecked())memberType = "Seller";
            else if (selectSupporters.isChecked())memberType="Supporters";
            User user = new User();
            user.setSextype(sextypeEdit.getText().toString());
            user.setBirthday(birthEdit.getText().toString().replace(" ", ""));
            user.setName(name);
            user.setMemberType(memberType);
            user.setEmail(email);
            user.setPassword(password);
            user.setNickName(nickName);
            mProgressbar.setVisibility(View.VISIBLE);
            registerProcess(user);

        } else {

            showSnackBarMessage("Enter Valid Details !");
        }
    }

    private void setError() {
        mTiName.setError(null);
        mTiEmail.setError(null);
        mTiPassword.setError(null);
    }

    private void checkNicName(String nickName) {
        mSubscriptions.add(ServiceGenerator.getRetrofit().duplicateCheck(nickName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleCheckResponse, this::handleCheckError));
    }

    private void registerProcess(User user) {

        mSubscriptions.add(ServiceGenerator.getRetrofit().register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
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

            showSnackBarMessage("Network Error !");
        }
    }

    private void showSnackBarMessage(String message) {

        if (getView() != null) {

            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void goToLogin() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        LoginFragment fragment = new LoginFragment();
        ft.replace(R.id.fragmentFrame, fragment, LoginFragment.TAG);
        ft.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
