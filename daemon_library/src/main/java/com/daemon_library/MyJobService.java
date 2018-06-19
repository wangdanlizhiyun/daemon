package com.daemon_library;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/**
 * Created by Administrator on 2018/1/29 0029.
 */

@SuppressLint("NewApi")
public class MyJobService extends JobService {

    public static void StartJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context
                .JOB_SCHEDULER_SERVICE);
//        setPersisted 在设备重启依然执行
        JobInfo.Builder builder = new JobInfo.Builder(10, new ComponentName(context
                .getPackageName(), MyJobService.class
                .getName())).setPersisted(true);
        //小于7.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // 每隔1s 执行一次 job
            builder.setPeriodic(1_000);
        } else {
            //延迟执行任务
            builder.setMinimumLatency(1_000);
        }

        jobScheduler.schedule(builder.build());
    }

    private static final String TAG = "MyJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "开启job");
        //如果7.0以上 轮训
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StartJob(this);
        }
        boolean isLocalRun = DaemonUtils.isRunningService(this, LocalService.class.getName());
        boolean isRemoteRun = DaemonUtils.isRunningService(this, RemoteService.class.getName());
        if (!isLocalRun || !isRemoteRun) {
            Log.e(TAG, "job  拉活");
            startService(new Intent(this, LocalService.class));
            startService(new Intent(this, RemoteService.class));
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
