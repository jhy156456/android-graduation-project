package com.mobitant.bestfood.lib;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

public class BaseFragment extends Fragment {
    Activity activity;

    public void progressON() {
        BaseApplication.getInstance().progressON( activity, null);
    }

    public void progressON(String message) {
        if(activity==null)  MyLog.d("널이다 다이개새끼야");
        else {
            MyLog.d("널이 아니다 다이개새끼야");
            MyLog.d("message" + message);
            BaseApplication.getInstance().progressON(activity, message);
        }
    }

    public void progressOFF() {
        BaseApplication.getInstance().progressOFF();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity)
            activity = (Activity) context;
    }
}