package company.android.documentmanager.Executor;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {

    private static Object lock = new Object();
    private static AppExecutor instance;
    private Executor diskIO;
    private Executor mainThread;
    private Executor networkIO;


    private AppExecutor(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static AppExecutor  getInstance(){
        if (instance ==null){
            synchronized (lock){
                instance = new AppExecutor(Executors.newSingleThreadExecutor(),Executors.newFixedThreadPool(3),new MainThreadExecutor());
            }

        }
        return instance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public void setDiskIO(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }



    private static class MainThreadExecutor implements Executor {
     Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}
