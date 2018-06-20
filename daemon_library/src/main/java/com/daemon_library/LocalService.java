package com.daemon_library;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2018/1/29 0029.
 */

public class LocalService extends Service {

    private static final String TAG = "Service";
    private MyBinder myBinder;

    class MyBinder extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double
                aDouble, String aString) throws RemoteException {

        }
    }

    ServiceConnection serviceConnection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myBinder = new MyBinder();
        serviceConnection = new ServiceConnection();
        //使Service变成前台服务
        startForeground(20, new Notification());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startService(new Intent(this, InnnerService.class));
        }
        if (onCreateRunnable != null){
            onCreateRunnable.run();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (onDestroyRunnable != null){
            onDestroyRunnable.run();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, RemoteService.class), serviceConnection,
                BIND_AUTO_CREATE);

        return super.onStartCommand(intent, flags, startId);
    }

    class ServiceConnection implements android.content.ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接后回调
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG,"子进程可能被干掉了，拉活");
            //连接中断后回调
            startService(new Intent(LocalService.this, RemoteService.class));
            bindService(new Intent(LocalService.this, RemoteService.class),serviceConnection,
                    BIND_AUTO_CREATE);
        }
    }

    public static class InnnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
            startForeground(20, new Notification());
            stopSelf();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    Runnable onCreateRunnable;
    Runnable onDestroyRunnable;

    public void setOnCreateRunnable(Runnable onCreateRunnable) {
        this.onCreateRunnable = onCreateRunnable;
    }

    public void setOnDestroyRunnable(Runnable onDestroyRunnable) {
        this.onDestroyRunnable = onDestroyRunnable;
    }
}
