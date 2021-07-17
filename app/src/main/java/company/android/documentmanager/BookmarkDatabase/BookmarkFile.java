package company.android.documentmanager.BookmarkDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmark")
public class BookmarkFile {
    @NonNull
    @PrimaryKey
    private String  path;

    @ColumnInfo(name = "file")
    private String  absolutePath;

    public BookmarkFile(String path, String absolutePath) {
        this.path = path;
        this.absolutePath = absolutePath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }
}
