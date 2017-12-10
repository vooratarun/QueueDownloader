package voora.com.queuedownloader.downloader;
import android.content.Context;
import android.util.Log;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import voora.com.queuedownloader.database.DItem;

/**
 * Created by tarun on 12/10/17.
 *
 */

public class PRFileDownloader implements OnProgressListener, OnPauseListener,
        OnStartOrResumeListener,OnDownloadListener,OnCancelListener {

    private static final String  TAG  = "PRFILEDOWNLOADER";

    public enum DownloaderState {
        AVAILABLE,
        DOWNLOADING
    }

    private DownloaderState downloaderState = DownloaderState.AVAILABLE;
    private DownloadEventUIListener downloadEventUIListener;


    private DItem downloadItem;
    private Context context;

    public PRFileDownloader(Context context, DItem dItem, DownloadEventUIListener downloadEventUIListener) {
        this.context = context;
        this.downloadItem = dItem;
        this.downloadEventUIListener = downloadEventUIListener;
    }


    @Override
    public void onProgress(Progress progress) {
        Log.d(TAG," on Progress: " + progress);
        downloadEventUIListener.onDownloadProgress(downloadItem,
                (progress.currentBytes /progress.totalBytes) * 100 );
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

        PRDownloader.download(
            downloadItem.getDownloadUrl(),
            downloadItem.getDownloadPath(),
            downloadItem.getFileName())
            .build()
            .setOnStartOrResumeListener(this)
            .setOnProgressListener(this)
            .setOnCancelListener(this)
            .setOnPauseListener(this)
            .start(this);
    }
}
