package com.porterking.commonlibrary.utils.runtimepermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

import com.porterking.commonlibrary.BCHelper;
import com.porterking.commonlibrary.manager.AppManager;
import com.porterking.commonlibrary.utils.LogUtils;

import androidx.core.app.ActivityCompat;

//import android.support.annotation.RequiresApi;

/**
 * Created by ms on 17-9-13.
 */

public class PermissionRequestManager {

    public static final String TAG = "PermissionRequestManager";
    private static final int REQUESTCODE_START = 1000;
    public static PermissionRequestManager mInstance;
    private static int maxRequestCode = REQUESTCODE_START;

    //敏感权限请求回调相关对象维护
    private SparseArray<OnPermissionRequestResultCallback> requestCodeVsCallbackMaps = new SparseArray<>();
    //悬浮窗显示权限请求回调
    private OnFloatPermissionRequestCallback mOnFloatPermissionRequestCallback;
    //修改系统设置权限请求回调
    private OnChangeSysSettingsPermissionRequestCallback mOnChangeSysSettingsPermissionRequestCallback;
    private SparseBooleanArray mCouldShowSystemDialog = new SparseBooleanArray();
    //跳转到设置页面申请权限的回调对象维护
    private RuntimePermissionModel mRuntimePermissionModel; //跳转到 设置-->应用详情页 申请权限的回调模型


    /***
     * 在需要跳转到应用详情页去帮助用户打开权限时，需要保持  resultinBackground 状态不变
     * 原因：由于跳转到应用详情页是在申请权限的回调中执行，而申请权限的回调方法会在Activity的onPause和onResume之间执行，
     *      在onResume中执行了 resultinBackground 标识的清除操作，从而导致从Hexin跳转到应用详情页时，正常执行和分发了onbackground事件
     * ***/
    private boolean isGoingToAppDetails = false;

    /**
     * 在用户授予了获取设备信息的权限后，应当实时更新设备信息
     **/
    private boolean needUpdateHardInfo = false;

    private PermissionRequestManager() {
    }

    public static PermissionRequestManager getmInstance() {
        if (mInstance == null) {
            mInstance = new PermissionRequestManager();
        }
        return mInstance;
    }

