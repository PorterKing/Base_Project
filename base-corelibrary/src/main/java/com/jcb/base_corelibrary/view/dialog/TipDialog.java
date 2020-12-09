package com.jcb.base_corelibrary.view.dialog;

import android.content.Context;

import com.jcb.base_corelibrary.BCHelper;
import com.jcb.base_corelibrary.R;
import com.jcb.base_corelibrary.view.dialog.base.BaseDialog;
import com.jcb.base_corelibrary.view.dialog.base.BaseNormalDialog;

import androidx.annotation.NonNull;

/**
 * Created by Poterking on 19-7-18.
 */
public class TipDialog extends BaseNormalDialog<TipDialog> {


    public TipDialog(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        setStyle(STYLE_ALERT);
        setCustomView(R.layout.dialog_normal);
        setMiddleText(BCHelper.getInstance().getContext().getResources().getString(R.string.dialog_normal_right_btn));
        setMiddleListener(new OnDialogClickListener() {
            @Override
            public void onClick(BaseDialog dialog) {
                dismiss();
            }
        });
    }

}
