package com.mobitant.bestfood.remote;

import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.KeepItem;
import com.mobitant.bestfood.model.Response;
import com.mobitant.bestfood.model.User;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 서버에 호출할 메소드를 선언하는 인터페이스
 */

public interface RemoteService {
    //String BASE_URL = "http://graduationproject-env.vcditjejd4.ap-northeast-2.elasticbeanstalk.com/";
    String BASE_URL = "http://192.168.216.228:3000/";
    String MEMBER_ICON_URL = BASE_URL + "/member/";
    String IMAGE_URL = BASE_URL + "/img/";

    //사용자 정보
   // @GET("/member/{mail}")
    //Call<MemberInfoItem> selectMemberInfo(@Path("phone") String phone);
    @GET("/member/{email}")
    Call<User> selectMemberInfo(@Path("email") String email);

    //User -> MemberInfoItem
    @POST("/member/info")
    Call<String> insertMemberInfo(@Body User memberInfoItem);

    @FormUrlEncoded
    @POST("/member/phone")
    Call<String> insertMemberPhone(@Field("phone") String phone);

    @Multipart
    @POST("/member/icon_upload")
    Call<ResponseBody> uploadMemberIcon(@Part("member_seq") RequestBody memberSeq,
                                        @Part MultipartBody.Part file);

    //맛집 정보
    @GET("/food/info/{info_seq}")
    Call<FoodInfoItem> selectFoodInfo(@Path("info_seq") int foodInfoSeq,
                                      @Query("member_seq") int memberSeq);
//조회하고자 하는 유저 프로필 정보를 반환한다.
    @GET("/users/{seq}")
    Call<User> selectUserInfo(@Path("seq") int wantMemberSeq);

    @POST("/food/info")
    Call<String> insertFoodInfo(@Body FoodInfoItem infoItem);

    @Multipart
    @POST("/food/info/image")
    Call<ResponseBody> uploadFoodImage(@Part("info_seq") RequestBody infoSeq,
                                       @Part("image_memo") RequestBody imageMemo,
                                       @Part MultipartBody.Part file);

    @GET("/food/list")
    Call<ArrayList<FoodInfoItem>> listFoodInfo(@Query("member_seq") int memberSeq,
                                               @Query("order_type") String orderType,
                                               @Query("current_page") int currentPage);
    @GET("/food/postedlist")
    Call<ArrayList<FoodInfoItem>> postedProfileListSoftwareInfo(@Query("member_seq") int wantMemberSeq,
                                                                @Query("current_page") int currentPage);
    //즐겨찾기
    @POST("/keep/{member_seq}/{info_seq}")
    Call<String> insertKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @DELETE("/keep/{member_seq}/{info_seq}")
    Call<String> deleteKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @GET("/keep/list")
    Call<ArrayList<KeepItem>> listKeep(@Query("member_seq") int memberSeq);
    //로그인
    @POST("users")
    Observable<Response> register(@Body User user);




    @POST("authenticate")
    Observable<Response> login();

    @GET("users/{email}")
    Observable<User> getProfile(@Path("email") String email);

    @PUT("users/{email}")
    Observable<Response> changePassword(@Path("email") String email, @Body User user);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordInit(@Path("email") String email);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);
}