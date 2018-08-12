package com.rayzhang.android.rzalbum.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rayzhang.android.rzalbum.R;
import com.rayzhang.android.rzalbum.common.RZConfig;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;
import com.rayzhang.android.rzalbum.utils.Utils;
import com.rayzhang.android.rzalbum.widget.RZPhotoNumberView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Ray on 2017/8/20.
 * Photo preview
 */

public class PreviewPhotoActivity extends AppCompatActivity implements FragPreviewPhoto.OnNumberViewClickListener {
    private TextView rzIndexText;
    private RZPhotoNumberView rzNumberView;

    private ArrayList<AlbumPhoto> allPhotos;
    private ArrayList<AlbumPhoto> addPhotos, deletePhotos;
    private int pickColor = RZConfig.DEFAULT_PICK_COLOR;
    private int currentItem = 0;
    private int limitCount = RZConfig.DEFAULT_LIMIT_COUNT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photo);

        Utils.setStatusBarTransparent(this);

        allPhotos = getIntent().getParcelableArrayListExtra(RZConfig.PREVIEW_ALL_PHOTOS);
        addPhotos = getIntent().getParcelableArrayListExtra(RZConfig.PREVIEW_ADD_PHOTOS);
        deletePhotos = new ArrayList<>();

        currentItem = getIntent().getIntExtra(RZConfig.PREVIEW_ITEM_POSITION, 0);
        pickColor = getIntent().getIntExtra(RZConfig.PREVIEW_PICK_COLOR, RZConfig.DEFAULT_PICK_COLOR);
        limitCount = getIntent().getIntExtra(RZConfig.PREVIEW_LIMIT_COUNT, RZConfig.DEFAULT_LIMIT_COUNT);
        int orientation = getIntent().getIntExtra(RZConfig.PREVIEW_ORIENTATION, RZConfig.DEFAULT_ORIENTATION);
        if (orientation != RZConfig.ORIENTATION_AUTO) {
            setRequestedOrientation(orientation == 0 ?
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setupView();
    }

    private void setupView() {
        ViewPager rzViewPager = findViewById(R.id.rzViewPager);
        RelativeLayout rzBottomView = findViewById(R.id.rzBottomView);
        rzBottomView.setBackgroundColor(Color.argb(200, 255, 255, 255));
        findViewById(R.id.rzBackImgBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishPreviewPhoto();
            }
        });
        rzIndexText = findViewById(R.id.rzIndexText);
        rzNumberView = findViewById(R.id.rzNumberView);

        rzIndexText.setText(String.format(Locale.TAIWAN, "(%d/%d)", currentItem + 1, allPhotos.size()));
        rzNumberView.setNumber(addPhotos.size());
        rzNumberView.setPickColor(pickColor);

        FragPreviewAdapter fragPreviewAdapter = new FragPreviewAdapter(getSupportFragmentManager(), allPhotos, addPhotos,
                limitCount, new WeakReference<>(this));
        rzViewPager.setAdapter(fragPreviewAdapter);
        rzViewPager.setCurrentItem(currentItem, false);
        rzViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                rzIndexText.setText(String.format(Locale.TAIWAN, "(%d/%d)", position + 1, allPhotos.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void finishPreviewPhoto() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(RZConfig.PREVIEW_ADD_PHOTOS, addPhotos);
        intent.putParcelableArrayListExtra(RZConfig.PREVIEW_DELETE_PHOTOS, deletePhotos);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onNumberViewClick(View view, AlbumPhoto photo, boolean isAdd) {
        if (isAdd) {
            deletePhotos.remove(photo);
            allPhotos.get(currentItem).setPickNumber(photo.getPickNumber());
        } else {
            deletePhotos.add(photo);
            allPhotos.get(currentItem).setPickNumber(0);
        }
        for (int i = 0; i < addPhotos.size(); i++) {
            addPhotos.get(i).setPickNumber(i + 1);
            if (allPhotos.contains(addPhotos.get(i))) {
                int index = allPhotos.indexOf(addPhotos.get(i));
                allPhotos.get(index).setPickNumber(addPhotos.get(i).getPickNumber());
            }
        }
        rzNumberView.setNumber(addPhotos.size());
        rzNumberView.setPickColor(pickColor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishPreviewPhoto();
    }

    private static class FragPreviewAdapter extends FragmentStatePagerAdapter {
        private ArrayList<AlbumPhoto> allPhotos;
        private ArrayList<AlbumPhoto> addPhotos;
        private WeakReference<PreviewPhotoActivity> weakReference;
        private int limitCount;

        private FragPreviewAdapter(FragmentManager fm, ArrayList<AlbumPhoto> allPhotos, ArrayList<AlbumPhoto> addPhotos,
                                   int limitCount, WeakReference<PreviewPhotoActivity> weakReference) {
            super(fm);
            this.allPhotos = allPhotos;
            this.addPhotos = addPhotos;
            this.weakReference = weakReference;
            this.limitCount = limitCount;
        }

        @Override
        public Fragment getItem(int position) {
            FragPreviewPhoto fragPreviewPhoto = FragPreviewPhoto.instance(allPhotos.get(position), addPhotos,
                    limitCount);
            fragPreviewPhoto.setOnNumberViewClickListener(weakReference.get());
            return fragPreviewPhoto;
        }

        @Override
        public int getCount() {
            return allPhotos == null ? 0 : allPhotos.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }
}
