package com.rayzhang.android.rzalbum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rayzhang.android.rzalbum.R;
import com.rayzhang.android.rzalbum.module.bean.AlbumFolder;

import java.util.List;

/**
 * Created by Ray on 2017/2/10.
 */

/**
 * 選擇相簿
 */
public class SelectAlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_NORMAL = 1;
    private Context context;
    private List<AlbumFolder> list;

    public SelectAlbumAdapter(Context context, List<AlbumFolder> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rz_sel_album_adapter_item, null);
            return new SelAlbumNView(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case ITEM_NORMAL:
                AlbumFolder folder = list.get(position);
                Glide.with(context)
                        .load(folder.getFolderPhotos().get(0).getPhotoPath())
                        .placeholder(R.drawable.ic_place_img_50dp)
                        .error(R.drawable.ic_place_img_50dp)
                        .into(((SelAlbumNView) holder).mImgView);
                int count = folder.getFolderPhotos().size();
                ((SelAlbumNView) holder).mTextFolder.setText(folder.getFolderName() + " (" + count + ") ");
                ((SelAlbumNView) holder).mRadioBut.setChecked(folder.isCheck());
                ((SelAlbumNView) holder).mRadioBut.setClickable(false);
                if (listener != null) {
                    ((SelAlbumNView) holder).rzSelAdapterItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onSelAlbumItemClick(position);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_NORMAL;
    }

    public class SelAlbumNView extends RecyclerView.ViewHolder {
        private RelativeLayout rzSelAdapterItem;
        private ImageView mImgView;
        private TextView mTextFolder;
        private RadioButton mRadioBut;

        public SelAlbumNView(View itemView) {
            super(itemView);

            rzSelAdapterItem = (RelativeLayout) itemView.findViewById(R.id.rzSelAdapterItem);
            mImgView = (ImageView) itemView.findViewById(R.id.mImgView);
            mTextFolder = (TextView) itemView.findViewById(R.id.mTextFolder);
            mRadioBut = (RadioButton) itemView.findViewById(R.id.mRadioBut);
        }
    }

    public interface OnSelAlbumItemClickListener {
        void onSelAlbumItemClick(int position);
    }

    private OnSelAlbumItemClickListener listener;

    public void setOnBottomDialogItemClickListener(OnSelAlbumItemClickListener listener) {
        this.listener = listener;
    }

    public void setCheckFolder(int index) {
        // 改變點選的狀態
        for (int i = 0, j = list.size(); i < j; i++) {
            list.get(i).setCheck(false);
            if (i == index) {
                list.get(i).setCheck(true);
            }
        }
        notifyDataSetChanged();
    }
}
