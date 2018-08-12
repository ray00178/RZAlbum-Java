package com.rayzhang.android.rzalbum.adapter.viewholder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rayzhang.android.rzalbum.R;
import com.rayzhang.android.rzalbum.adapter.base.BaseViewHolder;
import com.rayzhang.android.rzalbum.adapter.factory.IItemType;
import com.rayzhang.android.rzalbum.model.AlbumFolder;
import com.rayzhang.android.rzalbum.utils.DrawableUtils;

import java.util.Locale;

/**
 * Created by Ray on 2017/8/19.
 * FolderViewHolder
 */

public class FolderViewHolder extends BaseViewHolder<AlbumFolder> {
    private RelativeLayout rzFolderViewHolder;
    private ImageView rzImgView;
    private TextView rzFolderText;
    private RadioButton rzRadioBut;

    public FolderViewHolder(View itemView) {
        super(itemView);

        rzFolderViewHolder = (RelativeLayout) getView(R.id.rzFolderViewHolder);
        rzImgView = (ImageView) getView(R.id.rzImgView);
        rzFolderText = (TextView) getView(R.id.rzFolderText);
        rzRadioBut = (RadioButton) getView(R.id.rzRadioBut);
    }

    @Override
    public View[] getClickViews() {
        return new View[]{rzFolderViewHolder};
    }

    @Override
    public View[] getLongClickViews() {
        return new View[0];
    }

    @Override
    public void bindViewData(Context context, AlbumFolder data, int itemPosition) {
        Glide.with(context)
                .asBitmap()
                .load(data.getFolderPhotos().get(0).getPhotoPath())
                .into(rzImgView);

        rzFolderText.setText(String.format(Locale.TAIWAN,
                context.getResources().getString(R.string.rz_album_folder_count),
                data.getFolderName(), data.getFolderPhotos().size()));
        rzRadioBut.setChecked(data.isCheck());
        rzRadioBut.setClickable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int[] colors = new int[]{data.getPickColor(), Color.argb(255, 77, 77, 77)};
            rzRadioBut.setButtonTintList(DrawableUtils.drawableOfColorStateList(colors, android.R.attr.state_checked));
        }
    }
}
