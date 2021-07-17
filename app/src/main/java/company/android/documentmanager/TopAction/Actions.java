package company.android.documentmanager.TopAction;

import java.io.File;

public interface Actions {
    void bookMark(File file);
    void  isBookMark(File file);
    void destroy();
    void DeleteBookMark(File file);
}
