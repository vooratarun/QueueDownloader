package voora.com.queuedownloader.downloader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import voora.com.queuedownloader.database.DItem;
import voora.com.queuedownloader.MainActivity;

/**
 * Created by tarun on 12/10/17.
 *
 */

public class DownloadProgressHandler {

    private static final String TAG = "DownloadProgressHandler";


    private DItem downloadItem;
    private NotificationManager notificationManager;

    private int notificationId;

    private Context context;
    NotificationCompat.Builder notificationBuilder;


    DownloadProgressHandler(Context context,DItem dItem) {
        this.downloadItem = dItem;
        this.context = context;
    }

    public void startForegroundNotification(int initialProgress) {
        Log.d(TAG, "currentThread=" + Thread.currentThread().toString());
        Notification notification = createNotification(initialProgress);
        notificationManager.notify(notificationId, notification);
        ((DownloadService) context).startForeground(notificationId, notification);
    }

    public void setDownloadItem(DItem downloadItem) {
        this.downloadItem = downloadItem;
    }


    private Notification createNotification(int progress) {

        notificationId = downloadItem.getId();

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(context);

        PendingIntent pendingIntent = getPendingIntentOnClick();

        return notificationBuilder.setContentTitle(downloadItem.getFileName())
            .setContentText(downloadItem.getFileName() + " Downloading")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                    android.R.drawable.ic_menu_info_details))
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(false)
            .setProgress(100, progress, false)
            .setContentIntent(pendingIntent)
            .setOngoing(false)
            .setContentInfo(String.format("%d%%", progress))
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Cancel",
                    getCancelPendingIntent())
            .addAction(android.R.drawable.ic_media_pause, "Pause",
                    getPausePendingIntent())
            .build();

    }

    public void updateProgress(DItem downloadItem , int currentProgress) {
        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentInfo(String.format("%d%%", currentProgress));
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private PendingIntent getResumePendingIntent() {
        Intent notificationIntent = new Intent(context, DownloadService.class);
        notificationIntent.putExtra(DownloadService.SERVICE_ACTION,
                DownloadService.START_SERVICE_ACTION);
        notificationIntent.putExtra(DownloadService.EXTRA_DITEM,downloadItem);
        return PendingIntent.getService(context, getUniqueId(), notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private PendingIntent getPausePendingIntent() {
        Intent notificationIntent = new Intent(context, DownloadService.class);
        notificationIntent.putExtra(DownloadService.SERVICE_ACTION,DownloadService.PAUSE_SERVICE_ACTION);
        notificationIntent.putExtra(DownloadService.EXTRA_DITEM, downloadItem);
        return PendingIntent.getService(context, getUniqueId(), notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private PendingIntent getCancelPendingIntent() {
        Intent notificationIntent = new Intent(context, DownloadService.class);
        notificationIntent.putExtra(DownloadService.SERVICE_ACTION,DownloadService.CANCEL_SERVICE_ACTION);
        notificationIntent.putExtra(DownloadService.EXTRA_DITEM,downloadItem);
        return PendingIntent.getService(context, getUniqueId(), notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }


    private PendingIntent getPendingIntentOnClick() {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, getUniqueId(), notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }


    private static int getUniqueId() {
        return (int) (System.currentTimeMillis() & 0xfffffff);
    }

}
