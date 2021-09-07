package com.porterking.commonlibrary.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import org.greenrobot.eventbus.EventBus

/**
 * Created by Poterking on 19-7-9.
 */
abstract class BaseFragment<P : IBasePresenter> : Fragment(), IBaseView {

    protected var mContext: Context? = null
    protected var mPresenter: P? = null                                                   // P层的应用

    private var unbinder: Unbinder? = null                                                // 注册 ButterKnife 返回对象

    private var isInit = false
    private var isLoad = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        mPresenter = initPresenter()
        mPresenter?.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(getLayoutId(), container, false)
        unbinder = ButterKnife.bind(this, rootView)
        isInit = true
        initView();
        return rootView
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isCanLoadData()
    }

    override fun onResume() {
        super.onResume()
        isCanLoadData()
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private fun isCanLoadData() {
        if (!isInit) {
            return
        }
        if (userVisibleHint) {
            onForeground()
            isLoad = true
        } else {
            if (isLoad) {
                onBackground()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        isInit = false
        isLoad = false
        unbinder?.unbind()
        EventBus.getDefault().unregister(this)
    }

    /**
     * 获取布局id
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化
     */
    protected abstract fun initView()

    /**
     * 初始化Presenter
     */
    protected abstract fun initPresenter(): P

    /**
     * 页面展示
     */
    protected abstract fun onForeground()

    /**
     * 页面消失
     */
    protected abstract fun onBackground()



}
