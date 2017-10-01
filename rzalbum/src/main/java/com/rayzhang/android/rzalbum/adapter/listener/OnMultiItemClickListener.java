package com.rayzhang.android.rzalbum.adapter.listener;

/**
 * Created by Ray on 2017/3/5.
 * 實作ViewHolder的點擊事件
 */

import android.view.View;

public interface OnMultiItemClickListener {
    void onMultiItemClick(View view, int viewPosition, int itemPositon);

    void onMultiItemLongClick(View view, int viewPosition, int itemPositon);
}
