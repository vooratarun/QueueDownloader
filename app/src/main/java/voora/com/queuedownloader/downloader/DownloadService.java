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

import voora.com.queuedownloader.database.DItem;


public class DownloadService extends Service  implements DownloadEventUIListener{


    public static final String TAG = "DOWNLOAD_SERVICE";
    public static final String EXTRA_DITEM = "EXTRA_DITEM";
    public static final String SERVICE_ACTION = "SERVICE_ACTION";
    public static final String START_SERVICE_ACTION = "START";
    public static final String PAUSE_SERVICE_ACTION = "PAUSE";
    public static final String CANCEL_SERVICE_ACTION = "CANCEL";
    public static final String RESEUME_SERVICE_ACTION = "RESUME";

    ThreadLocal<PRFileDownloader> fileDownloader = new ThreadLocal<>();
    private DownloadProgressHandler downloadProgressHandler;

    public ServiceHandler serviceHandler;
    public Looper bgThreadLooper;

    public DownloadService() {}


    public static void startDownload(@NonNull Context context, @NonNull DItem dItem) {
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra(EXTRA_DITEM,dItem);
        intent.putExtra(SERVICE_ACTION,START_SERVICE_ACTION);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread handlerThread = new HandlerThread("DownloadThread",
                Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
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
    public void onDownloadProgress(DItem dItem, long progress) {

        downloadProgressHandler.updateProgress(dItem,(int)progress);
    }

    @Override
    public void onDownloadPaused(DItem dItem) {

    }

    @Override
    public void onDownloadStarted(DItem dItem) {

        downloadProgressHandler.setDownloadItem(dItem);
        downloadProgressHandler.startForegroundNotification(0);
    }

    @Override
    public void onDownloadCancelled(DItem dItem) {

    }

    @Override
    public void onDownloadCompleted(DItem dItem) {

    }

    public void startDownload(DItem dItem) {

        downloadProgressHandler = new DownloadProgressHandler(this,dItem);

        if(fileDownloader == null)
            fileDownloader = new ThreadLocal<>();

        fileDownloader.set(new PRFileDownloader(DownloadService.this,dItem,this));
        fileDownloader.get().startFileDownload();
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
                case RESEUME_SERVICE_ACTION :
                    break;
                case CANCEL_SERVICE_ACTION :
                    break;
                case PAUSE_SERVICE_ACTION :
                    break;
            }
        }
    }

}
