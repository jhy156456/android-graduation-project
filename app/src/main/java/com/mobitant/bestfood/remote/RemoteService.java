package com.mobitant.bestfood.remote;

import com.mobitant.bestfood.Constant;
import com.mobitant.bestfood.item.ChatContentsItem;
import com.mobitant.bestfood.item.ChatTalkData;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.KeepItem;
import com.mobitant.bestfood.item.NotificationCommentItem;
import com.mobitant.bestfood.item.NotificationItem;
import com.mobitant.bestfood.item.NotificationsModel;
import com.mobitant.bestfood.item.OrderCheckItem;
import com.mobitant.bestfood.item.OrderItem;
import com.mobitant.bestfood.item.ProductGridModellClass;
import com.mobitant.bestfood.model.Response;
import com.mobitant.bestfood.item.User;

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
 * 게시글등록할때
 */

public interface RemoteService {
    //String BASE_URL = "http://graduationproject-env.vcditjejd4.ap-northeast-2.elasticbeanstalk.com/";
    String BASE_URL = Constant.NETWORK_URL;
    String MEMBER_ICON_URL = BASE_URL + "/member/";
    String IMAGE_URL = BASE_URL + "/img/";

    //<==채팅을위한 라우팅 시작 ==>
    @GET("room/android/{id}")
    Call<ArrayList<ChatContentsItem>> getChatContents(@Path("id") String roomId,
                                                      @Query("current_page") int page);

    @POST("room/{id}/chat")
    Call<String> sendChat(@Path("id") String roomId,
                          @Body ChatContentsItem contentsItem);

    @GET("user/{user_type}")
    Call<ArrayList<User>> listSupporters(@Path("user_type") String userType,
                                         @Query("my_nick_name") String myNickName,
                                         @Query("current_page") int page);

    @GET("user/chat/{user_nickname}")
    Call<ArrayList<ChatTalkData>> listMyChat(@Path("user_nickname") String userType,
                                             @Query("current_page") int page);

    @GET("room/participant_down/{id}")
    Call<String> chatRoomParticipantExit(@Path("id") String roomId);

    @GET("room/owner_down/{id}")
    Call<String> chatRoomOwnerExit(@Path("id") String roomId);

    @POST("room/android/newroom")
    Call<String> newRoomFromAndroid(@Body ChatTalkData chatTalkData);

    //<==채팅을위한 라우팅 끝 ==>


    //구매완료
    @POST("/order/addorder")
    Call<String> insertOrderCheckItem(@Body OrderCheckItem orderCheckItem);


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
    Call<ResponseBody> uploadMemberIcon(@Part("id") RequestBody memberSeq,
                                        @Part MultipartBody.Part file);


    // <=== Doit으로 만든 게시판 시작: Node.js에 요청하기 ==>
    @POST("/process/addpost")
    Call<String> insertNotificationInfo(@Body NotificationItem notificationItem);


    @GET("/process/listpost")
    Call<ArrayList<NotificationItem>> listNotificationQuestionList(@Query("user_id") String id,
                                                                   @Query("current_page") int currentPage);

    //문의정보
    @GET("/process/showpost/{info_seq}")
    Call<NotificationItem> selectNotificationInfo(@Path("info_seq") String infoItemId,
                                                  @Query("member_seq") int memberSeq);

    @POST("/process/addcomment")
    Call<String> insertComment(@Body NotificationCommentItem commentItem);

    @GET("/process/removecomment")
    Call<String> removeComment(@Query("postId") String postId,
                               @Query("commentId") String id,
                               @Query("from") int from);

    // <== Doit으로 만든 게시판 끝: Node.js에 요청하기 ==>

    //buy_software_info_schema에 댓글추가
    @POST("/food/addcomment")
    Call<String> insertSoftWareComment(@Body NotificationCommentItem commentItem);
    // Doit으로 만든 Node.js에 요청하기 끝


    //맛집 정보
    @GET("/food/info/{info_seq}")
    Call<FoodInfoItem> selectFoodInfo(@Path("info_seq") int foodInfoSeq,
                                      @Query("member_seq") int memberSeq);


    //조회하고자 하는 유저 프로필 정보를 반환한다.
    @GET("/users/{seq}")
    Call<User> selectUserInfo(@Path("seq") int wantMemberSeq);

    @POST("/food/info")
    Call<String> insertFoodInfo(@Body FoodInfoItem infoItem);
    @GET("/food/info/remove")
    Call<String> removeInfo(@Query("post_id")String postId,
                            @Query("from") int from);
    @Multipart
    @POST("/food/info/image")
    Call<ResponseBody> uploadFoodImage(@Part("info_seq") RequestBody infoSeq,
                                       @Part("image_memo") RequestBody imageMemo,
                                       @Part MultipartBody.Part file);

    @GET("/food/list")
    Call<ArrayList<FoodInfoItem>> listFoodInfo(@Query("key_word") String keyWord,
                                               @Query("member_seq") int memberSeq,
                                               @Query("order_type") String orderType,
                                               @Query("current_page") int currentPage,
                                               @Query("post_category") int post_category);

    @GET("/order/list")
    Call<ArrayList<OrderCheckItem>> getOrderHistory(@Query("member_nick_name") String memberNickName,
                                                  @Query("current_page") int currentPage);
    @GET("/Notification/list")
    Call<ArrayList<NotificationsModel>> getNotification(@Query("current_page") int page);



    @GET("/food/list/tag")
    Call<ArrayList<FoodInfoItem>> listFoodInfowithTag(@Query("post_tag") ArrayList<String> postTag,
                                                      @Query("key_word") String keyWord,
                                                      @Query("member_seq") int memberSeq,
                                                      @Query("order_type") String orderType,
                                                      @Query("current_page") int currentPage,
                                                      @Query("post_category") int post_category);

    @GET("/food/list")
    Call<ArrayList<ProductGridModellClass>> listContestInfo(@Query("member_seq") int memberSeq,
                                                            @Query("board_type") int boardType,
                                                            @Query("sort_type") String sortType,
                                                            @Query("current_page") int currentPage);


    @GET("/food/postedlist")
    Call<ArrayList<FoodInfoItem>> postedProfileListSoftwareInfo(@Query("member_seq") int wantMemberSeq,
                                                                @Query("my_seq")int mySeq,
                                                                @Query("current_page") int currentPage);

    //즐겨찾기
    @POST("/keep/{member_seq}/{info_seq}")
    Call<String> insertKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @DELETE("/keep/{member_seq}/{info_seq}")
    Call<String> deleteKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @GET("/keep/list")
    Call<ArrayList<KeepItem>> listKeep(@Query("member_seq") int memberSeq);

    //로그인 관련 라우팅 시작
    @POST("users")
    Observable<Response> register(@Body User user);

    @GET("users/check/{nickname}")
    Observable<Response> duplicateCheck(@Path("nickname") String nickName);


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

    /*    과거에 카카오톡으로 로그인한적이 있는지 조사
        있다면 그값 불러와서 MyApp User에 두고
        없다면 닉네임설정하는화면으로 넘어감*/
    @GET("/users/kakao/{email}/{name}")
    Call<User> isPastKaKaoLogin(@Path("email") String id, @Path("name") String name);
    //로그인 관련 라우팅 끝
}