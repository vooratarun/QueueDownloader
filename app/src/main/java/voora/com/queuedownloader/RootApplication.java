package voora.com.queuedownloader;

import android.app.Application;

import com.downloader.PRDownloader;

/**
 * Created by tarun on 12/9/17.
 */

public class RootApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PRDownloader.initialize(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
