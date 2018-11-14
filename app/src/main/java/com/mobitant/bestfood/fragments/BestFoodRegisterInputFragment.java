package com.mobitant.bestfood.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haresh.multipleimagepickerlibrary.MultiImageSelector;
import com.mobitant.bestfood.BestFoodRegisterActivity;
import com.mobitant.bestfood.MyApp;
import com.mobitant.bestfood.R;
import com.mobitant.bestfood.adapter.ImagesAdapter;
import com.mobitant.bestfood.item.FoodInfoItem;
import com.mobitant.bestfood.item.ImageItem;
import com.mobitant.bestfood.lib.BaseFragment;
import com.mobitant.bestfood.lib.BitmapLib;
import com.mobitant.bestfood.lib.EtcLib;
import com.mobitant.bestfood.lib.FileLib;
import com.mobitant.bestfood.lib.GoLib;
import com.mobitant.bestfood.lib.MyLog;
import com.mobitant.bestfood.lib.MyToast;
import com.mobitant.bestfood.lib.RemoteLib;
import com.mobitant.bestfood.lib.StringLib;
import com.mobitant.bestfood.remote.RemoteService;
import com.mobitant.bestfood.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 맛집 정보를 입력하는 액티비티
 */
public class BestFoodRegisterInputFragment extends BaseFragment implements View.OnClickListener {
    public static final String INFO_ITEM = "INFO_ITEM";
    private final String TAG = this.getClass().getSimpleName();
    final List<Target> targets = new ArrayList<Target>();

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerViewImages;
    private GridLayoutManager gridLayoutManager;

    private ArrayList<String> mSelectedImagesList = new ArrayList<>();
    private final int MAX_IMAGE_SELECTION_LIMIT=100;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 401;
    private final int REQUEST_IMAGE=301;

    private MultiImageSelector mMultiImageSelector;
    private ImagesAdapter mImagesAdapter;

    Activity context;
    FoodInfoItem infoItem;
    EditText nameEdit;
    EditText telEdit;
    EditText osEdit;
    EditText descriptionEdit,sellPrice;
    TextView currentLength;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    //이미지 클래스에서 가져온 값

    File [] saveImageFile = new File[20];
    String [] saveImageFileName = new String[20];
    ImageItem [] saveImageItem = new ImageItem[20];
    Uri[] saveUri = new Uri[20];
    int mSelectedImagesListCount =0;
    int i=0;
    int count=0;
    int forHandler =0;


    RelativeLayout image_relative;
    File imageFile;
    String imageFilename;
    EditText imageMemoEdit;
    ImageItem imageItem;
    boolean isSavingImage = false;
    boolean isImageLoad = false;
    /**
     * FoodInfoItem 객체를 인자로 저장하는
     * BestFoodRegisterInputFragment 인스턴스를 생성해서 반환한다.
     * @param infoItem 맛집 정보를 저장하는 객체
     * @return BestFoodRegisterInputFragment 인스턴스
     */
    public static BestFoodRegisterInputFragment newInstance(FoodInfoItem infoItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INFO_ITEM, Parcels.wrap(infoItem));

