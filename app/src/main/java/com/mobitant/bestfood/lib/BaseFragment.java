package com.mobitant.bestfood.lib;


import android.app.Activity;

import android.content.Context;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    Activity activity;

    public void progressON() {
        BaseApplication.getInstance().progressON( activity, null);
    }

    public void progressON(String message) {
        if(activity==null)  MyLog.d("널이다 다이개새끼야");
        else {
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