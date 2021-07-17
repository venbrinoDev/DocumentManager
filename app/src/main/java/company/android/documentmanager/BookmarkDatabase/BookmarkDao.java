package company.android.documentmanager.BookmarkDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookmarkDao {

    @Query("SELECT * FROM bookmark")
    List<BookmarkFile> getPersonList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFile(BookmarkFile bookmarkFile);

    @Update
    void updateFile(BookmarkFile bookmarkFile);

    @Delete
    void deleteFile(BookmarkFile bookmarkFile);
}
