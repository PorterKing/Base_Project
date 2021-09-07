package com.porterking.commonlibrary.view.dialog.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.View;
import android.widget.TextView;

import com.porterking.commonlibrary.R;

import androidx.annotation.NonNull;

/**
 * 描述：按钮对话框基类
 * Created by PorterKing on 2019/7/12.
 */
public abstract class BaseNormalDialog<T extends BaseNormalDialog> extends BaseDialog<T> {

    private static final int DEFAULT_VALUE = -1;

    protected TextView titleTv;
    protected TextView contentTv;
    protected TextView leftTv;
    protected TextView rightTv;
    protected TextView middleTv;

    protected CharSequence title;
    protected CharSequence content;
    protected CharSequence leftText;
    protected CharSequence rightText;
    protected CharSequence middleText;
    protected OnDialogClickListener leftListener;
    protected OnDialogClickListener rightListener;
    protected OnDialogClickListener middleListener;
    protected int btnLeftColor = DEFAULT_VALUE;
    protected int btnRightColor = DEFAULT_VALUE;
    protected int btnMiddleColor = DEFAULT_VALUE;
    protected Typeface tf_right;
    protected MovementMethod movementMethod;


    public BaseNormalDialog(@NonNull Context context) {
        super(context);

        init();
    }

    private void init() {
    }

    protected  void bindView(){
        if(customView == null) return;;
        titleTv = customView.findViewById(R.id.title_tv);
        contentTv = customView.findViewById(R.id.content_tv);
        leftTv = customView.findViewById(R.id.dialog_left_tv);
        rightTv = customView.findViewById(R.id.dialog_right_tv);
        middleTv = customView.findViewById(R.id.dialog_middle_tv);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(title);
        setContent(content);
        setRightText(rightText);
        setRightBold(tf_right);
        setMiddleText(middleText);
        setLeftText(leftText);
        setContentMovementMethod(movementMethod);
        OnDialogClickListener dialogListener = new OnDialogClickListener() {
            @Override
            public void onClick(BaseDialog dialog) {

            }
        };
        setRightListener(rightListener != null ? rightListener : dialogListener);
        setLeftListener(leftListener != null ? leftListener : dialogListener);
        setMiddleListener(middleListener != null ? middleListener : dialogListener);

        if (btnLeftColor != DEFAULT_VALUE) {
            setTextColor(leftTv, btnLeftColor);
        }
        if (btnRightColor != DEFAULT_VALUE) {
            setTextColor(rightTv, btnRightColor);
        }
        if (btnMiddleColor != DEFAULT_VALUE) {
            setTextColor(middleTv, btnMiddleColor);
        }
    }

    public T setLeftText(CharSequence leftText) {
        this.leftText = leftText;
        setText(leftTv, leftText, View.GONE);
        return (T) this;
    }

    public T setLeftTextColor(int color) {
        this.btnLeftColor = color;
        return (T) this;
    }

    public T setRightText(CharSequence rightText) {
        this.rightText = rightText;
        setText(rightTv, rightText, View.GONE);
        return (T) this;
    }

    public T setRightBold(Typeface typeface) {
        if (rightTv != null && typeface != null) {
            rightTv.setTypeface(typeface);
        }
        tf_right = typeface;
        return (T) this;
    }

    public T setRightTextColor(int color) {
        this.btnRightColor = color;
        return (T) this;
    }

    public T setMiddleText(CharSequence middleText) {
        this.middleText = middleText;
        setText(middleTv, middleText, View.GONE);
        return (T) this;
    }

    public T setMiddleTextColor(int color) {
        this.btnMiddleColor = color;
        return (T) this;
    }

    public T setLeftListener(OnDialogClickListener leftListener) {
        this.leftListener = leftListener;
        setClickListener(leftTv, leftListener);
        return (T) this;
    }

    public T setRightListener(OnDialogClickListener rightListener) {
        this.rightListener = rightListener;
        setClickListener(rightTv, rightListener);
        return (T) this;
    }

    public T setMiddleListener(OnDialogClickListener rightListener) {
        this.middleListener = rightListener;
        setClickListener(middleTv, middleListener);
        return (T) this;
    }

    public T setTitle(CharSequence title) {
        this.title = title;
        setText(titleTv, title, View.GONE);
        return (T) this;
    }

    public T setContent(CharSequence content) {
        this.content = content;
        setText(contentTv, content, View.GONE);
        return (T) this;
    }

    public T setContentMovementMethod(MovementMethod movementMethod) {
        this.movementMethod = movementMethod;
        if (contentTv != null && movementMethod != null) {
            contentTv.setMovementMethod(LinkMovementMethod.getInstance());
            contentTv.setHighlightColor(Color.TRANSPARENT);
        }
        return (T) this;
    }

}
