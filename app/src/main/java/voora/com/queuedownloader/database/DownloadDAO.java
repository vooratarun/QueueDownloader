package voora.com.queuedownloader.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by tarun on 12/10/17.
 */

@Dao
public interface DownloadDAO {

    @Query("SELECT * FROM ditem")
    Single<List<DItem>> getAllItems();

    @Query("SELECT * FROM ditem WHERE id LIKE :id")
    Flowable<DItem> findItemById(int id);


    @Query("SELECT * FROM ditem WHERE id LIKE :id")
    Maybe<DItem> getItemById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertItem(DItem dItem);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(DItem... dItems);

    @Delete
    void delete(DItem dItem);

}

