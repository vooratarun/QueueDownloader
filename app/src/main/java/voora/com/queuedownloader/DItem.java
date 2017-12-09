package voora.com.queuedownloader;

/**
 * Created by tarun on 12/9/17.
 */

public class DItem {

    private int id;
    private String downloadUrl;
    private String downloadPath;
    private int downloadPercent;
    private int state;
    private String fileName;

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

    public static class Builder {

        private int id;
        private String downloadUrl;
        private String downloadPath;
        private int downloadPercent;
        private int state;
        private String fileName;

        Builder id(int id){
            this.id = id;
            return this;
        }

        Builder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        Builder downloadPath(String downloadPath) {
            this.downloadPath = downloadPath;
            return this;
        }

        Builder downloadPercent(int downloadPercent) {
            this.downloadPercent = downloadPercent;
            return this;
        }

        Builder state(int state) {
            this.state = state;
            return this;
        }

        Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        DItem build(){
            return new DItem(id,downloadUrl,downloadPath,downloadPercent,state,fileName);
        }

    }
}
