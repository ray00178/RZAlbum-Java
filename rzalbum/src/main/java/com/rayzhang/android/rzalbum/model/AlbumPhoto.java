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
    public static final int PHOTO_ITEM = R.layout.rz_album_adapter_grid_photo_item;

    private String bucketName;
    private int photoId;
    private String photoDesc;
    private double photoLat;
    private double photoLng;
    private int photoOrientation;
    private long photoDateAdded;
    private long photoDateModified;
    private String photoName;
    private int photoWidth;
    private int photoHeight;
    private long photoSize;
    private String photoMimeType;
    private boolean photoIsPrivate;
    private String photoPath;
    private boolean isPick;
    private int pickNumber;
    private int pickColor;

    public AlbumPhoto(String bucketName, int photoId, String photoDesc, double photoLat, double photoLng,
                      int photoOrientation, long photoDateAdded, long photoDateModified, String photoName,
                      int photoWidth, int photoHeight, long photoSize, String photoMimeType,
                      boolean photoIsPrivate, String photoPath, boolean isPick, int pickNumber, int pickColor) {
        this.bucketName = bucketName;
        this.photoId = photoId;
        this.photoDesc = photoDesc;
        this.photoLat = photoLat;
        this.photoLng = photoLng;
        this.photoOrientation = photoOrientation;
        this.photoDateAdded = photoDateAdded;
        this.photoDateModified = photoDateModified;
        this.photoName = photoName;
        this.photoWidth = photoWidth;
        this.photoHeight = photoHeight;
        this.photoSize = photoSize;
        this.photoMimeType = photoMimeType;
        this.photoIsPrivate = photoIsPrivate;
        this.photoPath = photoPath;
        this.isPick = isPick;
        this.pickNumber = pickNumber;
        this.pickColor = pickColor;
    }

    protected AlbumPhoto(Parcel in) {
        bucketName = in.readString();
        photoId = in.readInt();
        photoDesc = in.readString();
        photoLat = in.readDouble();
        photoLng = in.readDouble();
        photoOrientation = in.readInt();
        photoDateAdded = in.readLong();
        photoDateModified = in.readLong();
        photoName = in.readString();
        photoWidth = in.readInt();
        photoHeight = in.readInt();
        photoSize = in.readLong();
        photoMimeType = in.readString();
        photoIsPrivate = in.readByte() != 0;
        photoPath = in.readString();
        isPick = in.readByte() != 0;
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

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getPhotoDesc() {
        return photoDesc;
    }

    public void setPhotoDesc(String photoDesc) {
        this.photoDesc = photoDesc;
    }

    public double getPhotoLat() {
        return photoLat;
    }

    public void setPhotoLat(double photoLat) {
        this.photoLat = photoLat;
    }

    public double getPhotoLng() {
        return photoLng;
    }

    public void setPhotoLng(double photoLng) {
        this.photoLng = photoLng;
    }

    public int getPhotoOrientation() {
        return photoOrientation;
    }

    public void setPhotoOrientation(int photoOrientation) {
        this.photoOrientation = photoOrientation;
    }

    public long getPhotoDateAdded() {
        return photoDateAdded;
    }

    public void setPhotoDateAdded(long photoDateAdded) {
        this.photoDateAdded = photoDateAdded;
    }

    public long getPhotoDateModified() {
        return photoDateModified;
    }

    public void setPhotoDateModified(long photoDateModified) {
        this.photoDateModified = photoDateModified;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public int getPhotoWidth() {
        return photoWidth;
    }

    public void setPhotoWidth(int photoWidth) {
        this.photoWidth = photoWidth;
    }

    public int getPhotoHeight() {
        return photoHeight;
    }

    public void setPhotoHeight(int photoHeight) {
        this.photoHeight = photoHeight;
    }

    public long getPhotoSize() {
        return photoSize;
    }

    public void setPhotoSize(long photoSize) {
        this.photoSize = photoSize;
    }

    public String getPhotoMimeType() {
        return photoMimeType;
    }

    public void setPhotoMimeType(String photoMimeType) {
        this.photoMimeType = photoMimeType;
    }

    public boolean isPhotoIsPrivate() {
        return photoIsPrivate;
    }

    public void setPhotoIsPrivate(boolean photoIsPrivate) {
        this.photoIsPrivate = photoIsPrivate;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public boolean isPick() {
        return isPick;
    }

    public void setPick(boolean pick) {
        isPick = pick;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bucketName);
        dest.writeInt(photoId);
        dest.writeString(photoDesc);
        dest.writeDouble(photoLat);
        dest.writeDouble(photoLng);
        dest.writeInt(photoOrientation);
        dest.writeLong(photoDateAdded);
        dest.writeLong(photoDateModified);
        dest.writeString(photoName);
        dest.writeInt(photoWidth);
        dest.writeInt(photoHeight);
        dest.writeLong(photoSize);
        dest.writeString(photoMimeType);
        dest.writeByte((byte) (photoIsPrivate ? 1 : 0));
        dest.writeString(photoPath);
        dest.writeByte((byte) (isPick ? 1 : 0));
        dest.writeInt(pickNumber);
        dest.writeInt(pickColor);
    }

    @Override
    public int compareTo(@NonNull AlbumPhoto other) {
        return other.getPhotoId() - this.getPhotoId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumPhoto that = (AlbumPhoto) o;

        if (photoId != that.photoId) return false;
        if (Double.compare(that.photoLat, photoLat) != 0) return false;
        if (Double.compare(that.photoLng, photoLng) != 0) return false;
        if (photoOrientation != that.photoOrientation) return false;
        if (photoDateAdded != that.photoDateAdded) return false;
        if (photoDateModified != that.photoDateModified) return false;
        if (photoWidth != that.photoWidth) return false;
        if (photoHeight != that.photoHeight) return false;
        if (photoSize != that.photoSize) return false;
        if (photoIsPrivate != that.photoIsPrivate) return false;
        if (bucketName != null ? !bucketName.equals(that.bucketName) : that.bucketName != null)
            return false;
        if (photoDesc != null ? !photoDesc.equals(that.photoDesc) : that.photoDesc != null)
            return false;
        if (photoName != null ? !photoName.equals(that.photoName) : that.photoName != null)
            return false;
        if (photoMimeType != null ? !photoMimeType.equals(that.photoMimeType) : that.photoMimeType != null)
            return false;
        return photoPath != null ? photoPath.equals(that.photoPath) : that.photoPath == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = bucketName != null ? bucketName.hashCode() : 0;
        result = 31 * result + photoId;
        result = 31 * result + (photoDesc != null ? photoDesc.hashCode() : 0);
        temp = Double.doubleToLongBits(photoLat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(photoLng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + photoOrientation;
        result = 31 * result + (int) (photoDateAdded ^ (photoDateAdded >>> 32));
        result = 31 * result + (int) (photoDateModified ^ (photoDateModified >>> 32));
        result = 31 * result + (photoName != null ? photoName.hashCode() : 0);
        result = 31 * result + photoWidth;
        result = 31 * result + photoHeight;
        result = 31 * result + (int) (photoSize ^ (photoSize >>> 32));
        result = 31 * result + (photoMimeType != null ? photoMimeType.hashCode() : 0);
        result = 31 * result + (photoIsPrivate ? 1 : 0);
        result = 31 * result + (photoPath != null ? photoPath.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AlbumPhoto{" +
                "bucketName='" + bucketName + '\'' +
                ", photoId=" + photoId +
                ", photoDesc='" + photoDesc + '\'' +
                ", photoLat=" + photoLat +
                ", photoLng=" + photoLng +
                ", photoOrientation=" + photoOrientation +
                ", photoDateAdded=" + photoDateAdded +
                ", photoDateModified=" + photoDateModified +
                ", photoName='" + photoName + '\'' +
                ", photoWidth=" + photoWidth +
                ", photoHeight=" + photoHeight +
                ", photoSize=" + photoSize +
                ", photoMimeType='" + photoMimeType + '\'' +
                ", photoIsPrivate=" + photoIsPrivate +
                ", photoPath='" + photoPath + '\'' +
                ", isPick=" + isPick +
                ", pickNumber=" + pickNumber +
                ", pickColor=" + pickColor +
                '}';
    }

    @Override
    public int itemLayout() {
        return PHOTO_ITEM;
    }
}
