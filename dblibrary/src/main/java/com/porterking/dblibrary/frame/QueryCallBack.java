package com.porterking.dblibrary.frame;

import java.util.List;

/**
 * author: porter_king
 * 查询回调
 * @param <T>
 */
public interface QueryCallBack<T> {

    /**
     *
     * @param result    查询返回的list
     * @param code      返回码
     * @param msg       返回信息
     */
    void callback(List<T> result, int code, String msg);
}
