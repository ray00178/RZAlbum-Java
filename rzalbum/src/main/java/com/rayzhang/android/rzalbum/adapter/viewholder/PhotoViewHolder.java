package com.rayzhang.android.rzalbum.adapter.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

public class PhotoViewHolder extends BaseViewHolder<AlbumPhoto> {
    private ImageView rzPhotoImg;
    private TextView rzGifText;
    private RZPhotoBorderView rzBorderView;
    private RZPhotoNumberView rzNumberView;

    private PhotoViewHolder(View itemView) {
        super(itemView);
    }

    public PhotoViewHolder(int wh, View itemView) {
        super(itemView);

        rzPhotoImg = (ImageView) getView(R.id.rzPhotoImg);
        rzGifText = (TextView) getView(R.id.rzGifText);
        rzBorderView = (RZPhotoBorderView) getView(R.id.rzBorderView);
        rzNumberView = (RZPhotoNumberView) getView(R.id.rzNumberView);

        if (wh > 0) {
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = wh;
            lp.height = wh;
            itemView.setLayoutParams(lp);
        }
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
    public void bindViewData(Context context, AlbumPhoto data, int itemPosition) {
        rzGifText.setText(context.getResources().getString(R.string.rz_album_gif));

//        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.ic_place_img_50dp)
//                .error(R.drawable.ic_place_img_50dp);
        Glide.with(context)
                .asBitmap()
                .load(data.getPhotoPath())
                //.apply(options)
                .into(rzPhotoImg);

        if (data.getPhotoPath().endsWith(".gif") || data.getPhotoPath().endsWith(".GIF")) {
            float radius = rzGifText.getWidth() / 2;
            rzGifText.setBackground(DrawableUtils.drawableOfRoundRect(Color.argb(200, 255, 255, 255), radius));
            rzGifText.setVisibility(View.VISIBLE);
        } else {
            rzGifText.setVisibility(View.INVISIBLE);
        }
        rzBorderView.setDraw(data.getPickNumber() > 0, data.getPickColor());
        rzNumberView.setNumber(data.getPickNumber());
        rzNumberView.setPickColor(data.getPickColor());
    }
}
