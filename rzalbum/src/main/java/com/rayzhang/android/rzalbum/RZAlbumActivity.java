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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rayzhang.android.rzalbum.adapter.AlbumAdapter;
import com.rayzhang.android.rzalbum.adapter.SelectAlbumAdapter;
import com.rayzhang.android.rzalbum.dialog.PrevAlbumDialog;
import com.rayzhang.android.rzalbum.itemdecoration.RecycleItemDecoration;
import com.rayzhang.android.rzalbum.module.bean.AlbumFolder;
import com.rayzhang.android.rzalbum.module.listener.OnPrevBoxClickListener;
import com.rayzhang.android.rzalbum.utils.AlbumScanner;
import com.rayzhang.android.rzalbum.utils.FileProviderUtils;
import com.rayzhang.android.rzalbum.utils.Poster;
import com.rayzhang.android.rzalbum.widget.RZIconTextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RZAlbumActivity extends AppCompatActivity implements View.OnClickListener, OnPrevBoxClickListener {
    private static final String TAG = RZAlbumActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_STORAGE = 9998;
    private static final int PERMISSION_REQUEST_CAMERA = 9999;
    private static final int ACTIVITY_REQUEST_CAMERA = 9997;
    private static final int MAX_COUNT = 5;
    private static final int SPAN_COUNT = 3;
    private static final int STATUSBAR_COLOR = Color.parseColor("#0a7e07");
    private static final int TOOLBAR_COLOR = Color.parseColor("#259b24");
    private static final String TOOLBAR_TITLE = "RZAlbum";
    private static final String APP_NAME = "App名稱";
    // 參考資料 : ExecutorService
    // http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2013/0304/958.html
    // http://www.cnblogs.com/whoislcj/p/5607734.html
    private static ExecutorService sRunnableExecutor = Executors.newSingleThreadExecutor();

    private AlbumAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RZIconTextView mTextFolder, mTextPrev;
    private String appName, toolBarTitle;
    private int spanCount, limitCount;
    private int toolBarColor;
    private int dialogIcon;
    private boolean isShow;

    private List<AlbumFolder> mAlbumFolders;
    private BottomSheetDialog dialog;
    private SelectAlbumAdapter adapterSel;

    private MediaScannerConnection connection;
    private String mCameraPath;

    private PrevAlbumDialog prevAlbumDialog;
    // 紀錄bottomDialog點選到的相簿
    private int saveClickPos = 0;

    /**
     * 2017-02-26 添加預覽時statusBarColor改成黑色，離開時改回原來的Color
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rzalbum);

        Bundle bundle = getIntent().getExtras();
        appName = bundle.getString(RZAlbum.ALBUM_APP_NAME, APP_NAME);
        limitCount = bundle.getInt(RZAlbum.ALBUM_LIMIT_COUNT, MAX_COUNT);
        spanCount = bundle.getInt(RZAlbum.ALBUM_SPAN_COUNT, SPAN_COUNT);
        int statusBarColor = bundle.getInt(RZAlbum.ALBUM_STATUSBAR_COLOR, STATUSBAR_COLOR);
        toolBarTitle = bundle.getString(RZAlbum.ALBUM_TOOLBAR_TITLE, TOOLBAR_TITLE);
        toolBarColor = bundle.getInt(RZAlbum.ALBUM_TOOLBAR_COLOR, TOOLBAR_COLOR);
        dialogIcon = bundle.getInt(RZAlbum.ALBUM_DIALOG_ICON, R.drawable.ic_info_description_30_3dp);
        isShow = bundle.getBoolean(RZAlbum.ALBUN_SHOW_CAMERA, true);
        initView();
        setStatusBarColor(statusBarColor);
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

        RecyclerView mRecyView = (RecyclerView) findViewById(R.id.mRecyView);
        mLayoutManager = new GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false);
        mRecyView.setLayoutManager(mLayoutManager);
        mRecyView.setHasFixedSize(true);
        mRecyView.addItemDecoration(new RecycleItemDecoration(1, Color.argb(255, 255, 255, 255)));
        adapter = new AlbumAdapter(this, limitCount, isShow);
        mRecyView.setAdapter(adapter);

        RelativeLayout mBottomBar = (RelativeLayout) findViewById(R.id.mBottomBar);
        mTextFolder = (RZIconTextView) findViewById(R.id.mTextFolder);
        mTextPrev = (RZIconTextView) findViewById(R.id.mTextPrev);
        ImageView mImgDone = (ImageView) findViewById(R.id.mImgDone);

        mTextPrev.setText(String.format(Locale.getDefault(), "(%d/%d)", 0, limitCount));
        mBottomBar.setBackgroundColor(Color.argb(128, 0, 0, 0));
        mTextFolder.setDrawablePad(5);
        mTextPrev.setDrawablePad(5);

        mTextFolder.setOnClickListener(this);
        mTextPrev.setOnClickListener(this);
        mImgDone.setOnClickListener(this);
    }

    private void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor);
                window.setNavigationBarColor(ContextCompat.getColor(this, R.color.rz_colorBlack));
            }
        }
    }

    private void scanAllAlbum() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // 第一次被使用者拒絕後，這邊做些解釋的動作
                    showDescriptionDialog(1);
                } else {
                    // 第一次詢問
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_STORAGE);
                }
            } else {
                sRunnableExecutor.execute(scanner);
            }
        } else {
            sRunnableExecutor.execute(scanner);
        }
    }

    /**
     * Scan image.
     */
    private Runnable scanner = new Runnable() {
        @Override
        public void run() {
            mAlbumFolders = AlbumScanner.instances().getPhotoAlbum(RZAlbumActivity.this);
            Poster.instances().postDelayed(initialize, 200);
        }
    };

    /**
     * init ui
     */
    private Runnable initialize = new Runnable() {
        @Override
        public void run() {
            if (!RZAlbumActivity.this.isFinishing()) {
                showAlbum(0);
            } else {
                mAlbumFolders.clear();
                mAlbumFolders = null;
            }
        }
    };

    private void showAlbum(int index) {
        AlbumFolder folder = mAlbumFolders.get(index);
        mTextFolder.setText(folder.getFolderName());
        adapter.addDatas(mAlbumFolders.get(index).getFolderPhotos());
        mLayoutManager.scrollToPosition(0);
        adapter.setOnPhotoItemClick(new AlbumAdapter.OnPhotoItemClickListener() {
            @Override
            public void onCameraItemClick(View view) {
                requestOpenCamera();
            }

            @Override
            public void onPhotoItemClick(boolean isOver, int count) {
                mTextPrev.setText(String.format(Locale.getDefault(), "(%d/%d)", count, limitCount));
                if (isOver) {
                    Toast.makeText(RZAlbumActivity.this, String.format(Locale.getDefault(), "最多選擇%d張", limitCount), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判斷手機上是否有可以啟動照相機的應用程式
        if (camaraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                // 拍照時，將拍得的照片先保存在指定的資料夾中（未缩小）
                photoFile = createImgFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                // 拍照適配Android7.0 up
                Uri fileUri = FileProviderUtils.getUriForFile(this, photoFile);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileProviderUtils.grantPermissions(this, camaraIntent, fileUri, true);
                }
                // 2017-06-11 更改
                // 如果指定了圖片uri，data就没有數據，如果没有指定uri，則data就返回有數據
                // 指定圖片输出位置，若無這句則拍照後，圖片會放入內存中，由於占用内存太大導致無法剪切或者剪切後無法保存
                //camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(camaraIntent, ACTIVITY_REQUEST_CAMERA);
            }
        }
    }

    private File createImgFile() throws IOException {
        // 照片命名
        String timeTemp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imgFileName = "JEPG_" + timeTemp + ".jpg";
        // 建立目錄
        String imgPath = Environment.getExternalStorageDirectory() + "/RZAlbum_images";
        File storeDir = new File(imgPath);
        // 如果目錄不存在就建立
        if (!storeDir.exists()) {
            storeDir.mkdir();
        }
        File imgFile = new File(storeDir, imgFileName);
        // 建立檔案(存放的位置, 檔名)
        mCameraPath = imgFile.getAbsolutePath();
        return imgFile;
    }

    /**
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

    /**
     * 權限拒絕已達2次 or 權限已被永久拒絕
     * type = 1(讀取記憶卡) type = 2(拍攝照片)
     */
    private void showIsDenideDialog(final int type) {
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
            RecyclerView mBottomRecyView = (RecyclerView) LayoutInflater.from(this).inflate(R.layout.rz_sel_album_adapter, null);
            mBottomRecyView.setHasFixedSize(true);
            mBottomRecyView.addItemDecoration(new RecycleItemDecoration(1, Color.LTGRAY));
            RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mBottomRecyView.setLayoutManager(manager);
            adapterSel = new SelectAlbumAdapter(this, mAlbumFolders);
            mBottomRecyView.setAdapter(adapterSel);
            dialog.setContentView(mBottomRecyView);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            adapterSel.setOnBottomDialogItemClickListener(new SelectAlbumAdapter.OnSelAlbumItemClickListener() {
                @Override
                public void onSelAlbumItemClick(int position) {
                    saveClickPos = position;
                    showAlbum(position);
                    adapterSel.setCheckFolder(position);
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.mTextFolder) {
            showBottomDialog();
        } else if (id == R.id.mTextPrev) {
            if (adapter.getPhotos().size() == 0) return;
            prevAlbumDialog = new PrevAlbumDialog(this, adapter.getPhotos(), this);
            // 2017-06-10 將statusBar Hide
            /*prevAlbumDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    setStatusBarColor(statusBarColor);
                }
            });
            setStatusBarColor(Color.BLACK);*/
            prevAlbumDialog.show();
        } else {
            Intent intent = new Intent();
            ArrayList<String> pathList = new ArrayList<>();
            for (int i = 0, j = adapter.getPhotos().size(); i < j; i++) {
                pathList.add(adapter.getPhotos().get(i).getPhotoPath());
            }
            intent.putStringArrayListExtra(RZAlbum.ALBUM_IMAGE_PATH_LIST, pathList);
            setResult(RESULT_OK, intent);
            RZAlbumActivity.super.finish();
        }
    }

    @Override
    public void onCheck(boolean isCheck, int photoIndex) {
        mAlbumFolders.get(0).getFolderPhotos().get(photoIndex).setCheck(isCheck);
        adapter.refreshDatas(isCheck, photoIndex, mAlbumFolders.get(saveClickPos).getFolderPhotos());
        mTextPrev.setText(String.format(Locale.getDefault(), "(%d/%d)", adapter.getPhotos().size(), limitCount));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int permissionResult = grantResults[0];
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE:
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    sRunnableExecutor.execute(scanner);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        showDescriptionDialog(1);
                    } else {
                        showIsDenideDialog(1);
                    }
                }
                break;
            case PERMISSION_REQUEST_CAMERA:
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        showDescriptionDialog(2);
                    } else {
                        showIsDenideDialog(2);
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
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (prevAlbumDialog != null && prevAlbumDialog.isShowing()) prevAlbumDialog.dismiss();
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
            connection = null;
        }
        super.onDestroy();
    }
}
