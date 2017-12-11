package voora.com.queuedownloader.downloader;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import voora.com.queuedownloader.database.DItem;

/**
 * Created by tarun on 12/10/17.
 *
 */

public class PRFileDownloader implements OnProgressListener, OnPauseListener,
        OnStartOrResumeListener,OnDownloadListener,OnCancelListener {

    private static final String  TAG  = "PRFILEDOWNLOADER";
    private final int UPDATE_PROGRESS_INTERVAL = 3000;
    private int currentDownloadId ;
    ScheduledExecutorService updateService;


    @Nullable
    Progress progress;

    public enum DownloaderState {
        AVAILABLE,
        DOWNLOADING
    }

    private static DownloaderState downloaderState = DownloaderState.AVAILABLE;
    private DownloadEventUIListener downloadEventUIListener;
    private DItem downloadItem;

    public PRFileDownloader(Context context, DItem dItem, DownloadEventUIListener downloadEventUIListener) {
        this.downloadItem = dItem;
        this.downloadEventUIListener = downloadEventUIListener;
        if(dItem.getDownloadId() != -1)
            currentDownloadId = dItem.getDownloadId();
    }


    public static DownloaderState getDownloaderState() {
        return downloaderState;
    }

    @Override
    public void onProgress(Progress progress) {
      //  Log.d(TAG," on Progress: " + progress);
        this.progress = progress;
    }

    @Override
    public void onPause() {
        downloaderState = DownloaderState.AVAILABLE;
        downloadEventUIListener.onDownloadPaused(downloadItem);
    }

    @Override
    public void onStartOrResume() {
        downloaderState = DownloaderState.DOWNLOADING;
        downloadEventUIListener.onDownloadStarted(downloadItem);
    }

    @Override
    public void onDownloadComplete() {
        downloaderState = DownloaderState.AVAILABLE;
        downloadEventUIListener.onDownloadCompleted(downloadItem);
    }

    @Override
    public void onError(Error error) {
        downloaderState = DownloaderState.AVAILABLE;
    }

    @Override
    public void onCancel() {
        downloaderState = DownloaderState.AVAILABLE;
        downloadEventUIListener.onDownloadCancelled(downloadItem);
    }

    public void startFileDownload() {

        DownloadRequest downloadRequestBuilder = PRDownloader.download(
            downloadItem.getDownloadUrl(),
            downloadItem.getDownloadPath(),
            downloadItem.getFileName())
            .build();

        DownloadRequest downloadRequest =  downloadRequestBuilder.setOnStartOrResumeListener(this)
            .setOnProgressListener(this)
            .setOnCancelListener(this)
            .setOnPauseListener(this);

        currentDownloadId = downloadRequest.start(this);
        startUpdateService();
    }

    private void startUpdateService() {
        updateService = Executors.newSingleThreadScheduledExecutor();
        updateService.scheduleAtFixedRate(new UpdateRunnable(), 0,
                UPDATE_PROGRESS_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void stopUpDateService() {
        updateService.shutdown();
    }

    public void pauseFileDownload() {
        stopUpDateService();
        PRDownloader.pause(currentDownloadId);
    }

    public void resumeFileDownload() {
        startUpdateService();
        PRDownloader.resume(currentDownloadId);
    }

    public void cancelFileDownload() {
        stopUpDateService();
        PRDownloader.cancel(currentDownloadId);
    }

    private class UpdateRunnable implements Runnable {

        @Override
        public void run() {
            if(progress != null) {
                Log.d(TAG," Calling this ");
                downloadEventUIListener.onDownloadProgress(downloadItem,progress);
            }
        }

    }
}
