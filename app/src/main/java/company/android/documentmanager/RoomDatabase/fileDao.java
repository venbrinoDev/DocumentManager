package company.android.documentmanager.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface fileDao {

    @Query("SELECT * FROM storeFile")
    List<RecentFile> getPersonList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFile(RecentFile recentFile);

    @Update
    void updateFile(RecentFile recentFile);

    @Delete
    void deleteFile(RecentFile recentFile);
}
