package company.android.documentmanager.TopAction;

import android.content.Context;

import java.io.File;
import java.util.List;

import company.android.documentmanager.BookmarkDatabase.BookmarkDatabase;
import company.android.documentmanager.BookmarkDatabase.BookmarkFile;
import company.android.documentmanager.Executor.AppExecutor;

public class PerformAction implements Actions{

    private Context context;
    BookmarkDatabase database;
    Finsihcallback finsihcallback;

    public PerformAction(Context context,Finsihcallback finsihcallback) {
        this.context = context;
        database = BookmarkDatabase.getDatabaseInstance(context);
        this.finsihcallback = finsihcallback;
    }

    @Override
    public void bookMark(File file) {
        BookmarkFile bookmarkFile = new BookmarkFile(file.getAbsolutePath(),file.getAbsolutePath());

        AppExecutor.getInstance().getDiskIO().execute(() -> {
            database.Dao().insertFile(bookmarkFile);

        });
    }


    @Override
    public void isBookMark(File file) {

        AppExecutor.getInstance().getDiskIO().execute(() -> {
       List<BookmarkFile> bookmarkFiles = database.Dao().getPersonList();
       boolean isFileFound = false;
       for (BookmarkFile presentFile  : bookmarkFiles){
           if (file.getAbsolutePath().equals(presentFile.getAbsolutePath())){
               isFileFound = true;
               break;
           }
       }

            finsihcallback.finish(isFileFound);
        });

    }

    @Override
    public void destroy() {
        if (context!=null){
            context = null;
        }

        if (database!=null){
            database = null;
        }
        if (finsihcallback !=null){
            finsihcallback = null;
        }
    }

    @Override
    public void DeleteBookMark(File file) {
        BookmarkFile bookmarkFile = new BookmarkFile(file.getAbsolutePath(),file.getAbsolutePath());

        AppExecutor.getInstance().getDiskIO().execute(() -> {
            database.Dao().deleteFile(bookmarkFile);

        });
    }


}
