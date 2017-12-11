package voora.com.queuedownloader.downloader;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import voora.com.queuedownloader.database.AppDatabase;
import voora.com.queuedownloader.database.DItem;

/**
 * Created by tarun on 12/11/17.
 */

public class DBUpdateHandler {

    private Executor executor = Executors.newFixedThreadPool(2);
    private Context context;

    public DBUpdateHandler(Context context) {
        this.context = context.getApplicationContext();
    }



    public void updateProgressInDB(DItem dItem , int progress) {
        dItem.setDownloadPercent(progress);
        dItem.setState(DownloadState.DOWNLOADING.ordinal());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(context).downloadDao().insertItem(dItem);
            }
        });
    }

    public void updatePauseInDB(DItem dItem) {
        dItem.setState(DownloadState.PAUSED.ordinal());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(context).downloadDao().insertItem(dItem);
            }
        });
    }


    public void updateCompletedInDB(DItem dItem) {
        dItem.setState(DownloadState.COMPLETED.ordinal());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(context).downloadDao().insertItem(dItem);
            }
        });
    }

    public void updateCancelledInDB(DItem dItem) {
        dItem.setDownloadPercent(0);
        dItem.setState(DownloadState.NORMAL.ordinal());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(context).downloadDao().insertItem(dItem);
            }
        });
    }
}
