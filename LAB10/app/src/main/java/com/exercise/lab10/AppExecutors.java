package com.exercise.lab10;

import android.os.Looper;
import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// Class to manage background thread execution, ensuring database and network tasks don't block the main thread
public class AppExecutors {
    private static final Object LOCK = new Object(); // Lock for thread safety
    private static AppExecutors sInstance;           // Singleton instance of AppExecutors
    private final Executor diskIO;                  // Executor for database (disk) operations
    private final Executor mainThread;              // Executor for main thread operations
    private final Executor networkIO;               // Executor for network operations

    // Private constructor to initialize the executors
    private AppExecutors(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    // Singleton method to get the instance of AppExecutors
    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) { // Ensuring thread safety
                sInstance = new AppExecutors(
                        Executors.newSingleThreadExecutor(),        // Single thread for disk IO
                        new MainThreadExecutor(),                   // Custom executor for main thread
                        Executors.newFixedThreadPool(3));   // Fixed thread pool for network tasks
            }
        }
        return sInstance;
    }

    // Methods to access the different executors
    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

    // Executor class to handle main thread operations
    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper()); // Handler linked to the main thread's Looper

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command); // Post the runnable to the main thread
        }
    }
}
