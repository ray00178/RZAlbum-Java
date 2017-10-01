package com.rayzhang.android.rzalbum;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.rayzhang.android.rzalbum.adapter.UtilAdapter;
import com.rayzhang.android.rzalbum.adapter.itemdecoration.RecycleItemDecoration;
import com.rayzhang.android.rzalbum.adapter.listener.OnMultiItemClickListener;
import com.rayzhang.android.rzalbum.model.AlbumFolder;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;
import com.rayzhang.android.rzalbum.utils.AlbumScanner;
import com.rayzhang.android.rzalbum.utils.AnimationHelper;
import com.rayzhang.android.rzalbum.utils.FileProviderUtils;
import com.rayzhang.android.rzalbum.utils.MainHandler;
import com.rayzhang.android.rzalbum.view.PreviewPhotoActivty;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RZAlbumActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = RZAlbumActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_STORAGE = 9998;
    private static final int PERMISSION_REQUEST_CAMERA = 9999;
    private static final int ACTIVITY_REQUEST_CAMERA = 9997;
    private static final int ACTIVITY_REQUEST_PREVIEW = 9996;
    private static final int STATUSBAR_COLOR = Color.parseColor("#ff512da8");
    private static final int TOOLBAR_COLOR = Color.parseColor("#ff673ab7");
    private static final int PICK_COLOR = Color.parseColor("#ffffc107");
    private static final int DEFAULT_LIMIT_COUNT = 5;
    private static final int DEFAULT_SAPN_COUNT = 3;
    private static final String TOOLBAR_TITLE = "RZAlbum";

    private FloatingActionButton mFabMultiBut, mFabFloderBut, mFabDoneBut;
    private UtilAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String appName, toolBarTitle;
    private int spanCount, limitCount;
    private int toolBarColor, pickColor;
    private int dialogIcon;
    private boolean isShowCamera;
    private int orientation;

    private List<AlbumFolder> mAlbumFolders;
    private BottomSheetDialog dialog;
    private RecyclerView bottomView;
    private UtilAdapter bottomAdapter;
    // 紀錄選到的圖片
    private List<AlbumPhoto> photoList;

    private ExecutorService mSingleExecutor;
    private MediaScannerConnection connection;
    private String mCameraPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rzalbum);

        Bundle bundle = getIntent().getExtras();
        appName = bundle.getString(RZAlbum.ALBUM_APP_NAME);
        limitCount = bundle.getInt(RZAlbum.ALBUM_LIMIT_COUNT, DEFAULT_LIMIT_COUNT);
        spanCount = bundle.getInt(RZAlbum.ALBUM_SPAN_COUNT, DEFAULT_SAPN_COUNT);
        int statusBarColor = bundle.getInt(RZAlbum.ALBUM_STATUSBAR_COLOR, STATUSBAR_COLOR);
        toolBarTitle = bundle.getString(RZAlbum.ALBUM_TOOLBAR_TITLE, TOOLBAR_TITLE);
        toolBarColor = bundle.getInt(RZAlbum.ALBUM_TOOLBAR_COLOR, TOOLBAR_COLOR);
        pickColor = bundle.getInt(RZAlbum.ALBUM_PICK_COLOR, PICK_COLOR);
        dialogIcon = bundle.getInt(RZAlbum.ALBUM_DIALOG_ICON, R.drawable.ic_info_description_30_2dp);
        isShowCamera = bundle.getBoolean(RZAlbum.ALBUN_SHOW_CAMERA, true);
        orientation = bundle.getInt(RZAlbum.ALBUM_PREVIEW_ORIENTATION, RZAlbum.ORIENTATION_AUTO);

        mSingleExecutor = Executors.newSingleThreadExecutor();
        photoList = new ArrayList<>();
        setStatusBarColor(statusBarColor);
        initView();
        scanAllAlbum();
    }

    private void initView() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.mToolBar);
        mToolBar.setTitle(toolBarTitle);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setBackgroundColor(toolBarColor);
        mToolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        if (isShowCamera) {
            mToolBar.getMenu().add(0, 0, 0, "Camera")
                    .setIcon(R.drawable.ic_camera_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == 0) {
                        requestOpenCamera();
                        return true;
                    }
                    return false;
                }
            });
        }

        RecyclerView mRecyView = (RecyclerView) findViewById(R.id.mRecyView);
        mLayoutManager = new GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false);
        mRecyView.setLayoutManager(mLayoutManager);
        mRecyView.setHasFixedSize(true);
        mRecyView.setItemAnimator(null);
        mRecyView.addItemDecoration(new RecycleItemDecoration(2, Color.argb(255, 255, 255, 255)));
        adapter = new UtilAdapter(this, null);
        mRecyView.setAdapter(adapter);

        mFabMultiBut = (FloatingActionButton) findViewById(R.id.mFabMultiBut);
        mFabFloderBut = (FloatingActionButton) findViewById(R.id.mFabFloderBut);
        mFabDoneBut = (FloatingActionButton) findViewById(R.id.mFabDoneBut);
        mFabMultiBut.setAlpha(.8f);
        mFabMultiBut.setOnClickListener(this);
        mFabFloderBut.setOnClickListener(this);
        mFabDoneBut.setOnClickListener(this);

        // pick folder view
        bottomView = (RecyclerView) LayoutInflater.from(this).inflate(R.layout.rz_album_bottom_view, null);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bottomView.setLayoutManager(manager);
        bottomView.setHasFixedSize(true);
        bottomView.addItemDecoration(new RecycleItemDecoration(1, Color.LTGRAY));
        bottomAdapter = new UtilAdapter(this, null);
        bottomView.setAdapter(bottomAdapter);
    }

    private void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor);
                window.setNavigationBarColor(Color.BLACK);
            }
        }
    }

    private void scanAllAlbum() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // 第一次被使用者拒絕後，這邊做些解釋的動作
                    showDescriptionDialog(1);
                } else {
                    // 第一次詢問
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_STORAGE);
                }
            } else {
                mSingleExecutor.execute(scanner);
            }
        } else {
            mSingleExecutor.execute(scanner);
        }
    }

    /*
     * Scan image.
     */
    private Runnable scanner = new Runnable() {
        @Override
        public void run() {
            mAlbumFolders = AlbumScanner.instances(pickColor).getPhotoAlbum(RZAlbumActivity.this);
            MainHandler.instances().postDelayed(initUI, 200);
        }
    };

    /*
     * init ui
     */
    private Runnable initUI = new Runnable() {
        @Override
        public void run() {
            if (!isFinishing()) {
                showAlbum(0);
            } else {
                mAlbumFolders.clear();
                mAlbumFolders = null;
            }
        }
    };

    private void showAlbum(int index) {
        adapter.resetDatas(mAlbumFolders.get(index).getFolderPhotos());
        mLayoutManager.scrollToPosition(0);
        changePhotoStatus();
        adapterItemClick();
    }

    private void adapterItemClick() {
        adapter.setOnMultiItemClick(new OnMultiItemClickListener() {
            @Override
            public void onMultiItemClick(View view, int viewPosition, int itemPositon) {
                switch (viewPosition) {
                    case 0: // preview
                        photoPreview(itemPositon);
                        break;
                    case 1: // pick
                        photoPick(itemPositon);
                        break;
                }
            }

            @Override
            public void onMultiItemLongClick(View view, int viewPosition, int itemPositon) {

            }
        });
    }

    private void photoPreview(int itemPosition) {
        Intent preview = new Intent(this, PreviewPhotoActivty.class);
        preview.putParcelableArrayListExtra(PreviewPhotoActivty.RZ_PREVIEW_PHOTOS, (ArrayList<? extends Parcelable>) photoList);
        preview.putParcelableArrayListExtra(PreviewPhotoActivty.RZ_PREVIEW_ALL_PHOTOS,
                (ArrayList<? extends Parcelable>) adapter.getListData());
        preview.putExtra(PreviewPhotoActivty.RZ_PREVIEW_ITEMPOSITION, itemPosition);
        preview.putExtra(PreviewPhotoActivty.RZ_PREVIEW_PICK_COLOR, pickColor);
        preview.putExtra(PreviewPhotoActivty.RZ_PREVIEW_LIMIT_COUNT, limitCount);
        preview.putExtra(PreviewPhotoActivty.RZ_PREVIEW_ORIENTATION, orientation);
        startActivityForResult(preview, ACTIVITY_REQUEST_PREVIEW);
        overridePendingTransition(0, 0);
    }

    private void photoPick(int itemPosition) {
        AlbumPhoto photo = (AlbumPhoto) adapter.getListData().get(itemPosition);
        if (photo.getPickNumber() == 0) {
            if (photoList.size() == limitCount) {
                Toast.makeText(RZAlbumActivity.this, String.format(Locale.TAIWAN, "最多選擇%d張", limitCount),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            photo.setPickNumber(photoList.size() + 1);
            photoList.add(photo);
            adapter.notifyItemChanged(itemPosition);
        } else {
            if (photoList.size() == 0) return;
            photoList.remove(photo);
            photo.setPickNumber(0);
            adapter.notifyItemChanged(itemPosition);
            changePhotoStatus();
        }
    }

    private void changePhotoStatus() {
        // 改變item status & index
        for (int i = 0; i < photoList.size(); i++) {
            photoList.get(i).setPickNumber(i + 1);
            int index = adapter.getListData().indexOf(photoList.get(i));
            if (index != -1) {
                ((AlbumPhoto) adapter.getListData().get(index)).setPickNumber(i + 1);
                adapter.notifyItemChanged(index);
            }
        }
    }

    private void requestOpenCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    showDescriptionDialog(2);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CAMERA);
                }
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        // 打開手機的照相機
        Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判斷手機上是否有可以啟動照相機的應用程式
        if (camara.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                // 拍照時，將拍得的照片先保存在指定的資料夾中(未缩小)
                photoFile = createImgFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                // 照片存放路徑
                mCameraPath = photoFile.getAbsolutePath();
                // 拍照適配Android7.0 up
                Uri fileUri = FileProviderUtils.getUriForFile(this, photoFile);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileProviderUtils.grantPermissions(this, camara, fileUri, true);
                }
                // 2017-06-11 更改
                // 如果指定了圖片uri，data就没有數據，如果没有指定uri，則data就返回有數據
                // 指定圖片输出位置，若無這句則拍照後，圖片會放入內存中，由於占用内存太大導致無法剪切或者剪切後無法保存
                //camara.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                camara.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(camara, ACTIVITY_REQUEST_CAMERA);
            }
        }
    }

    private File createImgFile() throws IOException {
        // 照片命名
        String uuid = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = String.format(Locale.TAIWAN, "JEPG_%s.jpg", uuid);
        // 建立目錄
        //String imgPath = Environment.getExternalStorageDirectory() + "/RZAlbum_images";
        //File storeDir = new File(imgPath);
        File storeDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        // 如果目錄不存在就建立
        if (!storeDir.exists()) {
            boolean isCreate = storeDir.mkdirs();
        }
        // 建立檔案(存放的位置, 檔名)
        return new File(storeDir, fileName);
    }

    /*
     * 解釋給使用者 為何要取用權限說明
     * type = 1(讀取記憶卡) type = 2(拍攝照片)
     */
    private void showDescriptionDialog(final int type) {
        String title = getResources().getString(R.string.rz_album_dia_read_description);
        String msg = getResources().getString(R.string.rz_album_dia_read_message);
        if (type == 2) {
            title = getResources().getString(R.string.rz_album_dia_camera_description);
            msg = getResources().getString(R.string.rz_album_dia_camera_message);
        }
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setIcon(dialogIcon)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.rz_album_dia_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 再一次請求
                        if (type == 1) {
                            ActivityCompat.requestPermissions(RZAlbumActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
                        } else {
                            ActivityCompat.requestPermissions(RZAlbumActivity.this, new String[]{Manifest.permission.CAMERA},
                                    PERMISSION_REQUEST_CAMERA);
                        }
                    }
                })
                .setNegativeButton(R.string.rz_album_dia_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 退出activity
                        if (type == 1) {
                            RZAlbumActivity.this.finish();
                        }
                    }
                })
                .show();
    }

    /*
     * 權限已被永久拒絕
     * type = 1(讀取記憶卡) type = 2(拍攝照片)
     */
    private void showDeniedDailog(final int type) {
        String title = getResources().getString(R.string.rz_album_dia_read_description_denied);
        String msg = String.format(Locale.TAIWAN, getResources().getString(R.string.rz_album_dia_read_message_denied), appName);
        if (type == 2) {
            title = getResources().getString(R.string.rz_album_dia_camera_description_denied);
            msg = String.format(Locale.TAIWAN, getResources().getString(R.string.rz_album_dia_camera_message_denied), appName);
        }
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setIcon(dialogIcon)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.rz_album_dia_know, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (type == 1) {
                            RZAlbumActivity.this.finish();
                        }
                    }
                })
                .show();
    }

    private void showBottomDialog() {
        if (mAlbumFolders.get(0).getFolderPhotos().size() == 0) return;
        if (dialog == null) {
            dialog = new BottomSheetDialog(this);
            bottomAdapter.resetDatas(mAlbumFolders);
            bottomAdapter.setOnMultiItemClick(new OnMultiItemClickListener() {
                @Override
                public void onMultiItemClick(View view, int viewPosition, int itemPositon) {
                    showAlbum(itemPositon);
                    for (int i = 0; i < bottomAdapter.getItemCount(); i++) {
                        ((AlbumFolder) bottomAdapter.getListData().get(i)).setCheck(false);
                    }
                    ((AlbumFolder) bottomAdapter.getListData().get(itemPositon)).setCheck(true);
                    bottomAdapter.notifyItemRangeChanged(0, bottomAdapter.getItemCount());
                    dialog.dismiss();
                }

                @Override
                public void onMultiItemLongClick(View view, int viewPosition, int itemPositon) {

                }
            });
            dialog.setContentView(bottomView);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
        }
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        int tag = view.getId();
        if (tag == R.id.mFabMultiBut) {
            fabbutAnimator(mFabMultiBut);
        } else if (tag == R.id.mFabFloderBut) {
            showBottomDialog();
            fabbutAnimator(mFabMultiBut);
        } else if (tag == R.id.mFabDoneBut) {
            Intent intent = new Intent();
            ArrayList<String> pathList = new ArrayList<>();
            for (int i = 0, j = photoList.size(); i < j; i++) {
                pathList.add(photoList.get(i).getPhotoPath());
            }
            intent.putStringArrayListExtra(RZAlbum.ALBUM_IMAGE_PATH_LIST, pathList);
            setResult(RESULT_OK, intent);
            finish();
            fabbutAnimator(mFabMultiBut);
        }
    }

    public void fabbutAnimator(View view) {
        if (view.isSelected()) {
            AnimationHelper.hideFabButAnimation(mFabFloderBut, true);
            AnimationHelper.hideFabButAnimation(mFabDoneBut, false);
        } else {
            AnimationHelper.showFabButAnimation(mFabFloderBut, true);
            AnimationHelper.showFabButAnimation(mFabDoneBut, false);
        }
        view.setSelected(!view.isSelected());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int permissionResult = grantResults[0];
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE:
                if (grantResults.length > 0 && permissionResult == PackageManager.PERMISSION_GRANTED) {
                    mSingleExecutor.execute(scanner);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        showDescriptionDialog(1);
                    } else {
                        showDeniedDailog(1);
                    }
                }
                break;
            case PERMISSION_REQUEST_CAMERA:
                if (grantResults.length > 0 && permissionResult == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        showDescriptionDialog(2);
                    } else {
                        showDeniedDailog(2);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTIVITY_REQUEST_CAMERA:
                    // 掃描完檔案後在PC端即可發現 參考資料 : http://www.binkery.com/archives/473.html
                    connection = new MediaScannerConnection(this,
                            new MediaScannerConnection.MediaScannerConnectionClient() {
                                @Override
                                public void onMediaScannerConnected() {
                                    connection.scanFile(mCameraPath, null);
                                }

                                @Override
                                public void onScanCompleted(String s, Uri uri) {
                                    if (connection.isConnected()) {
                                        connection.disconnect();
                                        connection = null;
                                    }
                                    Intent intent = new Intent();
                                    ArrayList<String> pathList = new ArrayList<>();
                                    pathList.add(mCameraPath);
                                    intent.putStringArrayListExtra(RZAlbum.ALBUM_IMAGE_PATH_LIST, pathList);
                                    setResult(RESULT_OK, intent);
                                    RZAlbumActivity.super.finish();
                                }
                            });
                    connection.connect();
                    break;
                case ACTIVITY_REQUEST_PREVIEW:
                    photoList = data.getParcelableArrayListExtra(PreviewPhotoActivty.RZ_PREVIEW_PHOTOS);
                    List<AlbumPhoto> deleteList = data.getParcelableArrayListExtra(PreviewPhotoActivty.RZ_PREVIEW_DELETES);
                    // delete
                    for (int i = 0; i < deleteList.size(); i++) {
                        AlbumPhoto delete = deleteList.get(i);
                        int index = adapter.getListData().indexOf(delete);
                        ((AlbumPhoto) adapter.getListData().get(index)).setPickNumber(0);
                        adapter.notifyItemChanged(index);
                    }
                    // add
                    for (int j = 0; j < photoList.size(); j++) {
                        AlbumPhoto photo = photoList.get(j);
                        int index = adapter.getListData().indexOf(photo);
                        if (index != -1) {
                            ((AlbumPhoto) adapter.getListData().get(index)).setPickNumber(photo.getPickNumber());
                            adapter.notifyItemChanged(index);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
            connection = null;
        }
        if (!mSingleExecutor.isShutdown()) {
            mSingleExecutor.shutdown();
            mSingleExecutor = null;
        }
        super.onDestroy();
    }
}
