

package com.porterking.commonlibrary.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.porterking.commonlibrary.R;


/**
 * 描述：在底部出现，显示少量信息的等待框
 *
 * @author 谢秀杰
 */
public class ProgressDialog extends Dialog {

    public static final int DIALLG_TYPE_WITH_MESSAGE = R.style.HXProgressDialogStyle;
    public static final int DIALOG_TYPE_NO_MESSAGE = R.style.HXNoMessageDialogStyle;
    public static final int DIALOG_TYPE_WIDTH_PROGRESSBAR = R.style.HXProgressBarDialogStyle;

    /**
     * Toast显示位置距离底部的偏移量
     */
    public static final int OFFSET = 50;

    public static final int ICONHEIGHT = 33;
    /**
     * 默认为有背景框，可以添加加载信息的对话框
     */
    private int dialogType = DIALLG_TYPE_WITH_MESSAGE;

    /**
     * 等待图片转动一次的时间片
     */
    private static final int ROTATE_SPAN = 1000;

    /**
     * 等待框显示界面
     */
    private View progressRootView;

    /**
     * 等待图片
     */
    private ImageView loading;

    private Handler handler = new Handler();

    private ProcessDialogDismissListener dialogDismissListener;



    public ProgressDialog(Context context) {
        super(context, R.style.HXProgressDialogStyle);
        LayoutParams lp = new LayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.gravity = Gravity.BOTTOM;
        // 考虑手机的屏幕密度
        lp.y = (int) (OFFSET * dm.density);
        lp.flags = LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.5f;
        lp.width = LayoutParams.WRAP_CONTENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.type = LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        getWindow().setAttributes(lp);
        progressRootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.dialog_view_progress, null);
        loading = (ImageView) progressRootView.findViewById(R.id.icon);

        setCanceledOnTouchOutside(true);
        setContentView(progressRootView);
    }

    /**
     * dialogType:
     * DIALLG_TYPE_WITH_MESSAGE  显示在底部，可以设置message，背景会变暗
     * DIALOG_TYPE_NO_MESSAGE    显示在中间，不可以设置message，背景不会变暗
     * DIALOG_TYPE_WIDTH_PROGRESSBAR 显示在中间，上边有loading，下边message
     * 构造函数
     */
    public ProgressDialog(Context context, int style) {
        super(context, (style == DIALLG_TYPE_WITH_MESSAGE || style == DIALOG_TYPE_NO_MESSAGE) ? style : DIALLG_TYPE_WITH_MESSAGE);
        this.dialogType = style;
        LayoutParams lp = new LayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);

        if (this.dialogType == DIALOG_TYPE_NO_MESSAGE || this.dialogType == DIALOG_TYPE_WIDTH_PROGRESSBAR) {
            lp.gravity = Gravity.CENTER;
            lp.dimAmount = 0f;
            lp.y = -(int) (ICONHEIGHT * dm.density / 2);
        } else {
            lp.gravity = Gravity.BOTTOM;
            // 考虑手机的屏幕密度
            lp.y = (int) (OFFSET * dm.density);
            lp.dimAmount = 0.5f;
        }
        // 考虑手机的屏幕密度
        lp.flags = LayoutParams.FLAG_DIM_BEHIND;
        lp.width = LayoutParams.WRAP_CONTENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.type = LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        getWindow().setAttributes(lp);
        if (this.dialogType == DIALOG_TYPE_NO_MESSAGE) {
            progressRootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.dialog_view_progress, null);
            progressRootView.setBackgroundResource(0);
        } else if (this.dialogType == DIALOG_TYPE_WIDTH_PROGRESSBAR) {
            progressRootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.dialog_view_progressbar, null);
            progressRootView.setBackgroundResource(0);
        } else {
            progressRootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.dialog_view_progress, null);
        }
        loading = (ImageView) progressRootView.findViewById(R.id.icon);


        setCanceledOnTouchOutside(true);
        setContentView(progressRootView);
    }

    /**
     * 设置提示信息
     *
     * @param message
     */
    public void setMessage(String message) {
        if (dialogType == DIALOG_TYPE_NO_MESSAGE) {
            return;
        }
        ((TextView) progressRootView.findViewById(R.id.message)).setVisibility(View.VISIBLE);
        ((TextView) progressRootView.findViewById(R.id.message)).setText(message);
    }

    public void setMessage(String message, int gravityType) {
        if (dialogType == DIALOG_TYPE_NO_MESSAGE) {
            return;
        }
        TextView textView = (TextView) progressRootView.findViewById(R.id.message);
        textView.setVisibility(View.VISIBLE);
        textView.setGravity(gravityType);
        textView.setText(message);
    }

    public void setLoadingVisiable(int visiable) {
        if (loading != null) {
            loading.setVisibility(visiable);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (loading != null) {
            Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
            LinearInterpolator lin = new LinearInterpolator();
            operatingAnim.setInterpolator(lin);
            loading.startAnimation(operatingAnim);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        onRemove();
    }

    public void onRemove() {
        if (loading != null) {
            loading.clearAnimation();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (dialogDismissListener != null) {
                dialogDismissListener.onDialogDismiss();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void registerDialogDismissListener(ProcessDialogDismissListener dialogDismissListener) {
        this.dialogDismissListener = dialogDismissListener;
    }

    public void unregisterDialogDismissListener(ProcessDialogDismissListener dialogDismissListener) {
        this.dialogDismissListener = null;
    }


    /**
     * 进度条消失的监听器
     *
     * @author zhaoyh
     */
    public interface ProcessDialogDismissListener {
        public void onDialogDismiss();
    }
}
