package com.jcb.base_corelibrary.ui

/**
 * Created by Poterking on 19-7-10.
 */
abstract class BasePresenter<V : IBaseView> : IBasePresenter {

    protected lateinit var mView: V

    override fun attachView(view: IBaseView) {
        mView = view as V
    }
}
