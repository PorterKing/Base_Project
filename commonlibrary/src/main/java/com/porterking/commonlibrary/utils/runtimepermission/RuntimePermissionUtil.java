package com.porterking.commonlibrary.utils.runtimepermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;

import com.porterking.commonlibrary.manager.MMKVManager;
import com.porterking.commonlibrary.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 *  Created by mshuaia on 16-9-6.
 */
public class RuntimePermissionUtil {
    public static final int PERMISSION_ACTIVITY_REQUEST_CODE = 3001;
    public static final int FLOAT_PERMISSION_ACTIVITY_REQUEST_CODE = 3002;
    public static final int SYS_SETTING_PERMISSION_ACTIVITY_REQUEST_CODE = 3003;
    private static final String TAG = "RuntimePermissionUtil";

    /**
     * 适配android6.0运行时权限，检查是否有权限
     *
     * @param context    context
     * @param permission Manifest.permission.xxx
     */
    public static boolean checkSelfPermission(Context context, String permission) {
        return context != null && ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkPermissionsArray(Context context, String[] permissions) {
        if (context == null || permissions == null || permissions.length == 0) {
            return false;
        }
        for (String temp : permissions) {
            if (!checkSelfPermission(context, temp)) {
                return false;
            }
        }
        return true;
    }

    public static String[] getPermissionsArray(Context context, String[] permissions) {
        if (context == null || permissions == null || permissions.length == 0) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        for (String temp : permissions) {
            if (!checkSelfPermission(context, temp)) {
                list.add(temp);
            }
        }
        String[] permissionArray = list.toArray(new String[list.size()]);
        return permissionArray;
    }

    public static boolean couldPopUpSystemDialogForPermission(Activity activity, String[] permissions) {
        if (activity == null || permissions == null || permissions.length == 0) {
            LogUtils.e("shouldShowDescriptionForRequest()--> activity == null || permissions == null || permissions.length == 0");
            return false;
        }
        for (String permission : permissions) {
            if (!couldPopUpSystemDialogForPermission(activity, permission)) { //同时检查多个权限时，有一个不能弹框，即返回不能弹框
                return false;
            }
        }
        return true;
    }

    public static boolean couldPopUpSystemDialogForPermission(Activity activity, String permission) {
        if (activity == null || TextUtils.isEmpty(permission)) {
            LogUtils.e("shouldShowDescriptionForRequest()--> activity == null || TextUtils.isEmpty(permission)");
            return false;
        }
        return !getHasPermissionRequested(permission) || ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    static void saveHasPermissionsRequested(String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            return;
        }
        for (String permission : permissions) {
            saveHasPermissionRequested(permission);
        }
    }

    private static void saveHasPermissionRequested(String permission) {
        MMKVManager.defaultMMKV.setBoolean(permission, true);
    }

    private static boolean getHasPermissionRequested(String permission) {
        return MMKVManager.defaultMMKV.getBoolean(permission,false);
    }

    private static void setPermissionCallbackModel(String[] permissions, PermissionRequestManager.OnPermissionRequestResultCallback callback) {
        if (permissions == null || permissions.length == 0 || callback == null) {
            LogUtils.e("setPermissionCallbackModel()--> permissions == null || permissions.length == 0 || callback == null");
            return;
        }
        RuntimePermissionModel callbackModel= new RuntimePermissionModel(callback, permissions);
        PermissionRequestManager.getmInstance().setmRuntimePermissionModel(callbackModel);
    }

    /**
     * 跳转到应用详情页面，并保存请求权限以及回调到RuntimeDataManager供从详情页面返回到手炒时调用
     * **/
    static void gotoAppDetails(Activity activity, String[] permissions, PermissionRequestManager.OnPermissionRequestResultCallback callback) {
        if (activity == null) {
            LogUtils.e("gotoAppDetails()--> activity == null");
            return;
        }
        setPermissionCallbackModel(permissions, callback);
        Intent settingIntent = new Intent();
        settingIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        settingIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivityForResult(settingIntent, RuntimePermissionUtil.PERMISSION_ACTIVITY_REQUEST_CODE);
    }

    //有些手机系统(小米，乐视)，拒绝权限，但是返回有权限，所以以此方法来判断是否真的获取了  设备信息获取权限
//    public static boolean hasPermissionGetPhoneInfo() {
//        Context context = YSApplication.getApplication()();
//        if (!RuntimePermissionUtil.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)) {
//            LogUtils.e("readIIMUFromSystem()--> !RuntimePermissionUtil.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)");
//            return false;
//        }
//        try {
//            TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            if (telManager != null) {
//                String imei = telManager.getDeviceId();
//                String imsi = telManager.getSubscriberId();
//                MiddlewareProxy.getHangqingConfigManager().setIMEIString(imei);
//                MiddlewareProxy.getHangqingConfigManager().setIMSIString(imsi);
//                Log.i(TAG, "hasPermissionGetPhoneInfo()--> imei = " + imei);
//            }
//        } catch (Exception e) {
//            Log.printStackTrace(e);
//            return false;
//        }
//        return true;
//    }
}