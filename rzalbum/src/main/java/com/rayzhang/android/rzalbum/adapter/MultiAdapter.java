package com.rayzhang.android.rzalbum.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rayzhang.android.rzalbum.adapter.base.BaseViewHolder;
import com.rayzhang.android.rzalbum.adapter.factory.IItemType;
import com.rayzhang.android.rzalbum.adapter.factory.ItemTypeFactory;
import com.rayzhang.android.rzalbum.adapter.listener.OnMultiItemClickListener;
import com.rayzhang.android.rzalbum.adapter.listener.OnMultiItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MultiAdapter<T extends IItemType> extends RecyclerView.Adapter<BaseViewHolder<T>> {
    private Context mContext;
    private List<T> mData;
    private ItemTypeFactory itemTypeFactory;

    private static final int BASE_ITEM_TYPE_HEADER = 800000;
    private static final int BASE_ITEM_TYPE_FOOTER = 900000;
    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;

    private OnMultiItemClickListener clickListener;
    private OnMultiItemLongClickListener longClickListener;

    public MultiAdapter(@Nullable List<T> list) {
        mData = new ArrayList<>();
        if (list != null) mData.addAll(list);
        itemTypeFactory = ItemTypeFactory.instance(0);
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    public MultiAdapter(@Nullable List<T> list, int wh) {
        mData = new ArrayList<>();
        if (list != null) mData.addAll(list);
        itemTypeFactory = ItemTypeFactory.instance(wh);
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (mHeaderViews.get(viewType) != null) {
            return new HeaderViewHolder(mHeaderViews.get(viewType));
        } else if (mFooterViews.get(viewType) != null) {
            return new FooterViewHolder(mFooterViews.get(viewType));
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return itemTypeFactory.createViewHolder(viewType, view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder<T> holder, int position) {
        if (isHeaderViewPosition(position) || isFooterViewPosition(position)) return;

        final int finalPosition = position - getHeadersCount();
        holder.bindViewData(mContext, mData.get(finalPosition), finalPosition);
        if (clickListener != null) {
            for (int i = 0; i < holder.getClickViews().length; i++) {
                final int viewPosition = i;
                holder.getClickViews()[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onItemClick(holder, v, viewPosition, finalPosition);
                    }
                });
            }
        }
        if (longClickListener != null) {
            for (int i = 0; i < holder.getLongClickViews().length; i ++) {
                final int viewPosition = i;
                holder.getLongClickViews()[i].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        longClickListener.onItemLongClick(holder, v, viewPosition, finalPosition);
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + mData.size() + getFootersCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPosition(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPosition(position)) {
            return mFooterViews.keyAt(position - getHeadersCount() - mData.size());
        }
        return mData.get(position - getHeadersCount()).itemLayout();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (mHeaderViews.get(viewType) != null || mFooterViews.get(viewType) != null) {
                        return ((GridLayoutManager) layoutManager).getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    private class HeaderViewHolder extends BaseViewHolder {

        HeaderViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public View[] getClickViews() {
            return new View[0];
        }

        @Override
        public View[] getLongClickViews() {
            return new View[0];
        }

        @Override
        public void bindViewData(Context context, IItemType data, int itemPosition) {

        }
    }

    private class FooterViewHolder extends BaseViewHolder {

        FooterViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public View[] getClickViews() {
            return new View[0];
        }

        @Override
        public View[] getLongClickViews() {
            return new View[0];
        }

        @Override
        public void bindViewData(Context context, IItemType data, int itemPosition) {

        }
    }

    private int getHeadersCount() {
        return mHeaderViews.size();
    }

    private int getFootersCount() {
        return mFooterViews.size();
    }

    private boolean isHeaderViewPosition(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPosition(int position) {
        return position >= getHeadersCount() + mData.size();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFooterView(View view) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public void addData(T t) {
        int finalIndex = getHeadersCount() + mData.size() - 1;
        addData(finalIndex, t);
    }

    public void addData(int index, T t) {
        int finalIndex = getHeadersCount() + index - 1;
        mData.add(finalIndex, t);
        notifyItemInserted(finalIndex);
    }

    public void addDatas(@NonNull List<T> list) {
        int finalIndex = getHeadersCount() + mData.size() - 1;
        addDatas(finalIndex, list);

    }

    public void addDatas(int index, @NonNull List<T> list) {
        int finalIndex = getHeadersCount() + index - 1;
        mData.addAll(finalIndex, list);
        notifyItemInserted(finalIndex);
    }

    public void removeData(int index) {
        int finalIndex = getHeadersCount() + index - 1;
        mData.remove(finalIndex);
        notifyItemRemoved(finalIndex);
    }

    public void clear() {
        mData.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mHeaderViews.removeAtRange(0, mHeaderViews.size());
            mFooterViews.removeAtRange(0, mFooterViews.size());
        } else {
            for (int i = 0; i < mHeaderViews.size(); i++) {
                mHeaderViews.removeAt(i);
            }
            for (int i = 0; i < mFooterViews.size(); i++) {
                mFooterViews.removeAt(i);
            }
        }
        notifyDataSetChanged();
    }

    public void resetData(@NonNull List<T> list) {
        if (mData.size() > 0) mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mData;
    }

    public void setOnItemClickListener(OnMultiItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener(OnMultiItemLongClickListener listener) {
        this.longClickListener = listener;
    }
}
