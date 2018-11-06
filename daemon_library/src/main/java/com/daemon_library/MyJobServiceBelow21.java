package com.daemon_library;

import android.annotation.SuppressLint;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2018/1/29 0029.
 */

@SuppressLint("NewApi")
public class MyJobServiceBelow21 extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    try {
                        sleep(2000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    boolean isLocalRun = DaemonUtils.isRunningService(MyJobServiceBelow21.this, LocalService.class.getName());
                    boolean isRemoteRun = DaemonUtils.isRunningService(MyJobServiceBelow21.this, RemoteService.class.getName());
                    if (!isLocalRun || !isRemoteRun) {
                        startService(new Intent(MyJobServiceBelow21.this, LocalService.class));
                        startService(new Intent(MyJobServiceBelow21.this, RemoteService.class));
                    }
                }
            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
