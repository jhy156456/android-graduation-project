package com.mobitant.bestfood;

import android.app.Application;
import android.os.StrictMode;
import android.widget.Toast;

import com.kakao.auth.KakaoSDK;
import com.mobitant.bestfood.adapter.KakaoSDKAdapter;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.MemberInfoItem;
import com.mobitant.bestfood.model.User;
/**
 * 앱 전역에서 사용할 수 있는 클래스
 */
public class MyApp extends Application {
    private User userItem;
    private FoodInfoItem foodInfoItem;
    private boolean isNewBestFood;
    private boolean isLogin;
    private boolean isNewNotification;
    private static MyApp instance;

    public static MyApp getGlobalApplicationContext() {
        if (instance == null) {
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }
    public MyApp() {
        isNewBestFood = false;
        isNewNotification =false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // Kakao Sdk 초기화
        KakaoSDK.init(new KakaoSDKAdapter());
        // FileUriExposedException 문제를 해결하기 위한 코드
        // 관련 설명은 책의 [참고] 페이지 참고
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    //닉네임 반환함수
    public String getMemberNickName() {
        return userItem.nickname;
    }

    //로그인//
    public User getUserItem() {
        if (userItem == null) userItem = new User();
        return userItem;
    }

    public void setUserItem(User userItem) {
        this.userItem = userItem;
    }

    //로그인 끝//

    //원래 public MemberInfoItem
    public User getMemberInfoItem() {
        if (userItem == null) userItem = new User();
        return userItem;
    }
public String getMemberNickname(){
        return userItem.nickname;
}
    public int getMemberSeq() {
        return userItem.seq;
    }

    public void setFoodInfoItem(FoodInfoItem foodInfoItem) {
        this.foodInfoItem = foodInfoItem;
    }

    public FoodInfoItem getFoodInfoItem() {
        return foodInfoItem;
    }

    public String getFoodInfoItemNickName() {
        return foodInfoItem.post_nickname;
    }

    public void setIsNewBestfood(boolean isNewBestFood) {
        this.isNewBestFood = isNewBestFood;
    }

    public boolean getIsNewBestfood() {
        return isNewBestFood;
    }

    //문의사항 등록 후 목록으로 돌아왔을때 갱신하기위함
    public boolean getIsNewNotification() {
        return isNewBestFood;
    }
    public void setIsNewNotification(boolean isNewNotification) {
        this.isNewNotification = isNewNotification;
    }


    public String getMemberIconFilename() {
        return userItem.memberIconFilename;
    }

}
