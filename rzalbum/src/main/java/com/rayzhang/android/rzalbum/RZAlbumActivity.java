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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rayzhang.android.rzalbum.adapter.MultiAdapter;
import com.rayzhang.android.rzalbum.adapter.itemdecoration.RecycleItemDecoration;
import com.rayzhang.android.rzalbum.adapter.listener.OnMultiItemClickListener;
import com.rayzhang.android.rzalbum.common.RZConfig;
import com.rayzhang.android.rzalbum.model.AlbumFolder;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;
import com.rayzhang.android.rzalbum.utils.AlbumScanner;
import com.rayzhang.android.rzalbum.utils.AnimationHelper;
import com.rayzhang.android.rzalbum.utils.DisplayUtils;
import com.rayzhang.android.rzalbum.utils.FileProviderUtils;
import com.rayzhang.android.rzalbum.utils.MainHandler;
import com.rayzhang.android.rzalbum.utils.Utils;
import com.rayzhang.android.rzalbum.view.PreviewPhotoActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RZAlbumActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = RZAlbumActivity.class.getSimpleName();
    private final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private final int PERMISSION_REQUEST_STORAGE = 6666;
    private final int PERMISSION_REQUEST_CAMERA = PERMISSION_REQUEST_STORAGE + 1;
    private final int ACTIVITY_REQUEST_CAMERA = 7777;
    private final int ACTIVITY_REQUEST_PREVIEW = ACTIVITY_REQUEST_CAMERA + 1;

    private String appName = RZConfig.APP_NAME;
    private String toolBarTitle = RZConfig.TOOLBAR_TITLE;
    private int spanCount = RZConfig.DEFAULT_SPAN_COUNT;
    private int limitCount = RZConfig.DEFAULT_LIMIT_COUNT;
    private int statusBarColor = RZConfig.DEFAULT_STATUS_BAR_COLOR;
    private int toolBarColor = RZConfig.DEFAULT_TOOLBAR_COLOR;
    private int pickColor = RZConfig.DEFAULT_PICK_COLOR;
    private boolean showCamera = RZConfig.DEFAULT_SHOW_CAMERA;
    private boolean showGif = RZConfig.DEFAULT_SHOW_GIF;
    private int orientation = RZConfig.DEFAULT_ORIENTATION;
    private String folderName = "";
    private int dialogIcon = -1;

    private RecyclerView.LayoutManager mLayoutManager;
    private MultiAdapter<AlbumPhoto> mMultiAdapter;
    private FloatingActionButton mFabMultiBut, mFabFolderBut, mFabDoneBut;

    private BottomSheetDialog mBottomSheetDialog;
    private RecyclerView mBottomRecyclerView;
    private MultiAdapter<AlbumFolder> mBottomAdapter;

    // 相簿資料夾
    private List<AlbumFolder> mAlbumFolders;
    // 紀錄選擇到的Photo
    private List<AlbumPhoto> addPhotos;
    // 儲存照片的路徑
    private String mCameraPath;

    private ExecutorService mSingleExecutor;
    private Runnable scanAlbumRunnable = new Runnable() {
        @Override
        public void run() {
            folderName = folderName.isEmpty() ? getResources().getString(R.string.rz_album_all_folder_name) : folderName;
            mAlbumFolders = AlbumScanner.instances(pickColor, showGif).getPhotoAlbum(RZAlbumActivity.this, folderName);
            MainHandler.instances().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()) {
                        mAlbumFolders.clear();
                        mAlbumFolders = null;
                    } else {
                        showAlbum(0);
                    }
                }
            }, 200L);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rzalbum);

        DisplayUtils.instance(this.getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            appName = bundle.getString(RZConfig.APP_NAME, RZConfig.DEFAULT_APP_NAME);
            limitCount = bundle.getInt(RZConfig.LIMIT_COUNT, RZConfig.DEFAULT_LIMIT_COUNT);
            spanCount = bundle.getInt(RZConfig.SPAN_COUNT, RZConfig.DEFAULT_SPAN_COUNT);
            statusBarColor = bundle.getInt(RZConfig.STATUS_BAR_COLOR, RZConfig.DEFAULT_STATUS_BAR_COLOR);
            toolBarTitle = bundle.getString(RZConfig.TOOLBAR_TITLE, RZConfig.DEFAULT_TOOLBAR_TITLE);
            toolBarColor = bundle.getInt(RZConfig.TOOLBAR_COLOR, RZConfig.DEFAULT_TOOLBAR_COLOR);
            showCamera = bundle.getBoolean(RZConfig.SHOW_CAMERA, RZConfig.DEFAULT_SHOW_CAMERA);
            showGif = bundle.getBoolean(RZConfig.SHOW_GIF, RZConfig.DEFAULT_SHOW_GIF);
            orientation = bundle.getInt(RZConfig.PREVIEW_ORIENTATION, RZConfig.ORIENTATION_AUTO);
            pickColor = bundle.getInt(RZConfig.PICK_COLOR, RZConfig.DEFAULT_PICK_COLOR);
            folderName = bundle.getString(RZConfig.ALL_FOLDER_NAME, "");
            dialogIcon = bundle.getInt(RZConfig.DIALOG_ICON, -1);
        }

        mSingleExecutor = Executors.newSingleThreadExecutor();
        addPhotos = new ArrayList<>();
        setupView();
        requestScanPhotos();
    }

    private void setupView() {
        Utils.setStatusBarColor(this, statusBarColor);
        Toolbar mToolBar = findViewById(R.id.mToolBar);
        mToolBar.setTitle(toolBarTitle);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setBackgroundColor(toolBarColor);
        mToolBar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        if (showCamera) {
            mToolBar.getMenu().add(0, 0, 0, getResources().getString(R.string.rz_album_menu_camera))
                    .setIcon(R.drawable.ic_camera).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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

        RecyclerView mRZRecyclerView = findViewById(R.id.mRZRecyclerView);
        mLayoutManager = new GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false);
        mRZRecyclerView.setLayoutManager(mLayoutManager);
        mRZRecyclerView.setHasFixedSize(true);
        mRZRecyclerView.setItemAnimator(null);
        int itemSize = 1;
        mRZRecyclerView.addItemDecoration(new RecycleItemDecoration(itemSize, Color.argb(255, 255, 255, 255)));
        int itemWH = (DisplayUtils.screenW - (itemSize * (spanCount - 1))) / spanCount;
        mMultiAdapter = new MultiAdapter<>(null, itemWH);
        mRZRecyclerView.setAdapter(mMultiAdapter);

        mMultiAdapter.setOnItemClickListener(new OnMultiItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int viewPosition, int itemPosition) {
                if (viewPosition == 0) {
                    photoPreview(itemPosition);
                } else {
                    photoPick(itemPosition);
                }
            }
        });

        mFabMultiBut = findViewById(R.id.mFabMultiBut);
        mFabFolderBut = findViewById(R.id.mFabFolderBut);
        mFabDoneBut = findViewById(R.id.mFabDoneBut);
        mFabMultiBut.setAlpha(.8f);
        mFabMultiBut.setOnClickListener(this);
        mFabFolderBut.setOnClickListener(this);
        mFabDoneBut.setOnClickListener(this);

        // Bottom Adapter
        mBottomRecyclerView = new RecyclerView(this);
        mBottomRecyclerView.setBackgroundColor(Color.argb(255, 255, 255, 255));
        mBottomRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBottomRecyclerView.setLayoutManager(manager);
        mBottomRecyclerView.setHasFixedSize(true);
        mBottomRecyclerView.addItemDecoration(new RecycleItemDecoration(1, Color.LTGRAY));
        mBottomAdapter = new MultiAdapter<>(null);
        mBottomRecyclerView.setAdapter(mBottomAdapter);
    }

    private void requestScanPhotos() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionResult = ContextCompat.checkSelfPermission(this, PERMISSION_READ_EXTERNAL_STORAGE);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_READ_EXTERNAL_STORAGE)) {
                    // 第一次被使用者拒絕後，這邊做些解釋的動作
                    showDescriptionDialog(1);
                } else {
                    // 第一次詢問
                    ActivityCompat.requestPermissions(this,
                            new String[]{PERMISSION_READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_STORAGE);
                }
            } else {
                mSingleExecutor.execute(scanAlbumRunnable);
            }
        } else {
            mSingleExecutor.execute(scanAlbumRunnable);
        }
    }

    private void requestOpenCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionResult = ContextCompat.checkSelfPermission(this, PERMISSION_CAMERA);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_CAMERA)) {
                    showDescriptionDialog(2);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{PERMISSION_CAMERA},
                            PERMISSION_REQUEST_CAMERA);
                }
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }

    private void showAlbum(int index) {
        mMultiAdapter.resetData(mAlbumFolders.get(index).getFolderPhotos());
        mLayoutManager.scrollToPosition(0);
        //changePhotoStatus();
    }

    private void openCamera() {
        // 打開手機的照相機
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判斷手機上是否有可以啟動照相機的應用程式
        if (camera.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            // 照片命名
            String uuid = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = String.format(Locale.TAIWAN, "JPEG_%s.jpg", uuid);
            // 建立目錄
            File dcimFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            // 如果目錄不存在就建立
            if (!dcimFile.exists()) {
                boolean isCreate = dcimFile.mkdirs();
            }
            // 建立檔案(存放的位置, 檔名)
            // 拍照時，將拍得的照片先保存在指定的資料夾中(未缩小)
            photoFile = new File(dcimFile, fileName);

            // 照片存放路徑
            mCameraPath = photoFile.getAbsolutePath();
            // 拍照適配Android7.0 up
            Uri fileUri = FileProviderUtils.getUriForFile(this, photoFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProviderUtils.grantPermissions(this, camera, fileUri, true);
            }
            // 2017-06-11 更改
            // 如果指定了圖片uri，data就没有數據，如果没有指定uri，則data就返回有數據
            // 指定圖片输出位置，若無這句則拍照後，圖片會放入內存中，由於占用内存太大導致無法剪切或者剪切後無法保存
            //camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            camera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(camera, ACTIVITY_REQUEST_CAMERA);
        } else {
            Toast.makeText(this, getResources().getString(R.string.rz_album_camera_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    private void photoPreview(int itemPosition) {
        Intent preview = new Intent(this, PreviewPhotoActivity.class);
        preview.putParcelableArrayListExtra(RZConfig.PREVIEW_ADD_PHOTOS, (ArrayList<? extends Parcelable>) addPhotos);
        preview.putParcelableArrayListExtra(RZConfig.PREVIEW_ALL_PHOTOS, (ArrayList<? extends Parcelable>) mMultiAdapter.getDatas());
        preview.putExtra(RZConfig.PREVIEW_ITEM_POSITION, itemPosition);
        preview.putExtra(RZConfig.PREVIEW_PICK_COLOR, pickColor);
        preview.putExtra(RZConfig.PREVIEW_LIMIT_COUNT, limitCount);
        preview.putExtra(RZConfig.PREVIEW_ORIENTATION, orientation);
        startActivityForResult(preview, ACTIVITY_REQUEST_PREVIEW);
        overridePendingTransition(0, 0);
    }

    private void photoPick(int itemPosition) {
        AlbumPhoto photo = mMultiAdapter.getDatas().get(itemPosition);
        if (photo.getPickNumber() == 0) {
            if (addPhotos.size() == limitCount) {
                Toast.makeText(RZAlbumActivity.this, String.format(Locale.TAIWAN, getResources().getString(R.string.rz_album_limit_count), limitCount),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            photo.setPickNumber(addPhotos.size() + 1);
            addPhotos.add(photo);
            mMultiAdapter.notifyItemChanged(itemPosition);
        } else {
            if (addPhotos.size() == 0) return;
            addPhotos.remove(photo);
            photo.setPickNumber(0);
            mMultiAdapter.notifyItemChanged(itemPosition);
            changePhotoPickNumber();
        }
    }

    private void changePhotoPickNumber() {
        // 改變item status & index
        for (int i = 0; i < addPhotos.size(); i++) {
            addPhotos.get(i).setPickNumber(i + 1);
            int index = mMultiAdapter.getDatas().indexOf(addPhotos.get(i));
            if (index != -1) {
                mMultiAdapter.getDatas().get(index).setPickNumber(i + 1);
                mMultiAdapter.notifyItemChanged(index);
            }
        }
    }

    private void showDescriptionDialog(final int type) {
        String title = type == 1 ? getResources().getString(R.string.rz_album_dia_read_description) : getResources().getString(R.string.rz_album_dia_camera_description);
        String msg = type == 1 ? getResources().getString(R.string.rz_album_dia_read_message) : getResources().getString(R.string.rz_album_dia_camera_message);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.rz_album_dia_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 再一次請求
                        ActivityCompat.requestPermissions(RZAlbumActivity.this,
                                type == 1 ? new String[]{PERMISSION_READ_EXTERNAL_STORAGE} : new String[]{PERMISSION_CAMERA},
                                type == 1 ? PERMISSION_REQUEST_STORAGE : PERMISSION_REQUEST_CAMERA);
                    }
                })
                .setNegativeButton(R.string.rz_album_dia_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (type == 1) RZAlbumActivity.this.finish();
                    }
                });
        if (dialogIcon != -1) builder.setIcon(dialogIcon);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(pickColor);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(pickColor);
            }
        });
        dialog.show();
    }

    private void showDeniedDialog(final int type) {
        String title = type == 1 ? getResources().getString(R.string.rz_album_dia_read_description_denied) : getResources().getString(R.string.rz_album_dia_camera_description_denied);
        String msg = type == 1 ?
                String.format(Locale.TAIWAN, getResources().getString(R.string.rz_album_dia_read_message_denied), appName) :
                String.format(Locale.TAIWAN, getResources().getString(R.string.rz_album_dia_camera_message_denied), appName);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.rz_album_dia_go_setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        Utils.goAppSettingPage(RZAlbumActivity.this);
                        if (type == 1) RZAlbumActivity.this.finish();
                    }
                });
        if (dialogIcon != -1) builder.setIcon(dialogIcon);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(pickColor);
            }
        });
        dialog.show();
    }

    public void fatButHideAnimation(View view) {
        if (view.isSelected()) {
            AnimationHelper.hideFabButAnimation(mFabFolderBut, true);
            AnimationHelper.hideFabButAnimation(mFabDoneBut, false);
        } else {
            AnimationHelper.showFabButAnimation(mFabFolderBut, true);
            AnimationHelper.showFabButAnimation(mFabDoneBut, false);
        }
        view.setSelected(!view.isSelected());
    }

    private void showBottomDialog() {
        if (mAlbumFolders.get(0).getFolderPhotos().size() == 0) return;
        if (mBottomSheetDialog == null) {
            mBottomSheetDialog = new BottomSheetDialog(this);
            mBottomAdapter.resetData(mAlbumFolders);
            mBottomAdapter.setOnItemClickListener(new OnMultiItemClickListener() {
                @Override
                public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int viewPosition, int itemPosition) {
                    showAlbum(itemPosition);
                    for (int i = 0; i < mBottomAdapter.getItemCount(); i++) {
                        mBottomAdapter.getDatas().get(i).setCheck(false);
                    }
                    mBottomAdapter.getDatas().get(itemPosition).setCheck(true);
                    mBottomAdapter.notifyItemRangeChanged(0, mBottomAdapter.getItemCount());
                    mBottomSheetDialog.dismiss();
                }
            });
            mBottomSheetDialog.setContentView(mBottomRecyclerView);
            mBottomSheetDialog.setCancelable(true);
            mBottomSheetDialog.setCanceledOnTouchOutside(true);
        }
        mBottomSheetDialog.show();
    }

    @Override
    public void onClick(View view) {
        int tag = view.getId();
        if (tag == R.id.mFabMultiBut) {
            fatButHideAnimation(mFabMultiBut);
        } else if (tag == R.id.mFabFolderBut) {
            showBottomDialog();
            fatButHideAnimation(mFabMultiBut);
        } else if (tag == R.id.mFabDoneBut) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(RZConfig.RESULT_PHOTOS, (ArrayList<? extends Parcelable>) addPhotos);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int permissionResult = grantResults[0];
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE:
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    mSingleExecutor.execute(scanAlbumRunnable);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        showDescriptionDialog(1);
                    } else {
                        showDeniedDialog(1);
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
                        showDeniedDialog(2);
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
                    // 拍照完後透過掃描可在手機端發現剛拍完照的檔案 參考資料 : https://www.jianshu.com/p/bc8b04bffddf
                    MediaScannerConnection.scanFile(this, new String[]{mCameraPath}, new String[]{RZConfig.JPEG},
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, final Uri uri) {
                                    mSingleExecutor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            final AlbumPhoto photo = AlbumScanner.instances(pickColor, showGif)
                                                    .getSinglePhoto(RZAlbumActivity.this, uri);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent result = new Intent();
                                                    if (photo != null) {
                                                        ArrayList<AlbumPhoto> list = new ArrayList<>();
                                                        list.add(photo);
                                                        result.putParcelableArrayListExtra(RZConfig.RESULT_PHOTOS, list);
                                                    }
                                                    setResult(RESULT_OK, result);
                                                    finish();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                    break;
                case ACTIVITY_REQUEST_PREVIEW:
                    if (data != null) {
                        addPhotos = data.getParcelableArrayListExtra(RZConfig.PREVIEW_ADD_PHOTOS);
                        List<AlbumPhoto> deletePhotos = data.getParcelableArrayListExtra(RZConfig.PREVIEW_DELETE_PHOTOS);
                        // delete
                        for (int i = 0; i < deletePhotos.size(); i++) {
                            int index = mMultiAdapter.getDatas().indexOf(deletePhotos.get(i));
                            mMultiAdapter.getDatas().get(index).setPickNumber(0);
                            mMultiAdapter.notifyItemChanged(index);
                        }
                        // add
                        for (int j = 0; j < addPhotos.size(); j++) {
                            AlbumPhoto photo = addPhotos.get(j);
                            int index = mMultiAdapter.getDatas().indexOf(photo);
                            if (index != -1) {
                                mMultiAdapter.getDatas().get(index).setPickNumber(photo.getPickNumber());
                                mMultiAdapter.notifyItemChanged(index);
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (!mSingleExecutor.isShutdown()) {
            mSingleExecutor.shutdown();
            mSingleExecutor = null;
        }
        super.onDestroy();
    }
}
