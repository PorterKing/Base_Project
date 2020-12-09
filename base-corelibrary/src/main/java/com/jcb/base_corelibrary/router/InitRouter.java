package com.jcb.base_corelibrary.router;

import android.content.Context;


/**
 * Created by PorterKing on 2017/4/14.
 */

public class InitRouter {
    public static String ACTIVITY_MAIN                    =   "YSActivity/";

    /**
     * 配置url对应的跳转Acivity,eg 在Activity调用中Router.sharedRouter().open("case/16");
     */
    public static void initRouter(Context context) {
        // Set the global context
       Router.sharedRouter().setContext(context);
    }
}
