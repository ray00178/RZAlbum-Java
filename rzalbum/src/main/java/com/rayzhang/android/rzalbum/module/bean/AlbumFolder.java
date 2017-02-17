package com.rayzhang.android.rzalbum.module.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Ray on 2016/12/26.
 */

public class AlbumFolder implements Parcelable {

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

    public AlbumFolder() {
    }

    protected AlbumFolder(Parcel in) {
        folderID = in.readInt();
        folderName = in.readString();
        folderPhotos = in.createTypedArrayList(AlbumPhoto.CREATOR);
        isCheck = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(folderID);
        parcel.writeString(folderName);
        parcel.writeTypedList(folderPhotos);
        parcel.writeByte((byte) (isCheck ? 1 : 0));
    }

    @Override
    public String toString() {
        return "AlbumFolder{" +
                "folderID=" + folderID +
                ", folderName='" + folderName + '\'' +
                ", folderPhotos=" + folderPhotos +
                ", isCheck=" + isCheck +
                '}';
    }
}
