package com.porterking.dblibrary.frame;

/**
 * author: porter_king
 * 插入回调
 */
public interface AddCallBack {

    /**
     *
     * @param id        插入的行数   -1表示插入失败
     * @param code      返回码
     * @param msg       返回信息
     */
    void callback(long id, int code, String msg);
}