        BestFoodRegisterInputFragment fragment = new BestFoodRegisterInputFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * 프래그먼트가 생성될 때 호출되며 인자에 저장된 FoodInfoItem를
     * BestFoodRegisterActivity에 currentItem를 저장한다.
     * @param savedInstanceState 프래그먼트가 새로 생성되었을 경우, 이전 상태 값을 가지는 객체
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            infoItem = Parcels.unwrap(getArguments().getParcelable(INFO_ITEM));
            if (infoItem.seq != 0) {
                BestFoodRegisterActivity.currentItem = infoItem;
            }
            MyLog.d(TAG, "infoItem의 포스트카테고리 " + infoItem.post_category);
        }
    }

    /**
     * fragment_bestfood_register_input.xml 기반으로 뷰를 생성한다.
     * @param inflater XML를 객체로 변환하는 LayoutInflater 객체
     * @param container null이 아니라면 부모 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     * @return 생성한 뷰 객체
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();
        return inflater.inflate(R.layout.fragment_bestfood_register_input, container, false);
    }

    /**
     * onCreateView() 메소드 뒤에 호출되며 맛집 정보를 입력할 뷰들을 생성한다.
     * @param view onCreateView() 메소드에 의해 반환된 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image_relative = (RelativeLayout) view.findViewById(R.id.image_content_main_relative);

        recyclerViewImages = (RecyclerView) view.findViewById(R.id.recycler_view_images);
        gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerViewImages.setHasFixedSize(true);
        recyclerViewImages.setLayoutManager(gridLayoutManager);
        mMultiImageSelector = MultiImageSelector.create();


        imageItem = new ImageItem();
        for(int j=0; j<20;j++){
            saveImageFileName[j] = new String();
            saveImageItem[j] = new ImageItem();

        }

        imageMemoEdit = (EditText) view.findViewById(R.id.register_image_memo);

        ImageView imageRegister = (ImageView) view.findViewById(R.id.bestfood_image_register);
        imageRegister.setOnClickListener(this);


        currentLength = (TextView) view.findViewById(R.id.current_length);
        nameEdit = (EditText) view.findViewById(R.id.bestfood_name);
        telEdit = (EditText) view.findViewById(R.id.bestfood_tel);
        osEdit = (EditText) view.findViewById(R.id.bestfood_os);
        descriptionEdit = (EditText) view.findViewById(R.id.bestfood_description);
        sellPrice=(EditText)view.findViewById(R.id.sell_price);
        if(infoItem.post_category!=1002)sellPrice.setVisibility(View.GONE);

        descriptionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentLength.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        Button prevButton = (Button) view.findViewById(R.id.prev);
        prevButton.setOnClickListener(this);

        Button nextButton = (Button) view.findViewById(R.id.next);
        nextButton.setOnClickListener(this);
    }



    /**
     * 클릭이벤트를 처리한다.
     * @param v 클릭한 뷰에 대한 정보
     */
    @Override
    public void onClick(View v) {
        infoItem.name = nameEdit.getText().toString();
        infoItem.tel = telEdit.getText().toString();
        infoItem.os = osEdit.getText().toString();
        infoItem.description = descriptionEdit.getText().toString();
        infoItem.postMemberIconFilename = ((MyApp)getActivity().getApplication()).getMemberIconFilename();
        infoItem.memberSeq = ((MyApp)getActivity().getApplication()).getMemberSeq();
        infoItem.setSell_price(sellPrice.getText().toString());

        MyLog.d(TAG, "onClick imageItem " + infoItem);

        if (v.getId() == R.id.prev) {
            GoLib.getInstance().goFragment(getFragmentManager(),
                    R.id.content_main, BestFoodListFragment.newInstance());
        } else if (v.getId() == R.id.next) {
            save();
        }else if (v.getId() == R.id.bestfood_image_register) {
            showImageDialog(context);
        }
    }

    /**
     * 사용자가 입력한 정보를 확인하고 저장한다.
     */
    private void save() {
        if (StringLib.getInstance().isBlank(infoItem.name)) {
            MyToast.s(context, context.getResources().getString(R.string.input_bestfood_name));
            return;
        }

        if (StringLib.getInstance().isBlank(infoItem.tel)
                || !EtcLib.getInstance().isValidPhoneNumber(infoItem.tel)) {
            MyToast.s(context, context.getResources().getString(R.string.not_valid_tel_number));
            return;
        }
        progressON("저장중...");
        insertFoodInfo();
    }

    /**
     * 사용자가 입력한 정보를 서버에 저장한다.
     */
    private void insertFoodInfo() {
        MyLog.d(TAG, infoItem.toString());

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertFoodInfo(infoItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    int seq = 0;
                    String seqString = response.body();
                    try {
                        seq = Integer.parseInt(seqString);
                    } catch (Exception e) {
                        seq = 0;
                    }
                    if (seq == 0) {
                        //등록 실패
                    } else {
                        infoItem.seq = seq;
                        if(isImageLoad == true) { //이미지 업로드 할경우
                            saveImage(seq);
                        }
                        else  { // 이미지업로드 안할경우
                            ((MyApp)getActivity().getApplication()).setIsNewBestfood(true);
                            progressOFF();
                            context.finish();
                        }
                    }
                } else { // 등록 실패
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    MyLog.d(TAG, "fail " + statusCode + errorBody.toString());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }

    /**
     * 이미지를 촬영하고 그 결과를 받을 수 있는 액티비티를 시작한다.
     */
    private void getImageFromCamera() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        context.startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     * 앨범으로부터 이미지를 선택할 수 있는 액티비티를 시작한다.
     */
    private void getImageFromAlbum() {
        if(checkAndRequestPermissions()) {
            mMultiImageSelector.showCamera(true);
            mMultiImageSelector.count(MAX_IMAGE_SELECTION_LIMIT);
            mMultiImageSelector.multi();
            mMultiImageSelector.origin(mSelectedImagesList);
            mMultiImageSelector.start(context, REQUEST_IMAGE); //BestFoodRegisterInputFragment.this로 하니까 안되더라
        }
    }

    /**
     * 다른 액티비티를 실행한 결과를 처리하는 메소드
     * @param requestCode 액티비티를 실행하면서 전달한 요청 코드
     * @param resultCode 실행한 액티비티가 설정한 결과 코드
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            try {
                mSelectedImagesList = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                mImagesAdapter = new ImagesAdapter(context,mSelectedImagesList); //context대신 this로 하니까 안되더라;;
                recyclerViewImages.setAdapter(mImagesAdapter);
                mSelectedImagesListCount = mSelectedImagesList.size();

                if(mSelectedImagesListCount!=0) isImageLoad = true;

                for(int l=0; l<mSelectedImagesListCount;l++){
                    //변수가 l인데 i로써서 같은 이미지가 여러개 올라갔었다 ㅡㅡ
                    saveUri[l] = getUriFromPath(mSelectedImagesList.get(l));
                    MyLog.d(TAG,"바꾼 URI : " + saveUri[l]);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    /**
     * 사용자가 선택한 이미지와 입력한 메모를 ImageItem 객체에 저장한다.
     */
    private  void setImageItem() {
        String imageMemo = imageMemoEdit.getText().toString();
        if (StringLib.getInstance().isBlank(imageMemo)) {
            imageMemo = "";
        }
        saveImageItem[count].imageMemo = imageMemo;
        saveImageItem[count].fileName = saveImageFileName[count] + ".png";
        count++;

        imageItem.imageMemo = imageMemo;
        imageItem.fileName = imageFilename + ".png";
    }

    /**
     * 이미지를 서버에 업로드한다.
     */
    private void saveImage(int infoSeq) {

        for (; i < mSelectedImagesListCount; i++) {
            saveImageFileName[i] = infoSeq + "_" + i + "_" + infoItem.post_category+"_" +String.valueOf(System.currentTimeMillis());
            saveImageFile[i] = FileLib.getInstance().getImageFile(context, saveImageFileName[i]);
            setImageItem();
        }

//순차실행해야하는데 비동기로 실행되는구나.. 그러니까 배열을 넘기는 방법을 생각해봐야겠군..
        for(int i=0; i<mSelectedImagesListCount;i++) {
            final int k = i; //이걸안해주고 i로하면 뭔순서떄문인지 뒤죽박죽으로 실행된다..ㅠㅠㅠㅠ
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    MyLog.d(TAG, "saveBitmaptoFile함수 실행할떄의 k값 : " + k);
                    BitmapLib.getInstance().saveBitmapToFile(saveImageFile[k], bitmap);

                    //업로드 순서도 뒤죽박죽이네 ㅡㅡ 몽고db에 0~4의 순서가아니라 0,4,1,2,3,뭐 이런식??ㅋㅋ개빢침ㅋㅋ
                    if(k==(mSelectedImagesListCount-1)) finalUpload(infoSeq);
                    isSavingImage = true;
                    targets.remove(this);
                }
                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    targets.remove(this);
                }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
            targets.add(target);
            MyLog.d(TAG,   "picasso 실행하기전의 i값 : " + i);
            Picasso.with(getActivity())
                    .load(saveUri[i])
                    .into(target);
        }
