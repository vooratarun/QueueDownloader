package voora.com.queuedownloader.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import voora.com.queuedownloader.downloader.DownloadState;

/**
 * Created by tarun on 12/9/17.
 */

@Entity(tableName = "ditem")
public class DItem implements Parcelable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "downloadUrl")
    private String downloadUrl;

    @ColumnInfo(name = "downloadPath")
    private String downloadPath;

    @ColumnInfo(name = "downloadPercent")
    private int downloadPercent;

    @ColumnInfo(name = "state")
    private int state;

    @ColumnInfo(name = "fileName")
    private String fileName;

    @ColumnInfo(name = "downloadId")
    private int downloadId = -1;

    public DItem(int id, String downloadUrl, String downloadPath, int downloadPercent , int state,
                 String fileName) {
        this.id = id;
        this.downloadPath = downloadPath;
        this.downloadUrl = downloadUrl;
        this.downloadPercent = downloadPercent;
        this.state = state;
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public int getDownloadPercent() {
        return downloadPercent;
    }

    public void setDownloadPercent(int downloadPercent) {
        this.downloadPercent = downloadPercent;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }


    public static class Builder {

        private int id = -1;
        private String downloadUrl = "";
        private String downloadPath = "";
        private int downloadPercent = 0;
        private int state = DownloadState.NORMAL.ordinal();
        private String fileName = "";

        public Builder id(int id){
            this.id = id;
            return this;
        }

        public Builder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public Builder downloadPath(String downloadPath) {
            this.downloadPath = downloadPath;
            return this;
        }

        public Builder downloadPercent(int downloadPercent) {
            this.downloadPercent = downloadPercent;
            return this;
        }

        public Builder state(int state) {
            this.state = state;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public DItem build(){
            return new DItem(id,downloadUrl,downloadPath,downloadPercent,state,fileName);
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.downloadPath);
        dest.writeInt(this.downloadPercent);
        dest.writeInt(this.state);
        dest.writeString(this.fileName);
    }

    protected DItem(Parcel in) {
        this.id = in.readInt();
        this.downloadUrl = in.readString();
        this.downloadPath = in.readString();
        this.downloadPercent = in.readInt();
        this.state = in.readInt();
        this.fileName = in.readString();
    }

    public static final Parcelable.Creator<DItem> CREATOR = new Parcelable.Creator<DItem>() {
        @Override
        public DItem createFromParcel(Parcel source) {
            return new DItem(source);
        }

        @Override
        public DItem[] newArray(int size) {
            return new DItem[size];
        }
    };
}
