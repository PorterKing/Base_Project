package com.porterking.netlibrary.work;

import android.util.Log;

import com.porterking.netlibrary.NetHelper;
import com.porterking.netlibrary.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

/**
 * Created by Poterking on 19-8-23.
 */
public class RxErrorIntercept {


    public static boolean onErrorMsgDispatch(Throwable e, RxSubscribe rxSubscribe) {
        if (!NetWorkUtil.isNetWork()) {
            rxSubscribe._onError(NetHelper.getInstance().getContext().getResources().getString(R.string.net_unavailable));
//            Toast.makeText(AppManager.getAppManager().currentActivity(), NetHelper.getInstance().getContext().getResources().getString(R.string.net_unavailable), Toast.LENGTH_SHORT).show();
            Log.e("No sNetWork:", e.getMessage() + "");
            return true;
        } else if (e instanceof RxException.ServerException) {
            rxSubscribe._onError(e.getMessage());
            Log.e("ServerException", e.getMessage() + "");
            return true;
        } else if (e instanceof RxException.LoginFailException) {
            Log.e("LoginFailException", e.getMessage() + "");
            return true;
        } else if (e instanceof RxException.LowVersionException) {
            Log.e("LowVersionException", e.getMessage() + "");
            return true;
        } else if (e instanceof SocketTimeoutException) {
            rxSubscribe._onError(NetHelper.getInstance().getContext().getResources().getString(R.string.net_time_out));
            Log.e("SocketTimeoutException", e.getMessage() + "");
            return true;
        } else if (e instanceof FileNotFoundException) {
            rxSubscribe._onError(NetHelper.getInstance().getContext().getResources().getString(R.string.file_not_found));
            Log.e("FileNotFoundException", e.getMessage() + "");
            return true;
        } else if (e instanceof IOException) {
            rxSubscribe._onError(NetHelper.getInstance().getContext().getResources().getString(R.string.io_error));
            Log.e("IOException", e.getMessage() + "");
            return true;
        } else if (e instanceof HttpException) {
            rxSubscribe._onError("网络链接404！");
            Log.e("HttpException", e.getMessage() + "");
            return true;
        }
        return false;
    }



}
