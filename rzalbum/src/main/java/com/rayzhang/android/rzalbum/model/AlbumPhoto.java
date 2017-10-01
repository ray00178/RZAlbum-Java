package com.rayzhang.android.rzalbum.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.rayzhang.android.rzalbum.R;
import com.rayzhang.android.rzalbum.adapter.factory.IItemType;

/**
 * Created by Ray on 2016/12/26.
 * Photo data
 */

public class AlbumPhoto implements Parcelable, Comparable<AlbumPhoto>, IItemType {
    public static final int PHOTO_ITEM = R.layout.rz_album_adapter_photo;
    /**
     * 圖片ID
     */
    private int photoID;
    /**
     * 圖片路徑
     */
    private String photoPath;
    /**
     * 圖片名稱
     */
    private String photoName;
    /**
     * 拍照時間
     */
    private long photoAddTime;
    /**
     * 是否被選中
     */
    private boolean isCheck;
    /**
     * 照片的索引
     */
    private int photoIndex;
    /**
     * 選擇時的number
     */
    private int pickNumber;
    /**
     * 選擇時的color
     */
    private int pickColor;

    public AlbumPhoto() {
    }

    public AlbumPhoto(int photoID, String photoPath, String photoName, long photoAddTime,
                      boolean isCheck, int photoIndex, int pickNumber, int pickColor) {
        this.photoID = photoID;
        this.photoPath = photoPath;
        this.photoName = photoName;
        this.photoAddTime = photoAddTime;
        this.isCheck = isCheck;
        this.photoIndex = photoIndex;
        this.pickNumber = pickNumber;
        this.pickColor = pickColor;
    }

    protected AlbumPhoto(Parcel in) {
        photoID = in.readInt();
        photoPath = in.readString();
        photoName = in.readString();
        photoAddTime = in.readLong();
        isCheck = in.readByte() != 0;
        photoIndex = in.readInt();
        pickNumber = in.readInt();
        pickColor = in.readInt();
    }

    public static final Creator<AlbumPhoto> CREATOR = new Creator<AlbumPhoto>() {
        @Override
        public AlbumPhoto createFromParcel(Parcel in) {
            return new AlbumPhoto(in);
        }

        @Override
        public AlbumPhoto[] newArray(int size) {
            return new AlbumPhoto[size];
        }
    };

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public long getPhotoAddTime() {
        return photoAddTime;
    }

    public void setPhotoAddTime(long photoAddTime) {
        this.photoAddTime = photoAddTime;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getPhotoIndex() {
        return photoIndex;
    }

    public void setPhotoIndex(int photoIndex) {
        this.photoIndex = photoIndex;
    }

    public int getPickNumber() {
        return pickNumber;
    }

    public void setPickNumber(int pickNumber) {
        this.pickNumber = pickNumber;
    }

    public int getPickColor() {
        return pickColor;
    }

    public void setPickColor(int pickColor) {
        this.pickColor = pickColor;
    }

    @Override
    public String toString() {
        return "AlbumPhoto{" +
                "photoID=" + photoID +
                ", photoPath='" + photoPath + '\'' +
                ", photoName='" + photoName + '\'' +
                ", photoAddTime=" + photoAddTime +
                ", isCheck=" + isCheck +
                ", photoIndex=" + photoIndex +
                ", pickNumber=" + pickNumber +
                ", pickColor=" + pickColor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlbumPhoto)) return false;

        AlbumPhoto photo = (AlbumPhoto) o;

        if (getPhotoID() != photo.getPhotoID()) return false;
        if (getPhotoAddTime() != photo.getPhotoAddTime()) return false;
        if (getPhotoIndex() != photo.getPhotoIndex()) return false;
        if (getPhotoPath() != null ? !getPhotoPath().equals(photo.getPhotoPath()) : photo.getPhotoPath() != null)
            return false;
        return getPhotoName() != null ? getPhotoName().equals(photo.getPhotoName()) : photo.getPhotoName() == null;

    }

    @Override
    public int hashCode() {
        int result = getPhotoID();
        result = 31 * result + (getPhotoPath() != null ? getPhotoPath().hashCode() : 0);
        result = 31 * result + (getPhotoName() != null ? getPhotoName().hashCode() : 0);
        result = 31 * result + (int) (getPhotoAddTime() ^ (getPhotoAddTime() >>> 32));
        result = 31 * result + getPhotoIndex();
        return result;
    }

    @Override
    public int compareTo(@NonNull AlbumPhoto other) {
        return other.getPhotoID() - this.getPhotoID();
    }


    @Override
    public int itemType() {
        return PHOTO_ITEM;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(photoID);
        dest.writeString(photoPath);
        dest.writeString(photoName);
        dest.writeLong(photoAddTime);
        dest.writeByte((byte) (isCheck ? 1 : 0));
        dest.writeInt(photoIndex);
        dest.writeInt(pickNumber);
        dest.writeInt(pickColor);
    }
}
