package com.jcb.base_corelibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.jcb.base_corelibrary.BCHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {


    /**
     * @param s
     * @return
     * @throws ParseException
     */
    public static String dateToStamp(String s) {
        String res = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = simpleDateFormat.parse(s);
            long ts = date.getTime() / 1000;
            res = String.valueOf(ts);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }



    public static String formatTime(String time, String pattern) {
        if (isDigital(time)) {
            long timel = Long.parseLong(time);
            Date date = new Date(timel);
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            return dateFormat.format(date);
        }
        return "";
    }


    /**
     * 是否为全数字
     *
     * @param string 待测字符串
     * @return 返回true-是，false-否
     */
    public static boolean isDigital(String string) {
        if (string != null) {
            Pattern digitalPattern = Pattern.compile("[0-9]+");
            Matcher matcher = digitalPattern.matcher(string);
            return matcher.matches();
        }
        return false;
    }


    /**
     * 获取APP版本号
     *
     * @return
     */
    public static String getAPPVersion() {
        String version = "";
        try {
            PackageManager packageManager = BCHelper.getInstance().getContext().getPackageManager();
            PackageInfo pageInfo = packageManager.getPackageInfo(BCHelper.getInstance().getContext().getPackageName(), 0);
            version = pageInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 校验手机号码
     *
     * @param phonenumber
     * @return
     */

    public static boolean checkMobilePhonenumber(String phonenumber) {
        if (TextUtils.isEmpty(phonenumber)) {
            return false;
        }
        String patternString = "^1\\d{10}$";
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }



    /**
     * 校验url合法性
     *
     * @param urlString
     * @return
     */
    public static boolean isUrlValid(String urlString) {
        if (TextUtils.isEmpty(urlString))
            return false;
        try {
            URL url = new URL(urlString);
            return URLUtil.isValidUrl(urlString);
        } catch (MalformedURLException e) {

        }
        return false;
    }


    /**
     * 获取随机字符串
     * @return
     */
    public static String getRandomString() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 11; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


}
