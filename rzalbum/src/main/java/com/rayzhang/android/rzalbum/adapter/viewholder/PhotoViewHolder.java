package com.rayzhang.android.rzalbum.adapter.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rayzhang.android.rzalbum.R;
import com.rayzhang.android.rzalbum.adapter.base.BaseViewHolder;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;
import com.rayzhang.android.rzalbum.utils.DrawableUtils;
import com.rayzhang.android.rzalbum.widget.RZPhotoBorderView;
import com.rayzhang.android.rzalbum.widget.RZPhotoNumberView;

/**
 * Created by Ray on 2017/8/19.
 * PhotoViewHolder
 */

public class PhotoViewHolder extends BaseViewHolder {
    private ImageView rzImgPhoto;
    private TextView rzTextGif;
    private RZPhotoBorderView rzBorderView;
    private RZPhotoNumberView rzNumberView;

    public PhotoViewHolder(View itemView) {
        super(itemView);

        rzImgPhoto = (ImageView) getView(R.id.rzImgPhoto);
        rzTextGif = (TextView) getView(R.id.rzTextGif);
        rzBorderView = (RZPhotoBorderView) getView(R.id.rzBorderView);
        rzNumberView = (RZPhotoNumberView) getView(R.id.rzNumberView);
    }

    @Override
    public View[] getClickViews() {
        return new View[]{rzBorderView, rzNumberView};
    }

    @Override
    public View[] getLongClickViews() {
        return new View[0];
    }

    @Override
    public void bindViewData(Context context, Object data, int itemPosition) {
        AlbumPhoto photo = (AlbumPhoto) data;
        Glide.with(context)
                .load(photo.getPhotoPath())
                .asBitmap()
                .placeholder(R.drawable.ic_place_img_50dp)
                .error(R.drawable.ic_place_img_50dp)
                .into(rzImgPhoto);

        rzTextGif.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
        rzTextGif.setText("GIF");
        if (photo.getPhotoPath().endsWith(".gif") || photo.getPhotoPath().endsWith(".GIF")) {
            rzTextGif.setBackground(DrawableUtils.backgroundDrawable(Color.argb(200, 255, 255, 255)));
            rzTextGif.setVisibility(View.VISIBLE);
        } else {
            rzTextGif.setVisibility(View.INVISIBLE);
        }
        rzBorderView.setDraw(photo.getPickNumber() > 0, photo.getPickColor());
        rzNumberView.setNumber(photo.getPickNumber());
        rzNumberView.setPickColor(photo.getPickColor());
    }
}