    /**
     * 跳转设置悬浮窗权限
     *
     * @param activity
     * @param callback
     */
    public static void doSomethingWhenNeedWindowPermission(Activity activity, final OnFloatPermissionRequestCallback
            callback) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        activity.startActivityForResult(intent, RuntimePermissionUtil.FLOAT_PERMISSION_ACTIVITY_REQUEST_CODE);
        PermissionRequestManager.getmInstance().setOnFloatPermissionRequestCallback(callback);
    }

    /**
     * 针对一些特殊的Activity,无法正常通过getCurrentActivity得到
     *
     * @param permissions
     * @param activity
     * @param callback
     */
    public void requestPermission(String[] permissions, Activity activity, final OnPermissionRequestResultCallback
            callback) { //此接口一次请求多个权限时，只要有一个权限未授权，即返回false
        if (permissions == null || permissions.length == 0) {
            LogUtils.e("requestPermission() --> permissions == null || permissions.length == 0");
            return;
        }
        //执行请求
        executeRequest(permissions, activity, callback);
        //记录某个权限已经申请过
        RuntimePermissionUtil.saveHasPermissionsRequested(permissions);
    }

    private void executeRequest(String[] permissions, Activity activity, final OnPermissionRequestResultCallback
            callback) {
        if (activity == null) {
            LogUtils.e("excuteReust() --> activity == null");
            return;
        }
        boolean couldNotice = RuntimePermissionUtil.couldPopUpSystemDialogForPermission(activity, permissions);
        //当前是否还可以弹出系统提示框
        ActivityCompat.requestPermissions(activity, permissions, getRequestCode(callback, couldNotice));
    }

    private int getRequestCode(OnPermissionRequestResultCallback callback, boolean couldShowSystemDialog) {
        maxRequestCode++;
        requestCodeVsCallbackMaps.put(maxRequestCode, callback);
        mCouldShowSystemDialog.put(maxRequestCode, couldShowSystemDialog);
        return maxRequestCode;
    }

    /**
     * 正常场景权限获取的回调方法，在Acitvity的onRequestPermissionResult 方法里调用
     **/
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean isGrant = true;
        for (int granted : grantResults) {
            if (granted == PackageManager.PERMISSION_DENIED) {
                isGrant = false;
                break;
            }
        }
        if (isGrant) {  //若授予了读取设备信息权限则需要更新设备信息
            setNeedUpdateHardInfo(permissions);
        }
        OnPermissionRequestResultCallback callback = requestCodeVsCallbackMaps.get(requestCode);
        if (callback != null) {
            callback.onPermissionRequestResult(isGrant, mCouldShowSystemDialog.get(requestCode));
        }
        requestCodeVsCallbackMaps.remove(requestCode);
        mCouldShowSystemDialog.delete(requestCode);
    }

    /**
     * 从设置-->应用详情里打开权限的回调方法，在Acitvity的onActivityResult 方法里调用
     **/
    public void onActivityResult() { //处理权限动态获取的回调事件
        if (mRuntimePermissionModel != null && mRuntimePermissionModel.isValid()) {
            boolean isGrant = RuntimePermissionUtil.checkPermissionsArray(BCHelper.getInstance().getContext()
                    , mRuntimePermissionModel.permissions);
            if (isGrant) {  //若授予了读取设备信息权限则需要更新设备信息
                setNeedUpdateHardInfo(mRuntimePermissionModel.permissions);
            }
            mRuntimePermissionModel.callback.onPermissionRequestResult(isGrant, false);
        }
        mRuntimePermissionModel = null;
        isGoingToAppDetails = false;
    }

    void setmRuntimePermissionModel(RuntimePermissionModel mRuntimePermissionModel) {
        this.mRuntimePermissionModel = mRuntimePermissionModel;
    }


    public void gotoAppDetailsForPermission(Activity activity, String[] permissions,
                                            OnPermissionRequestResultCallback callback) {
        if (activity == null) {
            LogUtils.e("gotoAppDetails()--> activity == null");
            return;
        }
        if (permissions == null || permissions.length == 0) {
            LogUtils.e("gotoAppDetails()--> permissions == null || permission.lenth == 0");
            return;
        }

        for (String permission : permissions) {
            if (TextUtils.isEmpty(permission)) {
                LogUtils.e("gotoAppDetails()--> TextUtils.isEmpty(permission)");
                return;
            }
        }

        isGoingToAppDetails = true;
        RuntimePermissionUtil.gotoAppDetails(activity, permissions, callback);
    }


    public void resultForFloatPermission() {
        if (mOnFloatPermissionRequestCallback != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(BCHelper.getInstance().getContext())) {
                mOnFloatPermissionRequestCallback.onFloatPermissionRequestResult(false);
            } else {
                mOnFloatPermissionRequestCallback.onFloatPermissionRequestResult(true);
            }
            mOnFloatPermissionRequestCallback = null;
        }
    }

    public void setOnFloatPermissionRequestCallback(OnFloatPermissionRequestCallback onFloatPermissionRequestCallback) {
        this.mOnFloatPermissionRequestCallback = onFloatPermissionRequestCallback;
    }

    public void resultForSysSettingsPermission() {
        if (mOnChangeSysSettingsPermissionRequestCallback != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(BCHelper.getInstance().getContext())) {
                mOnChangeSysSettingsPermissionRequestCallback.onChangeSysSettingsPermissionRequestResult(false);
            } else {
                mOnChangeSysSettingsPermissionRequestCallback.onChangeSysSettingsPermissionRequestResult(true);
            }
            mOnChangeSysSettingsPermissionRequestCallback = null;
        }
    }

    //    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestSysSettingsPermission(final Context context, OnChangeSysSettingsPermissionRequestCallback
            callback) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        Activity activity = AppManager.getAppManager().currentActivity();
        if (activity != null) {
            activity.startActivityForResult(intent, RuntimePermissionUtil.SYS_SETTING_PERMISSION_ACTIVITY_REQUEST_CODE);
            this.mOnChangeSysSettingsPermissionRequestCallback = callback;
            isGoingToAppDetails = true;
        } else {
            LogUtils.e("SwitchSetting -setSystemSettings-->activity == null");
        }
    }


    public boolean isNeedUpdateHardInfo() {
        return needUpdateHardInfo;
    }

    private void setNeedUpdateHardInfo(String[] permissions) {
        for (String permission : permissions) {
            if (TextUtils.equals(permission, android.Manifest.permission.READ_PHONE_STATE)) {
                needUpdateHardInfo = true;
            }
        }
    }

    public void clearUpdateHardInfoFlag() {
        this.needUpdateHardInfo = false;
    }

    public interface OnPermissionRequestResultCallback {
        /**
         * @param isGranted   权限是否授予
         * @param couldNotice 本次请求权限时是否能弹出系统弹框(用户选择不再提示后，申请权限时不会再弹框供用户打开权限),用户可根据此标识决定是否要跳转到设置页面或者其他操作
         **/
        void onPermissionRequestResult(boolean isGranted, boolean couldNotice);
    }

    /**
     * 悬浮窗权限请求回调
     **/
    public interface OnFloatPermissionRequestCallback {
        void onFloatPermissionRequestResult(boolean isGranted);
    }

    /**
     * 修改系统设置权限请求回调
     **/
    public interface OnChangeSysSettingsPermissionRequestCallback {
        void onChangeSysSettingsPermissionRequestResult(boolean isGranted);
    }
}
