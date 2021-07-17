package company.android.documentmanager.BookmarkDatabase;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = BookmarkFile.class,exportSchema =false,version = 1)
public abstract class BookmarkDatabase extends RoomDatabase {
    private static BookmarkDatabase bookmarkDatabaseInstance;
    private static final String DBNAME ="bookmark_database";

    public static BookmarkDatabase getDatabaseInstance(Context context){
        if (bookmarkDatabaseInstance ==null){

            bookmarkDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(), BookmarkDatabase.class,DBNAME).fallbackToDestructiveMigration().build();
        }


        return bookmarkDatabaseInstance;
    }

    public abstract BookmarkDao Dao();
}
