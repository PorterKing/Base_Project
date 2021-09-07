package com.porterking.commonlibrary.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import com.porterking.commonlibrary.R
import com.porterking.commonlibrary.manager.AppManager
import com.porterking.commonlibrary.manager.ProgressDialogManager
import com.porterking.commonlibrary.view.titlebar.ITitleBar
import com.porterking.commonlibrary.view.titlebar.TitleBar
import com.porterking.commonlibrary.view.titlebar.TitleBarStruct
import com.yinshifinance.ths.utils.ScreenAdaptUtil


import org.greenrobot.eventbus.EventBus

/**
 * Created by Poterking on 19-7-10.
 */
abstract class BaseActivity<P : IBasePresenter> : AppCompatActivity(), IBaseView, ITitleBar {

    protected val TAG = "BaseActivity"

    protected var mPresenter: P? = null                                                   // P层的应用

    private var unbinder: Unbinder? = null                                                // 注册 ButterKnife 返回对象


    override fun onCreate(savedInstanceState: Bundle?) {
        ScreenAdaptUtil.setCustomDensity(this, application)
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(getLayoutId())
        AppManager.getAppManager().addActivity(this)

        //注册 ButterKnife 返回 Unbinder对象 为了释放资源调用
        unbinder = ButterKnife.bind(this)

        mPresenter = initPresenter()
        mPresenter?.attachView(this)
        notifyTitleBar(titleBarStruct)
        initView()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    /**
     * 获取布局id
     */

    protected abstract fun getLayoutId(): Int

    /**
     * 初始化mPresenter
     */
    protected abstract fun initPresenter(): P?

    /**
     * 初始化
     */
    protected abstract fun initView()


    override fun getTitleBarStruct(): TitleBarStruct? {
        return null
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (unbinder != null) {
            unbinder!!.unbind()
        }
        ProgressDialogManager.getInstance().dismissDialog()
        EventBus.getDefault().unregister(this)
        AppManager.getAppManager().finishActivity(this)
    }


    /**
     * 更新TitleBar
     * @param titleBarStruct
     */
    protected fun notifyTitleBar(titleBarStruct: TitleBarStruct?) {
        val actionBar = supportActionBar

        if (titleBarStruct == null || actionBar == null)
            return

        if (!titleBarStruct.isTitleBarVisible) {
            dismissTitleBar()
            return
        }
        val layoutParams = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER)
        val titleBar = LayoutInflater.from(this).inflate(R.layout.include_title_bar, null) as TitleBar
        titleBar.backListener = if (getBackListener() == null) View.OnClickListener { finish()} else getBackListener()
        titleBar.setTitleBarStruct(titleBarStruct, title.toString())

        actionBar.setCustomView(titleBar, layoutParams)
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayShowHomeEnabled(false)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setBackgroundDrawable(resources.getDrawable(titleBarStruct.backgroundColor))

        showTitleBar()
    }

    protected open fun getBackListener(): View.OnClickListener? {
        return null
    }

    /**
     * 显示TitleBar
     */
    @SuppressLint("RestrictedApi")
    fun showTitleBar() {
        val actionBar = supportActionBar
        actionBar?.setShowHideAnimationEnabled(false)
        actionBar?.show()
    }

    /**
     * 隐藏TitleBar
     */
    @SuppressLint("RestrictedApi")
    fun dismissTitleBar() {
        val actionBar = supportActionBar
        actionBar?.setShowHideAnimationEnabled(false)
        actionBar?.hide()
    }
}
