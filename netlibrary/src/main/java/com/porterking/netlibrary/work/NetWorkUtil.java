package com.porterking.netlibrary.work;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.jcb.base_corelibrary.utils.runtimepermission.RuntimePermissionUtil;
import com.porterking.netlibrary.NetHelper;


/**
 * Created by Poterking on 19-7-10.
 */
public class NetWorkUtil {

    /**
     * 判断是否有网络，不返回网络类型 false 不可用
     *
     * @return
     */
    public static boolean isNetWork() {

        // 得到系统服务，网络管理器
        ConnectivityManager manager = (ConnectivityManager) NetHelper.getInstance().getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();  // 获得激活的网络信息
        if (info != null) {
            boolean isavailable = info.isAvailable(); // 当前网络是否有效
            boolean isconnected = info.isConnected(); // 当前网络是否已连接
            if (isavailable && isconnected) {
                //网络是可用的
                return true;
            } else {
                //网络不可用
                return false;
            }
        }
        return false;
    }


    /**
     * 网络信息
     *
     * @return 网络信息
     */
    public static String networkInfo() {
        String info = "unknown";

        ConnectivityManager connectivityManager = (ConnectivityManager) NetHelper.getInstance().getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    info = networkInfo.getTypeName();
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(networkInfo.getTypeName());
                    sb.append(" [");
                    String networkOperatorName = getNetworkOperatorName();
                    if (!TextUtils.isEmpty(networkOperatorName)) {
                        sb.append(networkOperatorName);
                        sb.append("#");
                    }
                    sb.append(networkInfo.getSubtypeName());
                    sb.append("]");
                    info = sb.toString();
                }
            }
        }

        return info;
    }

    private static String getNetworkOperatorName() {
        if (RuntimePermissionUtil.checkSelfPermission(NetHelper.getInstance().getContext(), (Manifest.permission
                .READ_PHONE_STATE))) {
            TelephonyManager telephonyManager = (TelephonyManager) NetHelper.getInstance().getContext().getSystemService
                    (Context.TELEPHONY_SERVICE);
            return telephonyManager.getNetworkOperatorName();
        } else {
            return "unknown";
        }
    }


}
