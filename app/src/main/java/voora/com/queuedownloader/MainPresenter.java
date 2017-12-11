package voora.com.queuedownloader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import voora.com.queuedownloader.database.AppDatabase;
import voora.com.queuedownloader.database.DItem;
import voora.com.queuedownloader.utils.FileUtils;

/**
 * Created by tarun on 12/9/17.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {

    private static final String TAG = "MAIN_PRESENTER";
    private Executor executor = Executors.newFixedThreadPool(2);



    public void loadDownloadItems(Context context) {

        AppDatabase.getAppDatabase(context).downloadDao().getAllItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::showData, throwable -> showError());
    }


    public void showData(@Nullable  final List<DItem> dItems){
        ifViewAttached(view -> {
            if(dItems != null && dItems.size() > 0) {
                view.setData(dItems);
                view.showContent();
            } else {
                showError();
            }
        });
    }

    public void showError(){
        Log.d(TAG,"showError");
        ifViewAttached(view -> {
           // view.showError(new Throwable("Error"),false);
            view.showAddItem();
        });
    }


    public void insertTestData(Context context){


        final DItem dItem1 = DItem.newBuilder()
                .id(1)
                .downloadPath(FileUtils.getDirectoryPath())
                .downloadPercent(0)
                .downloadUrl("http://www.appsapk.com/downloading/latest/Facebook-119.0.0.23.70.apk")
                .fileName("Facebook")
                .build();

        final DItem dItem2 = DItem.newBuilder()
                .id(2)
                .downloadPath(FileUtils.getDirectoryPath())
                .downloadPercent(0)
                .downloadUrl("http://www.appsapk.com/downloading/latest/WeChat-6.5.7.apk")
                .fileName("WeChat")
                .build();

        final DItem dItem3 = DItem.newBuilder()
                .id(3)
                .downloadPath(FileUtils.getDirectoryPath())
                .downloadPercent(0)
                .downloadUrl("http://www.appsapk.com/downloading/latest/Instagram.apk")
                .fileName("Instagram")
                .build();

        final DItem dItem4 = DItem.newBuilder()
                .id(4)
                .downloadPath(FileUtils.getDirectoryPath())
                .downloadPercent(0)
                .downloadUrl("http://www.appsapk.com/downloading/latest/Emoji%20Flashlight%20-%20Brightest%20Flashlight%202018-2.0.1.apk")
                .fileName("Emoji")
                .build();

        final DItem dItem5 = DItem.newBuilder()
                .id(5)
                .downloadPath(FileUtils.getDirectoryPath())
                .downloadPercent(0)
                .downloadUrl("http://www.appsapk.com/downloading/latest/Screen%20Recorder-7.7.apk")
                .fileName("SoundRecorder")
                .build();

        final List<DItem> list = new ArrayList<DItem>() {{
            add(dItem1);
            add(dItem2);
            add(dItem3);
            add(dItem4);
            add(dItem5);
        }};

        executor.execute(() -> AppDatabase.getAppDatabase(context)
                .downloadDao().insertAll(dItem1,dItem2,dItem3,dItem4,dItem5));

    }

}
