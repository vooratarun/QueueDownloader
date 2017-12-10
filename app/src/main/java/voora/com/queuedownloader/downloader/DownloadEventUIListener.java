package voora.com.queuedownloader.downloader;


import voora.com.queuedownloader.database.DItem;

public interface DownloadEventUIListener {

    void onDownloadProgress(DItem dItem , long progress);

    void onDownloadPaused(DItem dItem);

    void onDownloadStarted(DItem dItem);

    void onDownloadCancelled(DItem dItem);

    void onDownloadCompleted(DItem dItem);

}