package voora.com.queuedownloader.downloader;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.util.Log;

import com.downloader.Progress;

import voora.com.queuedownloader.database.DItem;


public class DownloadService extends Service  implements DownloadEventUIListener{


    public static final String TAG = "DOWNLOAD_SERVICE";
    public static final String EXTRA_DITEM = "EXTRA_DITEM";
    public static final String SERVICE_ACTION = "SERVICE_ACTION";
    public static final String START_SERVICE_ACTION = "START";
    public static final String PAUSE_SERVICE_ACTION = "PAUSE";
    public static final String CANCEL_SERVICE_ACTION = "CANCEL";
    public static final String RESUME_SERVICE_ACTION = "RESUME";

    ThreadLocal<PRFileDownloader> fileDownloader = new ThreadLocal<>();
    private DownloadProgressHandler downloadProgressHandler;
    private DBUpdateHandler dbUpdateHandler;

    public ServiceHandler serviceHandler;
    public Looper bgThreadLooper;

    public DownloadService() {}


    public static void startDownload(@NonNull Context context, @NonNull DItem dItem) {
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra(EXTRA_DITEM,dItem);
        intent.putExtra(SERVICE_ACTION,START_SERVICE_ACTION);
        context.startService(intent);
    }

    public static void pauseDownload(@NonNull Context context, @NonNull DItem dItem) {
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra(EXTRA_DITEM,dItem);
        intent.putExtra(SERVICE_ACTION,PAUSE_SERVICE_ACTION);
        context.startService(intent);
    }

    public static void resumeDownload(@NonNull Context context, @NonNull DItem dItem) {
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra(EXTRA_DITEM,dItem);
        intent.putExtra(SERVICE_ACTION,RESUME_SERVICE_ACTION);
        context.startService(intent);
    }
    public static void cancelDownload(@NonNull Context context, @NonNull DItem dItem) {
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra(EXTRA_DITEM,dItem);
        intent.putExtra(SERVICE_ACTION,CANCEL_SERVICE_ACTION);
        context.startService(intent);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread handlerThread = new HandlerThread("DownloadThread",
                Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();

        dbUpdateHandler = new DBUpdateHandler(this);
        bgThreadLooper = handlerThread.getLooper();
        serviceHandler = new ServiceHandler(handlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null && intent.getExtras() != null) {
           if(intent.getExtras().containsKey(EXTRA_DITEM)) {
               postMessageToTheBackgroundThread(intent, startId);
           }
        }
        return START_STICKY;
    }

    private void postMessageToTheBackgroundThread(Intent intent, int startId) {

        Message msg = serviceHandler.obtainMessage();
        msg.setData(intent.getExtras());
        serviceHandler.sendMessage(msg);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDownloadProgress(DItem dItem, Progress progress) {
        double div = (progress.currentBytes + 0.01) / (progress.totalBytes + 0.01);
        int progressInt = (int) (div * 100);

        new Handler(Looper.getMainLooper()).post(() ->
             downloadProgressHandler.updateProgress(dItem, progressInt));

        dbUpdateHandler.updateProgressInDB(dItem,progressInt);
    }

    @Override
    public void onDownloadPaused(DItem dItem) {
        Log.d(TAG,"onDownload paused " + dItem.toString());
        dbUpdateHandler.updatePauseInDB(dItem);
    }

    @Override
    public void onDownloadStarted(DItem dItem) {
        Log.d(TAG,"onDownload Started " + dItem.toString());
        downloadProgressHandler.setDownloadItem(dItem);
        downloadProgressHandler.startForegroundNotification(dItem.getDownloadPercent());
    }

    @Override
    public void onDownloadCancelled(DItem dItem) {
        Log.d(TAG,"onDownload Cancelled " + dItem.toString());
        dbUpdateHandler.updateCancelledInDB(dItem);
        downloadProgressHandler.stopForegroundNotification();
        stopSelf();
    }

    @Override
    public void onDownloadCompleted(DItem dItem) {
        Log.d(TAG,"onDownload Completed " + dItem.toString());
        dbUpdateHandler.updateCompletedInDB(dItem);
        downloadProgressHandler.stopForegroundNotification();
        stopService();
    }

    public void stopService() {
        stopSelf();
        new Handler().postDelayed(() -> {
            bgThreadLooper.quit();
            serviceHandler = null;
            serviceHandler = null;
            fileDownloader = null;
            dbUpdateHandler = null;
        },300);

    }

    public void startDownload(DItem dItem) {
        downloadProgressHandler = new DownloadProgressHandler(this,dItem);
        fileDownloader.set(new PRFileDownloader(DownloadService.this,dItem,this));
        fileDownloader.get().startFileDownload();
    }

    public void pauseDownload(DItem dItem) {
        if(fileDownloader.get() == null)
            fileDownloader.set(new PRFileDownloader(DownloadService.this,dItem,this));
        fileDownloader.get().pauseFileDownload();
    }

    public void resumeDownload(DItem dItem) {
        if(fileDownloader.get() == null)
            fileDownloader.set(new PRFileDownloader(DownloadService.this,dItem,this));
        fileDownloader.get().resumeFileDownload();
    }

    public void cancelDownload(DItem dItem) {
        if(fileDownloader.get() == null)
            fileDownloader.set(new PRFileDownloader(DownloadService.this,dItem,this));
        fileDownloader.get().cancelFileDownload();
    }

    public class ServiceHandler extends Handler {

        ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String action = bundle.getString(SERVICE_ACTION,"");
            DItem dItem = bundle.getParcelable(EXTRA_DITEM);
            switch (action) {
                case START_SERVICE_ACTION :
                    startDownload(dItem);
                    break;
                case PAUSE_SERVICE_ACTION :
                    pauseDownload(dItem);
                    break;
                case RESUME_SERVICE_ACTION :
                    resumeDownload(dItem);
                    break;
                case CANCEL_SERVICE_ACTION :
                    cancelDownload(dItem);
                    break;
            }
        }
    }

}
