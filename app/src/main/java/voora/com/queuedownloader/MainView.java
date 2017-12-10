package voora.com.queuedownloader;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

import java.util.List;

import voora.com.queuedownloader.database.DItem;

/**
 * Created by tarun on 12/9/17.
 */

public interface MainView extends MvpLceView<List<DItem>> {

    void showAddItem();
}
