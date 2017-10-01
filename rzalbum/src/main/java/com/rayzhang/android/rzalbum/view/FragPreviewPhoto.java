package com.rayzhang.android.rzalbum.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.rayzhang.android.rzalbum.R;
import com.rayzhang.android.rzalbum.base.BaseLazyFragment;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;
import com.rayzhang.android.rzalbum.widget.RZPhotoNumberView;
import com.rayzhang.android.rzalbum.widget.RZZoomImgView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ray on 2017/8/20.
 * Photo preview
 */

public class FragPreviewPhoto extends BaseLazyFragment {
    private FrameLayout rzFrameLayout;
    private ProgressBar rzProgressBar;
    private RZPhotoNumberView rzNumberView;

    private static final String FRAG_ALBUM_PHOTO = "FRAG_ALBUM_PHOTO";
    private static final String FRAG_ALBUM_PHOTOS = "FRAG_ALBUM_PHOTOS";
    private static final String FRAG_ALBUM_LIMIT_COUNT = "FRAG_ALBUM_LIMIT_COUNT";
    private static final int GIF_LOOP_COUNT = 100;

    private AlbumPhoto photo;
    private List<AlbumPhoto> photoList;
    private int limitCount;
    private OnNumberViewClickListener listener;

    public static FragPreviewPhoto instance(AlbumPhoto photo, ArrayList<AlbumPhoto> photoList, int limitCount) {
        FragPreviewPhoto fragPreviewPhoto = new FragPreviewPhoto();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FRAG_ALBUM_PHOTO, photo);
        bundle.putParcelableArrayList(FRAG_ALBUM_PHOTOS, photoList);
        bundle.putInt(FRAG_ALBUM_LIMIT_COUNT, limitCount);
        fragPreviewPhoto.setArguments(bundle);
        return fragPreviewPhoto;
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview_photo, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        photoList = getArguments().getParcelableArrayList(FRAG_ALBUM_PHOTOS);
        AlbumPhoto photo = getArguments().getParcelable(FRAG_ALBUM_PHOTO);
        if (photo != null) this.photo = photo;
        limitCount = getArguments().getInt(FRAG_ALBUM_LIMIT_COUNT);
        rzFrameLayout = (FrameLayout) view.findViewById(R.id.rzFrameLayout);
        rzProgressBar = (ProgressBar) view.findViewById(R.id.rzProgressBar);
        rzNumberView = (RZPhotoNumberView) view.findViewById(R.id.rzNumberView);
        isPrepared = true;
    }

    @Override
    protected void loadData() {
        if (!isVisiable || !isPrepared || isLoadData) {
            if (isLoadData) {
                // update number
                rzNumberView.setNumber(photo.getPickNumber());
                rzNumberView.setPickColor(photo.getPickColor());
            }
            return;
        }

        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        String photoPath = photo.getPhotoPath();
        if (photoPath.endsWith(".gif") || photoPath.endsWith(".GIF")) {
            ImageView mImgView = new ImageView(getContext());
            mImgView.setLayoutParams(lp);
            Glide.with(getActivity())
                    .load(photoPath)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new GlideDrawableImageViewTarget(mImgView, GIF_LOOP_COUNT));
            rzFrameLayout.addView(mImgView, 0);
        } else {
            RZZoomImgView mZoomView = new RZZoomImgView(getContext());
            mZoomView.setLayoutParams(lp);
            Glide.with(getActivity())
                    .load(photoPath)
                    .asBitmap()
                    .into(mZoomView);
            rzFrameLayout.addView(mZoomView, 0);
        }
        rzNumberView.setNumber(photo.getPickNumber());
        rzNumberView.setPickColor(photo.getPickColor());

        rzNumberView.setOnClickListener(new View.OnClickListener() {
            boolean isAdd = true;

            @Override
            public void onClick(View v) {
                if (photo.getPickNumber() == 0) {
                    if (photoList.size() == limitCount) {
                        Toast.makeText(getActivity(), String.format(Locale.TAIWAN, "最多選擇%d張", limitCount),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    photo.setPickNumber(photoList.size() + 1);
                    photoList.add(photo);
                } else {
                    if (photoList.size() == 0) return;
                    photoList.remove(photo);
                    photo.setPickNumber(0);
                    isAdd = false;
                }
                rzNumberView.setNumber(photo.getPickNumber());
                rzNumberView.setPickColor(photo.getPickColor());
                if (listener != null) listener.onNumberViewClick(v, photo, isAdd);

            }
        });
        rzProgressBar.setVisibility(View.GONE);
        isLoadData = true;
    }

    @Override
    protected void unLoadData() {

    }

    public interface OnNumberViewClickListener {
        void onNumberViewClick(View view, AlbumPhoto photo, boolean isAdd);
    }

    public void setOnNumberViewClickListener(OnNumberViewClickListener listener) {
        this.listener = listener;
    }
}
