package com.rayzhang.android.rayzhangalbum;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;

import java.util.List;
import java.util.Locale;

public class DemoAdapter extends RecyclerView.Adapter {
    private List<AlbumPhoto> list;

    DemoAdapter(List<AlbumPhoto> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_linear_demo_item, parent, false);
        return new DemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DemoViewHolder) {
            ((DemoViewHolder) holder).setData(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void resetData(List<AlbumPhoto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private class DemoViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgView;
        private TextView mDescriptionText;

        DemoViewHolder(View itemView) {
            super(itemView);

            mImgView = itemView.findViewById(R.id.mImgView);
            mDescriptionText = itemView.findViewById(R.id.mDescriptionText);
        }

        public void setData(AlbumPhoto photo) {
            Glide.with(itemView.getContext())
                    .asBitmap()
                    .apply(new RequestOptions().circleCrop())
                    .load(photo.getPhotoPath())
                    .into(mImgView);

            String description = String.format(Locale.TAIWAN, "%s - %d x %d\n%s",
                    photo.getPhotoName(), photo.getPhotoWidth(), photo.getPhotoHeight(), photo.getPhotoPath() );
            mDescriptionText.setText(description);
        }
    }
}
