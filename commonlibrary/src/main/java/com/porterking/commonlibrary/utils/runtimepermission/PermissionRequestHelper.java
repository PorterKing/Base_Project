package com.porterking.commonlibrary.utils.runtimepermission;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.provider.Settings;

import com.porterking.commonlibrary.view.dialog.NormalDialog;
import com.porterking.commonlibrary.view.dialog.base.BaseDialog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 权限管理帮助类
 * Created by shenjie on 2018/4/18.
 */

public class PermissionRequestHelper {

    /**
     * 权限请求
     *
     * @param activity
     * @param permissions
     * @param content
     * @param callback
     */
    public static void requestPermission(Activity activity, String permissions, String content,
                                         PermissionGrantedCallback callback) {
        requestPermission(activity, new String[]{permissions}, content, callback);
    }

    /**
     * 多个权限请求
     *
     * @param activity
     * @param permissions
     * @param content
     * @param callback
     */
    public static void requestPermission(final Activity activity, final String[] permissions, final String content,
                                         final PermissionGrantedCallback callback) {
        if (activity == null) {
            return;
        }
        //android6.0以后才执行权限动态申请
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (callback != null) {
                callback.granted();
            }
            return;
        }
        //筛选掉已经获取到的权限，预防小米旧版多个权限单一允许，请求后会弹已允许的权限弹窗的bug
        final String[] permissionList = RuntimePermissionUtil.getPermissionsArray(activity, permissions);

        //如果有权限则直接跳过后续步骤
        if (permissionList == null || permissionList.length == 0) {
            if (callback != null) {
                callback.granted();
            }
            return;
        }

        PermissionRequestManager.getmInstance().requestPermission(permissionList, activity, (isGranted, couldNotice) -> {
            if (isGranted) {
                if (callback != null) {
                    callback.granted();
                }
            } else {
                showGotoDetailDialog(activity, permissionList, content, callback);
            }
        });
    }

    public static void showGotoDetailDialog(final Activity activity, final String[] permission, final String content,
                                            final PermissionGrantedCallback callback) {
        new NormalDialog(activity).setTitle("提示").setContent(content).setLeftText("取消")
                .setRightText("去设置").setLeftListener(new BaseDialog.OnDialogClickListener() {
            @Override
            public void onClick(BaseDialog dialog) {
                if (callback != null) {
                    callback.deny();
                }
            }
        }).setRightListener(new BaseDialog.OnDialogClickListener() {
            @Override
            public void onClick(BaseDialog dialog) {
                PermissionRequestManager.getmInstance().gotoAppDetailsForPermission(activity, permission,
                        (isGranted, couldNotice) -> {
                            if (isGranted) {
                                if (callback != null) {
                                    callback.granted();
                                }
                            } else {
                                showGotoDetailDialog(activity, permission, content, callback);
                            }
                        });
            }
        }).show();

    }

    /**
     * 请求系统设置修改权限（特殊权限）
     *
     * @param context
     * @param content
     * @param callback
     */
    public static void requestWriteSystemPermission(final Context context, final String content, final
    PermissionGrantedCallback callback) {
        if (content == null) {
            return;
        }
        //android6.0以后才执行权限动态申请
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (callback != null) {
                callback.granted();
            }
            return;
        }
        //如果有权限则直接跳过后续步骤
        if (Settings.System.canWrite(context)) {
            if (callback != null) {
                callback.granted();
            }
            return;
        }

        new NormalDialog(context).setTitle("提示").setContent(content).setLeftText("取消")
                .setRightText("去设置").setLeftListener(new BaseDialog.OnDialogClickListener() {
            @Override
            public void onClick(BaseDialog dialog) {
                if (callback != null) {
                    callback.deny();
                }
            }
        }).setRightListener(new BaseDialog.OnDialogClickListener() {
            @Override
            public void onClick(BaseDialog dialog) {
                PermissionRequestManager.getmInstance().requestSysSettingsPermission(context, isGranted -> {
                    if (callback != null) {
                        if (isGranted) {
                            callback.granted();
                        } else {
                            callback.deny();
                        }
                    }
                });
            }
        }).show();
    }

    /**
     * 请求悬浮窗权限（特殊权限）
     *
     * @param activity
     * @param content
     * @param callback
     */
    public static void requestWindowPermission(final Activity activity, final String content, final
    PermissionGrantedCallback callback) {
        if (activity == null) {
            return;
        }
        //android6.0以后才执行权限动态申请
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (callback != null) {
                callback.granted();
            }
            return;
        }
        //如果有权限则直接跳过后续步骤
        if (Settings.canDrawOverlays(activity)) {
            if (callback != null) {
                callback.granted();
            }
            return;
        }

        new NormalDialog(activity).setTitle("提示").setContent(content).setLeftText("取消")
                .setRightText("去设置").setLeftListener(new BaseDialog.OnDialogClickListener() {
            @Override
            public void onClick(BaseDialog dialog) {
                if (callback != null) {
                    callback.deny();
                }
            }
        }).setRightListener(new BaseDialog.OnDialogClickListener() {
            @Override
            public void onClick(BaseDialog dialog) {
                PermissionRequestManager.getmInstance().doSomethingWhenNeedWindowPermission(activity, isGranted -> {
                    if (callback != null) {
                        if (isGranted) {
                            callback.granted();
                        } else {
                            callback.deny();
                        }
                    }
                });
            }
        }).show();
    }

    /**
     * 检查通知栏权限是否开启
     * 参考SupportCompat包中的方法： NotificationManagerCompat.from(context).areNotificationsEnabled();
     *
     * @return 该权限默认情况下打开，所以默认返回true
     */
    public static boolean checkNotificationPermission(Context context) {
        if (context == null) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            return manager == null || manager.areNotificationsEnabled();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE,
                        String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return (Integer) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg) == 0;
            } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException |
                    IllegalAccessException | RuntimeException | ClassNotFoundException ignored) {
                return true;
            }
        } else {
            return true;
        }
    }

    public interface PermissionGrantedCallback {
        void granted();

        void deny();
    }
}
