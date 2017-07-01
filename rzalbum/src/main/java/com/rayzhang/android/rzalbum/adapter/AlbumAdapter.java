package com.rayzhang.android.rzalbum.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.rayzhang.android.rzalbum.R;
import com.rayzhang.android.rzalbum.module.bean.AlbumPhoto;
import com.rayzhang.android.rzalbum.utils.DisplayUtils;
import com.rayzhang.android.rzalbum.widget.RZCheckView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Ray on 2017/2/10.
 */

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_CAMERA = 1;
    private static final int ITEM_NORMAL = 2;
    private Context context;
    // list : 每次都會改變 ; oriList : 儲存所有的照片
    private List<AlbumPhoto> list, oriList;
    // 是否為第一次，用來防止oriList內容被改變
    private boolean isFirst = true;
    // 是否要顯示Camera function
    private boolean isShow = true;
    // 紀錄所點選到的照片
    private TreeMap<Integer, AlbumPhoto> multiPhotos;
    // 張數限制
    private int limitCount;

    public AlbumAdapter(Context context, int limitCount, boolean isShow) {
        this.context = context;
        this.limitCount = limitCount;
        this.isShow = isShow;
        DisplayUtils.initScreen(context);
        list = new ArrayList<>();
        oriList = new ArrayList<>();
        multiPhotos = new TreeMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_CAMERA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rz_album_adapter_camera_item, parent, false);
            return new PhotoCView(view);
        }
        if (viewType == ITEM_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rz_album_adapter_normal_item, parent, false);
            return new PhotoNView(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case ITEM_CAMERA:
                if (listener != null) {
                    ((PhotoCView) holder).rzAdapterCItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onCameraItemClick(view);
                        }
                    });
                }
                break;
            case ITEM_NORMAL:
                final int less = isShow ? 1 : 0;
                final AlbumPhoto photo = list.get(position - less);
                Glide.with(context)
                        .load(photo.getPhotoPath())
                        .crossFade()
                        .placeholder(R.drawable.ic_place_img_50dp)
                        .error(R.drawable.ic_place_img_50dp)
                        .into(((PhotoNView) holder).rzNImgView);
                if (photo.isCheck()) {
                    ((PhotoNView) holder).rzNClickView.setVisibility(View.VISIBLE);
                } else {
                    ((PhotoNView) holder).rzNClickView.setVisibility(View.GONE);
                }
                if (listener != null) {
                    ((PhotoNView) holder).rzAdapterNItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean isOver = false;
                            if (photo.isCheck()) {
                                // 已選中的照片
                                ((PhotoNView) holder).rzNClickView.setVisibility(View.GONE);
                                multiPhotos.remove(photo.getPhotoIndex());
                                list.get(position - less).setCheck(false);
                                isOver = false;
                            } else {
                                // 張數限制
                                if (multiPhotos.size() == limitCount) {
                                    isOver = true;
                                } else {
                                    ((PhotoNView) holder).rzNClickView.setVisibility(View.VISIBLE);
                                    multiPhotos.put(photo.getPhotoIndex(), photo);
                                    list.get(position - less).setCheck(true);
                                }
                            }
                            listener.onPhotoItemClick(isOver, multiPhotos.size());
                        }
                    });
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return isShow ? list.size() + 1 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isShow) {
            return position == 0 ? ITEM_CAMERA : ITEM_NORMAL;
        }
        return ITEM_NORMAL;
    }

    private class PhotoCView extends RecyclerView.ViewHolder {
        private RelativeLayout rzAdapterCItem;
        private ImageView rzCImgView;

        private PhotoCView(View itemView) {
            super(itemView);
            rzAdapterCItem = (RelativeLayout) itemView.findViewById(R.id.rzAdapterCItem);
            rzCImgView = (ImageView) itemView.findViewById(R.id.rzCImgView);
            rzCImgView.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        }
    }

    private class PhotoNView extends RecyclerView.ViewHolder {
        private RelativeLayout rzAdapterNItem;
        private ImageView rzNImgView;
        private RZCheckView rzNClickView;

        private PhotoNView(View itemView) {
            super(itemView);
            rzAdapterNItem = (RelativeLayout) itemView.findViewById(R.id.rzAdapterNItem);
            rzNImgView = (ImageView) itemView.findViewById(R.id.rzNImgView);
            rzNClickView = (RZCheckView) itemView.findViewById(R.id.rzNClickView);
            rzNClickView.setVisibility(View.GONE);
        }
    }

    public interface OnPhotoItemClickListener {
        void onCameraItemClick(View view);

        void onPhotoItemClick(boolean isOver, int count);
    }

    private OnPhotoItemClickListener listener;

    public void setOnPhotoItemClick(OnPhotoItemClickListener listener) {
        this.listener = listener;
    }

    public void addDatas(List<AlbumPhoto> photos) {
        if (isFirst) {
            oriList.addAll(photos);
            isFirst = false;
        }
        if (list.size() > 0) list.clear();
        list.addAll(photos);
        notifyDataSetChanged();
    }

    public void refreshDatas(boolean isAdd, int photoIndex, List<AlbumPhoto> photos) {
        //Log.d("RZ", "Photo mutilPhotos Be:" + mutilPhotos.size());
        if (isAdd) {
            // 如果是true就新增，反之刪除
            multiPhotos.put(oriList.get(photoIndex).getPhotoIndex(), oriList.get(photoIndex));
        } else {
            multiPhotos.remove(photoIndex);
        }
        //Log.d("RZ", "Photo mutilPhotos Af:" + mutilPhotos.size());
        list.clear();
        list.addAll(photos);
        notifyItemChanged(photoIndex);
    }

    public ArrayList<AlbumPhoto> getPhotos() {
        ArrayList<AlbumPhoto> list = new ArrayList<>();
        list.addAll(multiPhotos.values());
        return list;
    }
}
