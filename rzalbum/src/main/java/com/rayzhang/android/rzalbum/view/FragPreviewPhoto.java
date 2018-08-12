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
import com.rayzhang.android.rzalbum.R;
import com.rayzhang.android.rzalbum.base.BaseLazyFragment;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;
import com.rayzhang.android.rzalbum.widget.RZPhotoNumberView;
import com.rayzhang.android.rzalbum.widget.RZZoomImgView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Ray on 2017/8/20.
 * Photo preview
 */

public class FragPreviewPhoto extends BaseLazyFragment {
    private static final String FRAG_ALBUM_PHOTO = "FRAG_ALBUM_PHOTO";
    private static final String FRAG_ALBUM_PHOTOS = "FRAG_ALBUM_PHOTOS";
    private static final String FRAG_ALBUM_LIMIT_COUNT = "FRAG_ALBUM_LIMIT_COUNT";

    private FrameLayout rzFrameLayout;
    private ProgressBar rzProgressBar;
    private RZPhotoNumberView rzNumberView;

    private AlbumPhoto photo;
    private ArrayList<AlbumPhoto> addPhotos;
    private int limitCount;
    private OnNumberViewClickListener listener;

    public static FragPreviewPhoto instance(AlbumPhoto photo, ArrayList<AlbumPhoto> addPhotos, int limitCount) {
        FragPreviewPhoto fragPreviewPhoto = new FragPreviewPhoto();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FRAG_ALBUM_PHOTO, photo);
        bundle.putParcelableArrayList(FRAG_ALBUM_PHOTOS, addPhotos);
        bundle.putInt(FRAG_ALBUM_LIMIT_COUNT, limitCount);
        fragPreviewPhoto.setArguments(bundle);
        return fragPreviewPhoto;
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview_photo, container, false);
        setup(view);
        return view;
    }

    private void setup(View view) {
        if (getArguments() != null) {
            photo = getArguments().getParcelable(FRAG_ALBUM_PHOTO);
            addPhotos = getArguments().getParcelableArrayList(FRAG_ALBUM_PHOTOS);
            limitCount = getArguments().getInt(FRAG_ALBUM_LIMIT_COUNT);
        }

        rzFrameLayout = view.findViewById(R.id.rzFrameLayout);
        rzProgressBar = view.findViewById(R.id.rzProgressBar);
        rzNumberView = view.findViewById(R.id.rzNumberView);

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

        if (getActivity() != null) {
            if (photoPath.endsWith(".gif") || photoPath.endsWith(".GIF")) {
                ImageView mImgView = new ImageView(getContext());
                mImgView.setLayoutParams(lp);
                Glide.with(getActivity())
                        .asGif()
                        .load(photoPath)
                        .into(mImgView);
                rzFrameLayout.addView(mImgView, 0);
            } else {
                RZZoomImgView mZoomView = new RZZoomImgView(getContext());
                mZoomView.setLayoutParams(lp);
                Glide.with(getActivity())
                        .asBitmap()
                        .load(photoPath)
                        .into(mZoomView);
                rzFrameLayout.addView(mZoomView, 0);
            }
        }
        rzNumberView.setNumber(photo.getPickNumber());
        rzNumberView.setPickColor(photo.getPickColor());

        rzNumberView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdd = true;
                if (photo.getPickNumber() == 0) {
                    if (addPhotos.size() == limitCount) {
                        Toast.makeText(getActivity(), String.format(Locale.TAIWAN,
                                getResources().getString(R.string.rz_album_limit_count), limitCount),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    photo.setPickNumber(addPhotos.size() + 1);
                    addPhotos.add(photo);
                } else {
                    if (addPhotos.size() == 0) return;
                    addPhotos.remove(photo);
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
