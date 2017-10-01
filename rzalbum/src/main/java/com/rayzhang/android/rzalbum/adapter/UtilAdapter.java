package com.rayzhang.android.rzalbum.adapter;

import android.content.Context;

import com.rayzhang.android.rzalbum.adapter.base.BaseAdapter;
import com.rayzhang.android.rzalbum.adapter.factory.IItemType;

import java.util.List;

/**
 * Created by Ray on 2017/8/19.
 * 通用RecycleView Adapter
 */

public class UtilAdapter extends BaseAdapter {

    public UtilAdapter(Context mContext, List<? extends IItemType> mData) {
        super(mContext, mData);
    }

    @Override
    public int itemHeadViewType() {
        return 0;
    }

    @Override
    public int itemFootViewType() {
        return 0;
    }
}
