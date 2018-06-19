package com.daemon_library;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/1/29 0029.
 */

public class DaemonUtils {

    protected static boolean isRunningService(Context context, String name) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : runningServices
                ) {
            if (TextUtils.equals(info.service.getClassName(), name)) {
                return true;
            }
        }
        return false;
    }
    public static void start(Activity activity){
        activity.startService(new Intent(activity, LocalService.class));
        activity.startService(new Intent(activity, RemoteService.class));
        MyJobService.StartJob(activity);
    }
}
