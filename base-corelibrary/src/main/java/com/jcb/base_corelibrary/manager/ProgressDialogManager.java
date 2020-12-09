package com.jcb.base_corelibrary.manager;


import com.jcb.base_corelibrary.view.dialog.ProgressDialog;

/**
 * 菊花dialog 管理
 * Created by Poterking on 19-8-13.
 */
public class ProgressDialogManager {

    private ProgressDialog dialog;

    public static final ProgressDialogManager getInstance() {
        return ProgressDialogManagerHelp.INTANCE;
    }

    private static class ProgressDialogManagerHelp {
        private static final ProgressDialogManager INTANCE = new ProgressDialogManager();
    }

    public void show(String msg) {
        dialog = new ProgressDialog(AppManager.getAppManager().currentActivity(), ProgressDialog.DIALOG_TYPE_NO_MESSAGE);
//        SpannableString spannableString = new SpannableString(msg);
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#666666"));
//        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        dialog.setTitle(spannableString);//设置对话框标题
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }


}
