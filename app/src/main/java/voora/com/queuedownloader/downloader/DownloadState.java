package voora.com.queuedownloader.downloader;

/**
 * Created by tarun on 12/10/17.
 */

public enum DownloadState {

    NORMAL,
    DOWNLOADING,
    QUEUED,
    PAUSED,
    CANCEL,
    UNKNOWN,
    COMPLETED
}
