package com.rayzhang.android.rzalbum.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.rayzhang.android.rzalbum.R;
import com.rayzhang.android.rzalbum.adapter.factory.IItemType;

import java.util.ArrayList;

/**
 * Created by Ray on 2016/12/26.
 * AlbumFolder
 */

public class AlbumFolder implements Parcelable, IItemType {
    public static final int FOLDER_ITEM = R.layout.rz_album_adapter_folder;
    private int folderID;
    /**
     * 資料夾名稱
     */
    private String folderName;
    /**
     * 資料夾內 所有的照片
     */
    private ArrayList<AlbumPhoto> folderPhotos = new ArrayList<>();
    /**
     * 資料夾是否被選中
     */
    private boolean isCheck;
    /**
     * radioButton color
     */
    private int pickColor;

    public AlbumFolder() {
    }

    protected AlbumFolder(Parcel in) {
        folderID = in.readInt();
        folderName = in.readString();
        folderPhotos = in.createTypedArrayList(AlbumPhoto.CREATOR);
        isCheck = in.readByte() != 0;
        pickColor = in.readInt();
    }

    public static final Creator<AlbumFolder> CREATOR = new Creator<AlbumFolder>() {
        @Override
        public AlbumFolder createFromParcel(Parcel in) {
            return new AlbumFolder(in);
        }

        @Override
        public AlbumFolder[] newArray(int size) {
            return new AlbumFolder[size];
        }
    };

    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<AlbumPhoto> getFolderPhotos() {
        return folderPhotos;
    }

    public void addPhoto(AlbumPhoto photo) {
        this.folderPhotos.add(photo);
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getPickColor() {
        return pickColor;
    }

    public void setPickColor(int pickColor) {
        this.pickColor = pickColor;
    }

    @Override
    public String toString() {
        return "AlbumFolder{" +
                "folderID=" + folderID +
                ", folderName='" + folderName + '\'' +
                ", folderPhotos=" + folderPhotos +
                ", isCheck=" + isCheck +
                ", pickColor=" + pickColor +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(folderID);
        dest.writeString(folderName);
        dest.writeTypedList(folderPhotos);
        dest.writeByte((byte) (isCheck ? 1 : 0));
        dest.writeInt(pickColor);
    }

    @Override
    public int itemType() {
        return FOLDER_ITEM;
    }
}
