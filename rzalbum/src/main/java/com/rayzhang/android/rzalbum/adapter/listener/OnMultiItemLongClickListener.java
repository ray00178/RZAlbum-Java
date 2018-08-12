package com.rayzhang.android.rzalbum.adapter.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface OnMultiItemLongClickListener {
    void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int viewPosition, int itemPosition);
}
