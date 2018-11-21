package com.mobitant.bestfood.lib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


import com.mobitant.bestfood.BestFoodInfoActivity;
import com.mobitant.bestfood.BestFoodRegisterActivity;
import com.mobitant.bestfood.HomeActivity;
import com.mobitant.bestfood.KeepActivity;
import com.mobitant.bestfood.MainActivity;
import com.mobitant.bestfood.MainActivity2;
import com.mobitant.bestfood.MemberProfile;
import com.mobitant.bestfood.NotificationActivity;
import com.mobitant.bestfood.NotificationsActivity;
import com.mobitant.bestfood.OrderHistoryActivity;
import com.mobitant.bestfood.ProfileActivity;
import com.mobitant.bestfood.SoftwareBuyActivity;
import com.mobitant.bestfood.adapter.NotificationAdapter;
import com.mobitant.bestfood.item.OrderCheckItem;


/**
 * 액티비티나 프래그먼트 실행 라이브러리
 */
public class GoLib {
    public final String TAG = GoLib.class.getSimpleName();
    private volatile static GoLib instance;

    public static GoLib getInstance() {
        if (instance == null) {
            synchronized (GoLib.class) {
                if (instance == null) {
                    instance = new GoLib();
                }
            }
        }
        return instance;
    }

    /**
     * 프래그먼트를 보여준다.
     * @param fragmentManager 프래그먼트 매니저
     * @param containerViewId 프래그먼트를 보여줄 컨테이너 뷰 아이디
     * @param fragment 프래그먼트
     */
    public void goFragment(FragmentManager fragmentManager, int containerViewId,
                           Fragment fragment) {
        fragment.getArguments();
        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }
    /**
     * 프래그먼트를 보여준다.
     * @param fragmentManager 프래그먼트 매니저
     * @param containerViewId 프래그먼트를 보여줄 컨테이너 뷰 아이디
     * @param fragment 프래그먼트
     */
    public void goFragmentDetail(FragmentManager fragmentManager, int containerViewId,
                           Fragment fragment,Bundle bundle) {
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }

    /**
     * 즐겨찾기 액티비티 실행
     * @param context 컨텍스트
     */
    public void goKeepActivity(Context context) {
        Intent intent = new Intent(context, KeepActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 공지사항 액티비티 실행
     * @param context 컨텍스트
     */
    public void goNotificationActivity(Context context) {
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 구매 액티비티 실행
     * @param context 컨텍스트
     */
    public void goBuyActivity(Context context, OrderCheckItem orderCheckItem) {
        Intent intent = new Intent(context, SoftwareBuyActivity.class);
        intent.putExtra("orderCheckItem", orderCheckItem);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 뒤로가기를 할 수 있는 프래그먼트를 보여준다.
     * @param fragmentManager 프래그먼트 매니저
     * @param containerViewId 프래그먼트를 보여줄 컨테이너 뷰 아이디
     * @param fragment 프래그먼트
     */
    public void goFragmentBack(FragmentManager fragmentManager, int containerViewId,
                               Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 이전 프래그먼트를 보여준다.
     * @param fragmentManager 프래그먼트 매니저
     */
    public void goBackFragment(FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
    }

    /**
     * 프로파일 액티비티를 실행한다.
     * @param context 컨텍스트
     */
    public void goProfileActivity(Context context) {
        Intent intent = new Intent(context, MemberProfile.class);
        intent.putExtra("callActivity","NavigationClick");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public void goOrderHistoryActivity(Context context) {
        Intent intent = new Intent(context, OrderHistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public void goRealNotificationActivity(Context context) {
        Intent intent = new Intent(context, NotificationsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 프로파일 액티비티를 실행한다.
     * @param context 컨텍스트
     */
    public void goProfileChangeActivity(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public void goHomeActivity(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        //기존 액티비티 모두 제거 후 (모두제거는맞는지모르겠지만)홈으로간다.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    /**
     * 맛집 정보 등록 액티비티를 실행한다.
     * @param context 컨텍스트
     */
    public void goBestFoodRegisterActivity(Context context,int from) {
        Intent intent = new Intent(context, BestFoodRegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("from",from);
        context.startActivity(intent);
    }
public void goBestFoodMainActivity(Context context){
    Intent intent = new Intent(context, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
}

    /**
     * 맛집 정보 액티비티를 실행한다.
     * @param context 컨텍스트
     * @param infoSeq 맛집 정보 일련번호
     */
    public void goBestFoodInfoActivity(Context context, int infoSeq,String itemPostNickName) {
        Intent intent = new Intent(context, BestFoodInfoActivity.class);
        intent.putExtra(BestFoodInfoActivity.INFO_SEQ, infoSeq);
        intent.putExtra("item_post_nick_name",itemPostNickName);
        context.startActivity(intent);
    }

    public void goLoginActivity(Context context) {
        Intent intent = new Intent(context, MainActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
