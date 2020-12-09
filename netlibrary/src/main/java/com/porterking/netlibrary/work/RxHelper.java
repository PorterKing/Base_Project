package com.porterking.netlibrary.work;


import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxHelper {
    /**
     * d
     * 对结果进行预处理
     */
    public static <T> ObservableTransformer<BaseModel<T>, T> handleResult() {
        return upstream -> upstream.flatMap((Function<BaseModel<T>, Observable<T>>) result -> {
            if (result.success()) {
                return createData(result.data);
            } else {
                return Observable.error(new RxException.ServerException(result.msg));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 创建成功的数据
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(emitter -> {
            try {
                emitter.onNext(data);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

    }


    public static <T> Observer<T> emptySubscribe() {
        return new RxSubscribe<T>() {
            @Override
            protected void _onNext(T object) {

            }
        };
    }


}
