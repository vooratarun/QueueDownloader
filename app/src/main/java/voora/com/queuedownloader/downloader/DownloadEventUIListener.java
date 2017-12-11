package voora.com.queuedownloader.downloader;


import com.downloader.Progress;

import voora.com.queuedownloader.database.DItem;

public interface DownloadEventUIListener {

    void onDownloadProgress(DItem dItem , Progress progress);

    void onDownloadPaused(DItem dItem);

    void onDownloadStarted(DItem dItem);

    void onDownloadCancelled(DItem dItem);

    void onDownloadCompleted(DItem dItem);

}