package voora.com.queuedownloader;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarun on 12/9/17.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {


    public void loadDownloadItems() {


        final DItem dItem1 = DItem.newBuilder()
                .id(1)
                .downloadPath("")
                .downloadPercent(0)
                .downloadUrl("http://www.appsapk.com/downloading/latest/Facebook-119.0.0.23.70.apk")
                .fileName("Facebook")
                .build();

        final DItem dItem2 = DItem.newBuilder()
                .id(1)
                .downloadPath("")
                .downloadPercent(0)
                .downloadUrl("http://www.appsapk.com/downloading/latest/WeChat-6.5.7.apk")
                .fileName("WeChat")
                .build();

        final DItem dItem3 = DItem.newBuilder()
                .id(1)
                .downloadPath("")
                .downloadPercent(0)
                .downloadUrl("http://www.appsapk.com/downloading/latest/Instagram.apk")
                .fileName("Instagram")
                .build();

        final DItem dItem4 = DItem.newBuilder()
                .id(1)
                .downloadPath("")
                .downloadPercent(0)
                .downloadUrl("http://www.appsapk.com/downloading/latest/Emoji%20Flashlight%20-%20Brightest%20Flashlight%202018-2.0.1.apk")
                .fileName("Emoji")
                .build();

        final DItem dItem5 = DItem.newBuilder()
                .id(1)
                .downloadPath("")
                .downloadPercent(0)
                .downloadUrl("http://www.appsapk.com/downloading/latest/Screen%20Recorder-7.7.apk")
                .fileName("SoundRecorder")
                .build();

        List<DItem> list = new ArrayList<DItem>() {{
           add(dItem1);
           add(dItem2);
           add(dItem3);
           add(dItem4);
           add(dItem5);
        }};

        if(isViewAttached()) {
            getView().setData(list);
        }
    }
}
