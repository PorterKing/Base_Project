package com.porterking.netlibrary.work;

import android.util.Log;

import com.porterking.commonlibrary.manager.ProgressDialogManager;
import com.porterking.netlibrary.NetHelper;
import com.porterking.netlibrary.R;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public abstract class RxSubscribe<T> implements Observer<T> {
    private String msg;
    private Boolean showDialog = false;
    private Disposable mDisposable;

    public RxSubscribe() {
        this(NetHelper.getInstance().getContext().getResources().getString(R.string.net_process));
    }

    public RxSubscribe(String msg) {
        this.msg = msg;
    }

    public RxSubscribe(Boolean showDialog) {
        this(NetHelper.getInstance().getContext().getResources().getString(R.string.net_process),showDialog);
    }

    public RxSubscribe(String msg, Boolean showDialog) {
        this.msg = msg;
        this.showDialog = showDialog;
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        if (showDialog) {
            ProgressDialogManager.getInstance().show(msg);
        }
    }

    protected abstract void _onNext(T t);

    protected void _onError(String message) {
    }

    protected void _onCompleted() {
    }


    @Override
    public void onNext(T t) {
        ProgressDialogManager.getInstance().dismissDialog();
        _onNext(t);
    }

    @Override
    public void onComplete() {
        ProgressDialogManager.getInstance().dismissDialog();
        _onCompleted();
    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        ProgressDialogManager.getInstance().dismissDialog();
        if (!RxErrorIntercept.onErrorMsgDispatch(e,this)) {
            Log.e("error", e.toString() + "");
            _onError(NetHelper.getInstance().getContext().getResources().getString(R.string.net_error));
        }
    }

    /**
     * 取消订阅
     */
    public void disPose() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public boolean isDisposed() {
        return mDisposable != null && mDisposable.isDisposed();
    }


}
