package com.rayzhang.android.rzalbum.adapter;

import android.content.Context;
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

    private final int ITEM_CAMERA = 1;
    private final int ITEM_NORMAL = 2;
    private Context context;
    // list : 每次都會改變 ; oriList : 儲存所有的照片
    private List<AlbumPhoto> list, oriList;
    // 是否為第一次，用來防止oriList內容被改變
    private boolean isFirst = true;
    // 紀錄所點選到的照片
    private TreeMap<Integer, AlbumPhoto> mutilPhotos;
    // 張數限制
    private int limitCount, WH_SIZE;
    private RelativeLayout.LayoutParams itemLp;

    public AlbumAdapter(Context context, int spanCount, int limitCount) {
        this.context = context;
        this.limitCount = limitCount;
        DisplayUtils.initScreen(context);
        WH_SIZE = (DisplayUtils.screenWidth - (spanCount + 1) * 2) / spanCount;
        itemLp = new RelativeLayout.LayoutParams(WH_SIZE, WH_SIZE);
        list = new ArrayList<>();
        oriList = new ArrayList<>();
        mutilPhotos = new TreeMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_CAMERA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rz_album_adapter_camera_item, null);
            return new PhotoCView(view);
        }
        if (viewType == ITEM_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rz_album_adapter_normal_item, null);
            return new PhotoNView(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case ITEM_CAMERA:
                Glide.with(context)
                        .load(R.drawable.ic_camera_35dp)
                        .placeholder(R.drawable.ic_camera_35dp)
                        .into(((PhotoCView) holder).rzCImgView);
                if (listener != null) {
                    ((PhotoCView) holder).rzCImgView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onCameraItemClick(view);
                        }
                    });
                }
                break;
            case ITEM_NORMAL:
                final AlbumPhoto photo = list.get(position - 1);
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
                                mutilPhotos.remove(photo.getPhotoIndex());
                                list.get(position - 1).setCheck(false);
                                isOver = false;
                            } else {
                                // 張數限制
                                if (mutilPhotos.size() == limitCount) {
                                    isOver = true;
                                } else {
                                    ((PhotoNView) holder).rzNClickView.setVisibility(View.VISIBLE);
                                    mutilPhotos.put(photo.getPhotoIndex(), photo);
                                    list.get(position - 1).setCheck(true);
                                }
                            }
                            listener.onPhotoItemClick(isOver, mutilPhotos.size());
                        }
                    });
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_CAMERA : ITEM_NORMAL;
    }

    public class PhotoCView extends RecyclerView.ViewHolder {
        private ImageView rzCImgView;

        public PhotoCView(View itemView) {
            super(itemView);
            RelativeLayout rzAdapterCItem = (RelativeLayout) itemView.findViewById(R.id.rzAdapterCItem);
            rzCImgView = (ImageView) itemView.findViewById(R.id.rzCImgView);
            rzAdapterCItem.setLayoutParams(itemLp);
        }
    }

    public class PhotoNView extends RecyclerView.ViewHolder {
        private RelativeLayout rzAdapterNItem;
        private ImageView rzNImgView;
        private RZCheckView rzNClickView;

        public PhotoNView(View itemView) {
            super(itemView);
            rzAdapterNItem = (RelativeLayout) itemView.findViewById(R.id.rzAdapterNItem);
            rzNImgView = (ImageView) itemView.findViewById(R.id.rzNImgView);
            rzNClickView = (RZCheckView) itemView.findViewById(R.id.rzNClickView);
            //rzNClickView.setBackgroundColor(Color.argb(128, 0, 0, 0));
            //rzNClickView.setBackgroundColor(Color.argb(128, 255, 80, 80));
            rzNClickView.setVisibility(View.GONE);
            rzAdapterNItem.setLayoutParams(itemLp);
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
            mutilPhotos.put(oriList.get(photoIndex).getPhotoIndex(), oriList.get(photoIndex));
        } else {
            mutilPhotos.remove(photoIndex);
        }
        //Log.d("RZ", "Photo mutilPhotos Af:" + mutilPhotos.size());
        list.clear();
        list.addAll(photos);
        notifyDataSetChanged();
    }

    public ArrayList<AlbumPhoto> getPhotos() {
        ArrayList<AlbumPhoto> list = new ArrayList<>();
        list.addAll(mutilPhotos.values());
        return list;
    }
}
