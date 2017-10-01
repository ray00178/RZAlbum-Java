package com.rayzhang.android.rzalbum.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ray on 2017/3/16.
 * 懶加載
 */

public abstract class BaseLazyFragment extends Fragment {
    protected static final String TAG = BaseLazyFragment.class.getSimpleName();
    // 當前的fragment 是否可見
    protected boolean isVisiable = false;
    // 當前的fragment是否已初始化完成
    public boolean isPrepared = false;
    // 當前的fragment是否已加載數據完成
    public boolean isLoadData = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    public abstract View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisiable = true;
            onVisiable();
        } else {
            isVisiable = false;
            onInVisiable();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && isPrepared) {
            loadData();
        }
    }

    private void onInVisiable() { // 不可見時做甚麼事
        unLoadData();
    }

    private void onVisiable() {
        // 加載數據
        loadData();
    }

    protected abstract void loadData();

    protected abstract void unLoadData();
}
