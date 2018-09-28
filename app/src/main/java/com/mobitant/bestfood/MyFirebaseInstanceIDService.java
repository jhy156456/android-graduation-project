package com.mobitant.bestfood;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mobitant.bestfood.lib.MyLog;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyIID";
    private String myIp = "192.168.25.6";
    @Override
    //토큰 : 등록아이디, 등록아이디 갱신되었을때(단말환경 바뀌었을때 갱신됨)

    public void onTokenRefresh() {
        Log.d(TAG, "onTokenRefresh() 호출됨.");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed Token : " + refreshedToken);
        gogoFcm();

        //(0928작성)
        //음 여기서 권한이란건 뭔지 모르겠는데  내생각엔.. gogoDevice함수를 호출하기위해선 권한이 필요할것같다.
        //그래서 이 클래스에 작성하면 먼저 설치시에  onTokenRefresh()를 호출하지만 권하닝 없어서 gogoDevice를 실행하지 못하기때문에
        //등록아이디를 데이터베이스에 저장못하는것같음!
        //나중에 시간날때 한번 확인해보장~


        //(언제작성했는지기억안남)
        // 이게 좃같은게 권한때문에 처음에 실행은 안되는데 최초에 1번만 실행시키고싶은데
        //그래서 여기다놨더니 데이터베이스에 regid에 ""가들어간다
        //라고 추측했는데 이게 아니라 gogoFcm이 .. 어쨋든 실행순서가 안맞아서그럼
        //걍여기다가놓자..
    }
    public void gogoFcm() {
        String regId = FirebaseInstanceId.getInstance().getToken();
        MyLog.d("등록 ID : " + regId);

        sendToMobileServer(regId);
    }

    public void sendToMobileServer(final String regId) {
        String registerUrl = "http://"+myIp+":3000/process/register";

        StringRequest request = new StringRequest(Request.Method.POST, registerUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            MyLog.d("onResponse() 호출됨 : " + response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("mobile", getMobile());
                params.put("registrationId", regId);

                return params;
            }
        };

        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
        MyLog.d("웹서버에 요청함 : " + registerUrl);
    }


    public String getMobile() {
        String mobile = null;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            mobile = telephonyManager.getLine1Number();
        }
        if (telephonyManager.getLine1Number() != null) {
            mobile = telephonyManager.getLine1Number();
        }

        return mobile;
    }
}
