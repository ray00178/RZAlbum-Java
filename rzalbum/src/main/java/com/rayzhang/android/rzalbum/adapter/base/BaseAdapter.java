package com.rayzhang.android.rzalbum.adapter.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rayzhang.android.rzalbum.adapter.factory.IItemType;
import com.rayzhang.android.rzalbum.adapter.factory.ItemTypeFactory;
import com.rayzhang.android.rzalbum.adapter.listener.OnMultiItemClickListener;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Ray on 2017/3/14.
 * BaseAdapter
 */

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private Context mContext;
    private List<IItemType> mData;
    private ItemTypeFactory itemTypeFactory;
    private OnMultiItemClickListener listener;

    public BaseAdapter(Context mContext, List<? extends IItemType> mData) {
        this.mContext = mContext;
        this.mData = new ArrayList<>();
        if (mData != null) this.mData.addAll(mData);
        itemTypeFactory = new ItemTypeFactory();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return itemTypeFactory.createViewHolder(viewType, view);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        // noinspection unchecked
        holder.bindViewData(mContext, mData.get(position), position);
        if (listener != null) {
            for (int i = 0, j = holder.getClickViews().length; i < j; i++) {
                final int viewPosition = i;
                holder.getClickViews()[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onMultiItemClick(view, viewPosition, holder.getAdapterPosition());
                    }
                });
            }
            for (int i = 0, j = holder.getLongClickViews().length; i < j; i++) {
                final int viewPosition = i;
                holder.getLongClickViews()[i].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        listener.onMultiItemLongClick(view, viewPosition, holder.getAdapterPosition());
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).itemType();
    }

    /*
     * 回傳header or footer的佈局
     *
     * @return itemType
     */
    public abstract int itemHeadViewType();

    public abstract int itemFootViewType();

    /*
     * 參考資料 : http://blog.csdn.net/qibin0506/article/details/49716795
     * 為GridLayout加上Header & Footer
     * 我們解釋一下這段代碼，首先我們設置了一個SpanSizeLookup，這個類是一個抽像類，
     * 而且僅有一個抽象方法getSpanSize，這個方法的返回值決定了我們每個position上的item佔據的單元格個數，
     * 而我們這段代碼綜合上面為GridLayoutManager設置的每行的個數來解釋的話，
     * 就是當前位置是header的位置，那麼該item佔據n(看設定決定)個單元格，正常情況下佔據1個單元格。
     * 那這段代碼放哪呢？為了以後的封裝，我們還是在Adapter中找方法放吧。
     * 我們在Adapter中再重寫一個方法onAttachedToRecyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemType = getItemViewType(position);
                    if (itemType == itemHeadViewType() || itemType == itemFootViewType()) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    /*
     * 為StaggeredLayoutManager添加header
     */
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == 0) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public void setOnMultiItemClick(OnMultiItemClickListener listener) {
        this.listener = listener;
    }

    public void addData(IItemType typeVH) {
        mData.add(typeVH);
        // getItemCount : 取得所有item總數，從1開始計算
        notifyItemInserted(getItemCount());
    }

    public void addData(int index, IItemType typeVH) {
        mData.add(index, typeVH);
        notifyItemInserted(index);
    }

    public void addDatas(List<? extends IItemType> list) {
        if (list == null || list.size() == 0) return;
        mData.addAll(list);
        notifyItemInserted(getItemCount());
    }

    public void addDatas(int index, List<? extends IItemType> list) {
        if (list == null || list.size() == 0) return;
        mData.addAll(index, list);
        notifyItemInserted(index);
    }

    public void removeData(int index) {
        mData.remove(index);
        notifyItemRemoved(index);
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void resetDatas(List<? extends IItemType> list) {
        mData.clear();
        mData.addAll(0, list);
        notifyDataSetChanged();
    }

    public void addHeadView(IItemType typeVH) {
        mData.add(0, typeVH);
        notifyItemInserted(0);
    }

    /*
     * @param index  自定義位置
     * @param typeVH typeVH
     */
    public void addHeadView(int index, IItemType typeVH) {
        mData.add(index, typeVH);
        notifyItemInserted(index);
    }

    public void addFootView(IItemType typeVH) {
        mData.add(getItemCount(), typeVH);
        notifyItemInserted(getItemCount());
    }

    public List<? extends IItemType> getListData() {
        return mData;
    }
}
