package com.mobitant.bestfood;

public interface Constant {
    int MAX_LENGTH_DESCRIPTION = 50;

    int MAP_MAX_ZOOM_LEVEL = 10;
    int MAP_ZOOM_LEVEL_DETAIL = 13;

    String ORDER_TYPE_HITS = "hits_cnt";
    String ORDER_TYPE_FAVORITE = "keep_cnt";
    String ORDER_TYPE_RECENT = "reg_date";
    public static final String NETWORK_URL = "http://172.30.1.36:8005/";

    /*
    #나가기버튼클릭 :
    -roomDB 참가수필드 1삭제,
    -participant_is_exit : 나가기선택한 닉네임이 participant일때
    -owner_is_exit:나가기 선택한 닉네임이 owner일때
    둘다나갔을때 : 소켓 disconnect부분에서 해당 room컬렉션의 참가수필드를 확인하고 0이라면 룸을지워버림
    */

    /*
    # 나가기버튼 클릭한 사람에게 ... 채팅다시보낼때
    -participnat_is_exit , owner_is_exit이 true인지 확인한다
    -true라면
        -room 컬렉션의 participant_is_exit , owner_is_exit중에 뭔지는모르겠지만 1개를 다시 false로 바꾼다
        -접속해있는 네.스 에게보낸다
        -함수수행

    -false라면
        -함수수행


    */


    /*
    게시글 :
    MainActivity -> RegisterActivity : 1003
    ListFragment -> RegisterActivity : 1002
    ContestActivity -> RegisterActivity : 1001
    NotificationActivity -> RegisterActivity : 1004
    HomeActivity -> 1006
    SupportersActivity-> 1007
     */
}