package com.rayzhang.android.rzalbum.adapter.factory;

import android.view.View;

import com.rayzhang.android.rzalbum.adapter.base.BaseViewHolder;
import com.rayzhang.android.rzalbum.adapter.viewholder.FolderViewHolder;
import com.rayzhang.android.rzalbum.adapter.viewholder.PhotoViewHolder;
import com.rayzhang.android.rzalbum.model.AlbumFolder;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;

/**
 * Created by Ray on 2017/3/5.
 * 統一管理所有ViewHolder的佈局
 */

public class ItemTypeFactory {
    private static volatile ItemTypeFactory itemTypeFactory;
    private int wh;
    private static final int PHOTO_ITEM = AlbumPhoto.PHOTO_ITEM;
    private static final int FOLDER_ITEM = AlbumFolder.FOLDER_ITEM;

    public static ItemTypeFactory instance(int wh) {
        if (itemTypeFactory == null) itemTypeFactory = new ItemTypeFactory(wh);
        return itemTypeFactory;
    }

    private ItemTypeFactory(int wh) {
        this.wh = wh;
    }

    @SuppressWarnings("unchecked")
    public <T extends IItemType> BaseViewHolder<T> createViewHolder(int type, View itemView) {
        if (type == PHOTO_ITEM) {
            return (BaseViewHolder<T>) new PhotoViewHolder(wh, itemView);
        } else if (type == FOLDER_ITEM) {
            return (BaseViewHolder<T>) new FolderViewHolder(itemView);
        } else {
            return null;
        }
    }
}
