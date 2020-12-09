package com.porterking.base;

import com.jcb.base_corelibrary.ui.BaseActivity;
import com.jcb.base_corelibrary.ui.IBasePresenter;

public class MainActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected IBasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {

    }
}