package com.rayzhang.android.rzalbum.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rayzhang.android.rzalbum.R;
import com.rayzhang.android.rzalbum.RZAlbum;
import com.rayzhang.android.rzalbum.adapter.factory.IItemType;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;
import com.rayzhang.android.rzalbum.widget.RZPhotoNumberView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ray on 2017/8/20.
 * Photo preview
 */

public class PreviewPhotoActivty extends AppCompatActivity implements FragPreviewPhoto.OnNumberViewClickListener {
    private TextView rzTextIndex;
    private RZPhotoNumberView rzNumberView;
    private List<? extends IItemType> albumPhotos;
    private List<AlbumPhoto> photoList, deleteList;
    private int pickColor;
    private int currentItem = 0;
    private int limitCount;

    public static final String RZ_PREVIEW_PHOTOS = "RZ_PREVIEW_PHOTOS";
    public static final String RZ_PREVIEW_DELETES = "RZ_PREVIEW_DELETES";
    public static final String RZ_PREVIEW_ALL_PHOTOS = "RZ_PREVIEW_ALL_PHOTOS";
    public static final String RZ_PREVIEW_ITEMPOSITION = "RZ_PREVIEW_ITEMPOSITION";
    public static final String RZ_PREVIEW_PICK_COLOR = "RZ_PREVIEW_PICK_COLOR";
    public static final String RZ_PREVIEW_LIMIT_COUNT = "RZ_PREVIEW_LIMIT_COUNT";
    public static final String RZ_PREVIEW_ORIENTATION = "RZ_PREVIEW_ORIENTATION";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_preview_photo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        photoList = getIntent().getParcelableArrayListExtra(RZ_PREVIEW_PHOTOS);
        albumPhotos = getIntent().getParcelableArrayListExtra(RZ_PREVIEW_ALL_PHOTOS);
        currentItem = getIntent().getIntExtra(RZ_PREVIEW_ITEMPOSITION, 0);
        pickColor = getIntent().getIntExtra(RZ_PREVIEW_PICK_COLOR, 0);
        limitCount = getIntent().getIntExtra(RZ_PREVIEW_LIMIT_COUNT, 0);
        int orientation = getIntent().getIntExtra(RZ_PREVIEW_ORIENTATION, 0);
        deleteList = new ArrayList<>();
        if (orientation != RZAlbum.ORIENTATION_AUTO) {
            setRequestedOrientation(orientation == 0 ?
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        initView();
    }

    private void initView() {
        RelativeLayout rzBottomView = (RelativeLayout) findViewById(R.id.rzBottomView);
        rzBottomView.setBackgroundColor(Color.argb(200, 255, 255, 255));
        ImageButton mImgView = (ImageButton) findViewById(R.id.rzImgBackBut);
        rzTextIndex = (TextView) findViewById(R.id.rzTextIndex);
        rzNumberView = (RZPhotoNumberView) findViewById(R.id.rzNumberView);

        rzTextIndex.setText(String.format(Locale.TAIWAN, "(%d/%d)", currentItem + 1, albumPhotos.size()));
        rzNumberView.setNumber(photoList.size());
        rzNumberView.setPickColor(pickColor);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.rzViewPager);
        FragPreviewAdapter fragPreviewAdapter = new FragPreviewAdapter(getSupportFragmentManager(), albumPhotos,
                (ArrayList<AlbumPhoto>) photoList, limitCount, new WeakReference<>(this));

        mViewPager.setAdapter(fragPreviewAdapter);
        mViewPager.setCurrentItem(currentItem, false);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                rzTextIndex.setText(String.format(Locale.TAIWAN, "(%d/%d)", position + 1, albumPhotos.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishPreviewPhoto();
            }
        });
    }

    @Override
    public void onNumberViewClick(View view, AlbumPhoto photo, boolean isAdd) {
        if (isAdd) {
            deleteList.remove(photo);
            ((AlbumPhoto) albumPhotos.get(currentItem)).setPickNumber(photo.getPickNumber());
        } else {
            deleteList.add(photo);
            ((AlbumPhoto) albumPhotos.get(currentItem)).setPickNumber(0);
        }
        for (int i = 0; i < photoList.size(); i++) {
            photoList.get(i).setPickNumber(i + 1);
            if (albumPhotos.contains(photoList.get(i))) {
                int index = albumPhotos.indexOf(photoList.get(i));
                ((AlbumPhoto) albumPhotos.get(index)).setPickNumber(photoList.get(i).getPickNumber());
            }
        }
        rzNumberView.setNumber(photoList.size());
        rzNumberView.setPickColor(pickColor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishPreviewPhoto();
    }

    private void finishPreviewPhoto() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(RZ_PREVIEW_PHOTOS, (ArrayList<? extends Parcelable>) photoList);
        intent.putParcelableArrayListExtra(RZ_PREVIEW_DELETES, (ArrayList<? extends Parcelable>) deleteList);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    private static class FragPreviewAdapter extends FragmentStatePagerAdapter {
        private List<? extends IItemType> list;
        private ArrayList<AlbumPhoto> photoList;
        private int limitCount;
        private WeakReference<PreviewPhotoActivty> weakReference;

        private FragPreviewAdapter(FragmentManager fm, List<? extends IItemType> list, ArrayList<AlbumPhoto> photoList,
                                   int limitCount, WeakReference<PreviewPhotoActivty> weakReference) {
            super(fm);
            this.list = list;
            this.photoList = photoList;
            this.limitCount = limitCount;
            this.weakReference = weakReference;
        }

        @Override
        public Fragment getItem(int position) {
            FragPreviewPhoto fragPreviewPhoto = FragPreviewPhoto.instance((AlbumPhoto) list.get(position), photoList,
                    limitCount);
            fragPreviewPhoto.setOnNumberViewClickListener(weakReference.get());
            return fragPreviewPhoto;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
