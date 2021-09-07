package com.porterking.commonlibrary.view.dialog;

import android.content.Context;

import com.porterking.commonlibrary.BCHelper;
import com.porterking.commonlibrary.R;
import com.porterking.commonlibrary.view.dialog.base.BaseDialog;
import com.porterking.commonlibrary.view.dialog.base.BaseNormalDialog;

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
