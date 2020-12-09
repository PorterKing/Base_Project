package com.jcb.base_corelibrary.view.titlebar;

import android.view.View;

import com.jcb.base_corelibrary.R;


/**
 * author: PorterKing
 * 保存titlebar模型
 */
public class TitleBarStruct {
    private View mLeftView = null;
    private View mMiddleView = null;
    private View mRightView = null;
    private boolean isTitleBarVisible = true;
    private boolean isTitleBarLeftVisible = true;
    private boolean isTitleBarMiddleVisible = true;
    private boolean isTitleBarRightVisible = true;
    private int backgroundColor = R.color.white;

    private String mTitle = null;

    public boolean isTitleBarLeftVisible() {
        return isTitleBarLeftVisible;
    }

    public boolean isTitleBarMiddleVisible() {
        return isTitleBarMiddleVisible;
    }


    public boolean isTitleBarRightVisible() {
        return isTitleBarRightVisible;
    }


    public void setTitleBarLeftVisible(boolean isTitleBarLeftVisible) {
        this.isTitleBarLeftVisible = isTitleBarLeftVisible;
    }


    public void setTitleBarMiddleVisible(boolean isTitleBarMiddleVisible) {
        this.isTitleBarMiddleVisible = isTitleBarMiddleVisible;
    }


    public void setTitleBarRightVisible(boolean isTitleBarRightVisible) {
        this.isTitleBarRightVisible = isTitleBarRightVisible;
    }

    public boolean isTitleBarVisible() {
        return isTitleBarVisible;
    }

    public void setTitleBarVisible(boolean isTitleBarVisible) {
        this.isTitleBarVisible = isTitleBarVisible;
    }

    public View getLeftView() {
        return mLeftView;
    }

    public View getMiddleView() {
        return mMiddleView;
    }

    public View getRightView() {
        return mRightView;
    }

    public void setLeftView(View mLeftView) {
        this.mLeftView = mLeftView;
    }

    public void setMiddleView(View mMiddleView) {
        this.mMiddleView = mMiddleView;
    }

    public void setRightView(View mRightView) {
        this.mRightView = mRightView;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
