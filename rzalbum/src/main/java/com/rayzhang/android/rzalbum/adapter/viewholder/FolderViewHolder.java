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
import com.rayzhang.android.rzalbum.model.AlbumFolder;

/**
 * Created by Ray on 2017/8/19.
 * FolderViewHolder
 */

public class FolderViewHolder extends BaseViewHolder {
    private RelativeLayout rzFolderViewHolder;
    private ImageView rzImgView;
    private TextView rzTextFolder;
    private RadioButton rzRadioBut;

    public FolderViewHolder(View itemView) {
        super(itemView);

        rzFolderViewHolder = (RelativeLayout) getView(R.id.rzFolderViewHolder);
        rzImgView = (ImageView) getView(R.id.rzImgView);
        rzTextFolder = (TextView) getView(R.id.rzTextFolder);
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
    public void bindViewData(Context context, Object data, int itemPosition) {
        AlbumFolder folder = (AlbumFolder) data;
        Glide.with(context)
                .load(folder.getFolderPhotos().get(0).getPhotoPath())
                .asBitmap()
                .placeholder(R.drawable.ic_place_img_50dp)
                .error(R.drawable.ic_place_img_50dp)
                .into(rzImgView);

        int count = folder.getFolderPhotos().size();
        rzTextFolder.setText(folder.getFolderName() + " (" + count + ") ");
        rzRadioBut.setChecked(folder.isCheck());
        rzRadioBut.setClickable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int[] colors = new int[]{folder.getPickColor(), Color.argb(255, 77, 77, 77)};
            int[][] states = new int[2][];
            states[0] = new int[]{android.R.attr.state_checked};
            states[1] = new int[]{};
            ColorStateList colorStateList = new ColorStateList(states, colors);
            rzRadioBut.setButtonTintList(colorStateList);
        }
    }
}
