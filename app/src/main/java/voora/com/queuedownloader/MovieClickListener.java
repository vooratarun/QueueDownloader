package voora.com.queuedownloader;

import voora.com.queuedownloader.database.DItem;

/**
 * Created by tarun on 12/10/17.
 */

public interface MovieClickListener {

    void onDownloadClick(DItem dItem);
    void onPauseClick(DItem dItem);
    void onResumeClick(DItem dItem);
    void onCancleClick(DItem dItem);

}
