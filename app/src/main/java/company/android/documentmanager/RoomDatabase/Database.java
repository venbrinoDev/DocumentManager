package company.android.documentmanager.RoomDatabase;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = RecentFile.class,exportSchema =false,version = 2)
public abstract class Database extends RoomDatabase {
    private static Database databaseInstance;
    private static final String DBNAME ="file_db";

    public static Database getDatabaseInstance(Context context){
        if (databaseInstance ==null){

            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),Database.class,DBNAME).fallbackToDestructiveMigration().build();
        }


        return databaseInstance;
    }

    public abstract fileDao Dao();
}
