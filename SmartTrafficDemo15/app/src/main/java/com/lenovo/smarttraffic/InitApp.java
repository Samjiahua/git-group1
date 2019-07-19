package com.lenovo.smarttraffic;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */
public class InitApp extends MultiDexApplication {

    private static Handler mainHandler;
    //    private static Context AppContext;
    private static InitApp instance;
    private Set<Activity> allActivities;
    private static RequestQueue requestQueue;
    private static Context context;
    public static boolean flag = true;
    public static long firstTimer = 0;
    public static DecimalFormat format = new DecimalFormat("###,###,###,##0.00");


    public static synchronized InitApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
//        AppContext = this;
        instance = this;
        mainHandler = new Handler();
        context = getApplicationContext();
        if (requestQueue == null) {
            synchronized (this) {
                requestQueue = Volley.newRequestQueue(instance);
            }
        }
        LitePal.initialize(context);
    }

    public static Context getContext() {
        return context;
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    //    public static Context getContext(){
//        return AppContext;
//    }
    public static Handler getHandler() {
        return mainHandler;
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
