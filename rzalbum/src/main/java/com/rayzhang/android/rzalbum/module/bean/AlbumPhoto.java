package com.rayzhang.android.rzalbum.module.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ray on 2016/12/26.
 */

public class AlbumPhoto implements Parcelable, Comparable<AlbumPhoto> {
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

    public AlbumPhoto() {
    }

    public AlbumPhoto(int photoID, String photoPath, String photoName, long photoAddTime, boolean isCheck, int photoIndex) {
        this.photoID = photoID;
        this.photoPath = photoPath;
        this.photoName = photoName;
        this.photoAddTime = photoAddTime;
        this.isCheck = isCheck;
        this.photoIndex = photoIndex;
    }

    protected AlbumPhoto(Parcel in) {
        photoID = in.readInt();
        photoPath = in.readString();
        photoName = in.readString();
        photoAddTime = in.readLong();
        isCheck = in.readByte() != 0;
        photoIndex = in.readInt();
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

    @Override
    public String toString() {
        return "AlbumPhoto{" +
                "photoID=" + photoID +
                ", photoPath='" + photoPath + '\'' +
                ", photoName='" + photoName + '\'' +
                ", photoAddTime=" + photoAddTime +
                ", isCheck=" + isCheck +
                ", photoIndex=" + photoIndex +
                '}';
    }

    @Override
    public int compareTo(AlbumPhoto other) {
        return this.getPhotoIndex() - other.getPhotoIndex();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(photoID);
        parcel.writeString(photoPath);
        parcel.writeString(photoName);
        parcel.writeLong(photoAddTime);
        parcel.writeByte((byte) (isCheck ? 1 : 0));
        parcel.writeInt(photoIndex);
    }
}