//올라는가는데 같은이미지가 3개 올라가는 이유가뭘까??
        for (int k = 0; k < mSelectedImagesListCount; k++) {
            Picasso.with(context).invalidate(RemoteService.IMAGE_URL + saveImageItem[k].fileName);
        }

        isSavingImage = false;
    }

    public void finalUpload(int infoSeq){
        for (int j = 0; j < (mSelectedImagesList.size()-1); j++) {
            RemoteLib.getInstance().uploadFoodImage(infoSeq,
                    saveImageItem[j].imageMemo, saveImageFile[j]);
        }

        RemoteLib.getInstance().uploadFoodImagewithHandler(infoSeq,
                saveImageItem[mSelectedImagesListCount - 1].imageMemo, saveImageFile[mSelectedImagesListCount - 1], finishHandler);

    }
    /**
     * 이미지를 어떤 방식으로 선택할지에 대해 다이얼로그를 보여준다.
     * @param context 컨텍스트 객체
     */
    public void showImageDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_bestfood_image_register)
                .setSingleChoiceItems(R.array.camera_album_category, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    getImageFromCamera();
                                } else {
                                    getImageFromAlbum();
                                }

                                dialog.dismiss();
                            }
                        }).show();
    }
    private boolean checkAndRequestPermissions() {
        int externalStoragePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (externalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            floatingActionButton.performClick();
        }
    }

    public Uri getUriFromPath(String path) {
        String fileName = "file:///sdcard/DCIM/Camera/2013_07_07_12345.jpg";
        Uri fileUri = Uri.parse(path);
        String filePath = fileUri.getPath();
        Cursor c = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);
        c.moveToNext();
        int id = c.getInt(c.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
        return uri;
    }
    Handler imageUploadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isSavingImage = false;
            setImageItem();
            Picasso.with(context).invalidate(RemoteService.IMAGE_URL + saveImageItem[forHandler++].fileName);
        }
    };

    Handler finishHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ((MyApp)getActivity().getApplication()).setIsNewBestfood(true);
            context.finish();
            progressOFF();
        }
    };

}
