package voora.com.queuedownloader.downloader;


public interface DownloadEventUIListener {

    void onDownloadOpened(String movieId , int quality,int progress);

    void onDownloadProgress(String movieId, String localPath, int currentProgress);

    void onDownloadDRMServerAuth(String movieId);

    void onDownloadChunkFailed(String movieId, int currentProgress);

    void onDownloadPaused(String movieId, int currentProgress ,boolean interrupted);

    void onDownloadResumed(String movieId, int currentProgress);

    void onDownloadCompleted(String movieId,String localPath, int quality, int currentProgress);

    void onDownloaderManifestOk(String movieId, String localPath ,int quality ,int progress);

    void onDownloadFailed(String movieId,int currentProgress, int code);

    void onDownloadCancelled(String movieId,int currentProgress);

    void onDiskFull(String movieId, int currentProgress);
}