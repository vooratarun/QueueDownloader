package voora.com.queuedownloader.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by tarun on 12/10/17.
 */


@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    Flowable<List<User>>getAll();

    @Query("SELECT * FROM user where first_name LIKE  :firstName AND last_name LIKE :lastName")
    Single<User> findByName(String firstName, String lastName);

    @Query("SELECT COUNT(*) from user")
    int countUsers();


    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

}
