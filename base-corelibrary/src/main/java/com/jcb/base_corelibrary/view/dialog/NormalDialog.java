package com.jcb.base_corelibrary.view.dialog;


import android.content.Context;

import com.jcb.base_corelibrary.BCHelper;
import com.jcb.base_corelibrary.R;
import com.jcb.base_corelibrary.view.dialog.base.BaseDialog;
import com.jcb.base_corelibrary.view.dialog.base.BaseNormalDialog;

import androidx.annotation.NonNull;


/**
 * 描述：desc:带1个/2个/3个按钮对话框
 * Created by PorterKing on 2019/7/12.
 */

public class NormalDialog extends BaseNormalDialog<NormalDialog> {

    private static int STYLE_DEFAULT = STYLE_ALERT;

    public NormalDialog(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        setStyle(STYLE_DEFAULT);
        setCustomView(R.layout.dialog_normal);
        setRightText(BCHelper.getInstance().getContext().getResources().getString(R.string.dialog_normal_right_btn));
        setLeftText(BCHelper.getInstance().getContext().getResources().getString(R.string.dialog_normal_left_btn));
        setLeftListener(new OnDialogClickListener() {
            @Override
            public void onClick(BaseDialog dialog) {
                dismiss();
            }
        });
    }


}
