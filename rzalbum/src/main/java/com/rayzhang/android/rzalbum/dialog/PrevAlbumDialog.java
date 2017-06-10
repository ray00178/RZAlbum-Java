package com.rayzhang.android.rzalbum.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rayzhang.android.rzalbum.R;
import com.rayzhang.android.rzalbum.RZAlbumActivity;
import com.rayzhang.android.rzalbum.module.bean.AlbumPhoto;
import com.rayzhang.android.rzalbum.module.listener.OnPrevBoxClickListener;
import com.rayzhang.android.rzalbum.widget.RZZoomImgView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ray on 2017/2/10.
 */

public class PrevAlbumDialog extends AppCompatDialog implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    /**
     * 瀏覽照片
     */
    private OnPrevBoxClickListener listener;
    private List<AlbumPhoto> albumPhotos;
    private CheckBox mChkBox;
    private TextView mTextIndex;
    private ArrayList<RZZoomImgView> listViews;
    // 紀錄現在的瀏覽的索引
    private int currentPos = 0;

    public PrevAlbumDialog(RZAlbumActivity albumActivity, List<AlbumPhoto> albumPhotos, OnPrevBoxClickListener listener) {
        super(albumActivity, R.style.RZ_AlbumPrevDialogStyle);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rz_prev_album_dialog);

        // 2017-06-10 Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        /*int statusBarHeight = -1;
        // 獲取status_bar_height資源的ID
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            // 根據資源ID獲取對應的尺寸值
            statusBarHeight = getContext().getResources().getDimensionPixelSize(resourceId);
        }*/
        // 設置Dialog為全屏
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = metrics.widthPixels;
        //p.height = metrics.heightPixels - statusBarHeight;
        // 2017-06-10 更改
        p.height = metrics.heightPixels;
        p.gravity = Gravity.CENTER;
        this.getWindow().setAttributes(p);

        this.listener = listener;
        this.albumPhotos = albumPhotos;

        listViews = new ArrayList<>();
        for (int i = 0, j = albumPhotos.size(); i < j; i++) {
            RZZoomImgView mZoomView = new RZZoomImgView(getContext());
            Glide.with(getContext())
                    .load(albumPhotos.get(i).getPhotoPath())
                    .crossFade()
                    .into(mZoomView);
            listViews.add(mZoomView);
        }
        initView();
    }

    private void initView() {
        RelativeLayout rzPrevBottomView = (RelativeLayout) findViewById(R.id.rzPrevBottomView);
        rzPrevBottomView.setBackgroundColor(Color.argb(128, 0, 0, 0));
        ImageView mImgView = (ImageView) findViewById(R.id.mImgView);
        mChkBox = (CheckBox) findViewById(R.id.mChkBox);
        mTextIndex = (TextView) findViewById(R.id.mTextIndex);
        mTextIndex.setText(String.format(Locale.getDefault(), "(%d/%d)", 1, listViews.size()));
        mChkBox.setChecked(true);
        mChkBox.setOnCheckedChangeListener(this);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mViewPager.setOffscreenPageLimit(listViews == null ? 0 : listViews.size());
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return listViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == listViews.get(Integer.parseInt(object.toString()));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(listViews.get(position));
                return position;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(listViews.get(position));
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPos = position;
                if (albumPhotos.get(position).isCheck()) {
                    mChkBox.setChecked(true);
                } else {
                    mChkBox.setChecked(false);
                }
                mTextIndex.setText(String.format(Locale.getDefault(), "(%d/%d)", position + 1, listViews.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mImgView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mImgView) dismiss();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
        albumPhotos.get(currentPos).setCheck(isCheck);
        listener.onCheck(albumPhotos.get(currentPos).isCheck(), albumPhotos.get(currentPos).getPhotoIndex());
    }
}
