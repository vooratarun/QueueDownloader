package voora.com.queuedownloader.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by tarun on 12/10/17.
 */

public class FileUtils {

    public static String getDirectoryPath() {

        File downloadFolder = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        return downloadFolder.getAbsolutePath();
    }
}
