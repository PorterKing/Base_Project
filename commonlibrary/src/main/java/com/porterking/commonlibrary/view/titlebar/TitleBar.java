package com.porterking.commonlibrary.view.titlebar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.porterking.commonlibrary.R;
import com.porterking.commonlibrary.utils.CommonUtils;

import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * author: PorterKing
 * 标题栏
 */
public class TitleBar extends RelativeLayout {

    private Context context;

    private Unbinder unbinder;

    LinearLayout leftContainer;
    LinearLayout rightContainer;
    LinearLayout middleContainer;

    private OnClickListener backListener;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        unbinder = ButterKnife.bind(this);
        leftContainer = findViewById(R.id.include_title_bar_leftview_ll);
        rightContainer = findViewById(R.id.include_title_bar_rightview_ll);
        middleContainer = findViewById(R.id.include_title_bar_middleview_ll);

    }


    public TitleBar(Context context) {
        super(context);
        this.context = context;
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setTitleBarStruct(TitleBarStruct titleBarStruct, String title) {
        if (titleBarStruct == null) {
            // 填充默认
            setVisibility(View.VISIBLE);
            setDefaultTitle(title);
            return;
        } else {
            setVisibility(View.VISIBLE);
            setTitleByTitleStruct(titleBarStruct, title);
        }
    }

    private void setTitleByTitleStruct(TitleBarStruct titleBarStruct, String title) {
        leftContainer.removeAllViews();
        middleContainer.removeAllViews();
        rightContainer.removeAllViews();
        if (title == null) {
            title = "";
        }
        View leftView = getLeftView(titleBarStruct);
        View middView = getMiddleView(titleBarStruct, title);
        View rightView = titleBarStruct.getRightView();
        if (titleBarStruct.isTitleBarLeftVisible())
            leftContainer.addView(leftView);
        if (titleBarStruct.isTitleBarMiddleVisible())
            middleContainer.addView(middView);
        if (rightView != null && titleBarStruct.isTitleBarRightVisible()) {
            rightContainer.addView(rightView);
        }

    }

    public void setTitle(String title) {
        TextView textView = findViewById(R.id.title_bar_title);
        if (!TextUtils.isEmpty(title) && textView != null) {
            textView.setText(title);
        }
    }

    private void setDefaultTitle(String title) {
        leftContainer.removeAllViews();
        middleContainer.removeAllViews();
        rightContainer.removeAllViews();
        if (title == null) {
            title = "";
        }
        View leftView = getLeftView(null);
        View middView = getMiddleView(null, title);
        leftContainer.addView(leftView);
        middleContainer.addView(middView);
    }

    /**
     * 获取左边的View
     *
     * @param titleBarStruct
     * @return
     */
    private View getLeftView(TitleBarStruct titleBarStruct) {
        View view = null;
        if (titleBarStruct != null)
            view = titleBarStruct.getLeftView();
        if (view == null) {
            // 创建默认的leftView

            view = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER;
            ((ImageView) view).setImageResource(R.drawable.titlebar_back_icon);
            view.setLayoutParams(layoutParams);
            view.setBackgroundResource(R.drawable.titlebar_item_bg);
            int padding = CommonUtils.dip2px(context, 16);
            view.setPadding(padding, 0, padding, 0);
            leftContainer.setOnClickListener(backListener);
        }

        return view;
    }

    /**
     * 获取中间的View
     *
     * @param titleBarStruct
     * @return
     */
    private View getMiddleView(TitleBarStruct titleBarStruct, String title) {
        View view = null;
        String priorityTitle = null;
        if (titleBarStruct != null) {
            view = titleBarStruct.getMiddleView();
            priorityTitle = titleBarStruct.getTitle();
        }
        if (view == null) {
            // 创建默认的middleView
            if (!TextUtils.isEmpty(priorityTitle)) {
                title = priorityTitle;
            }

            TextView textView = new TextView(getContext());
            textView.setTextColor(ContextCompat.getColor(context, R.color.textColorMajor));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.font_large));
            textView.setId(R.id.title_bar_title);
            textView.setGravity(Gravity.CENTER);
            textView.setText(title);
            view = textView;
        }

        return view;
    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public OnClickListener getBackListener() {
        return backListener;
    }

    public void setBackListener(OnClickListener backListener) {
        this.backListener = backListener;
    }
}
