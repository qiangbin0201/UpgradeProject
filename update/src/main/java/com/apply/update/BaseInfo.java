package com.apply.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2017/8/14.
 */

public class BaseInfo {

    //版本code码
    public static int versionCode;

    public static String versionName;

    //热更新对应的versionCode,每次更新差分包 tinkerCode应手动加1
    public final static int tinkerCode = 1;

    public static  String PROJECT;

    public static final String business = "bbnews";

    public static void init(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        } catch (Exception e) {
            versionName = "undefined";
        }
    }
}
