package com.mobitant.bestfood.lib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import com.mobitant.bestfood.R;
import com.mobitant.bestfood.item.SingerItem;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 다이얼로그와 관련된 메소드로 구성된 라이브러리
 */
public class DialogLib {
    public final String TAG = DialogLib.class.getSimpleName();
    private volatile static DialogLib instance;

    public static DialogLib getInstance() {
        if (instance == null) {
            synchronized (DialogLib.class) {
                if (instance == null) {
                    instance = new DialogLib();
                }
            }
        }
        return instance;
    }
    public void showCommentDeleteDialog(Context context,String postId,String id){


    }




    public void inputPostDialog(Context context){

    new AlertDialog.Builder(context)
            .setTitle("로그인")
            .setMessage("로그인이 필요합니다. 로그인 하시겠습니까?")
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            })
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GoLib.getInstance().goLoginActivity(context);
                }
            })
            .show();
}
    /**
     * 즐겨찾기 추가 다이얼로그 화면을 보여준다.
     * @param context 컨텍스트 객체
     * @param handler 핸들러 객체
     * @param memberSeq 사용자 일련번호
     * @param infoSeq 맛집 정보 일련번호
     */
    public void showKeepInsertDialog(Context context, final Handler handler,
                                     final int memberSeq, final int infoSeq) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.keep_insert)
                .setMessage(R.string.keep_insert_message)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        KeepLib.getInstance().insertKeep(handler, memberSeq, infoSeq);
                    }
                })
                .show();
    }

    /**
     * 즐겨찾기 삭제 다이얼로그 화면을 보여준다.
     * @param context 컨텍스트 객체
     * @param handler 핸들러 객체
     * @param memberSeq 사용자 일련번호
     * @param infoSeq 맛집 정보 일련번호
     */
    public void showKeepDeleteDialog(Context context, final Handler handler,
                                     final int memberSeq, final int infoSeq) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.keep_delete)
                .setMessage(R.string.keep_delete_message)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        KeepLib.getInstance().deleteKeep(handler, memberSeq, infoSeq);
                    }
                })
                .show();
    }
}
