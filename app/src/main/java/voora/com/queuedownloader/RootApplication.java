package voora.com.queuedownloader;

import android.app.Application;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;

import voora.com.queuedownloader.database.AppDatabase;

/**
 * Created by tarun on 12/9/17.
 */

public class RootApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this,config);

        AppDatabase.getAppDatabase(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
