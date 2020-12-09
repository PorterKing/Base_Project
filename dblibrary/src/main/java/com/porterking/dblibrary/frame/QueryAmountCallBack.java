package com.porterking.dblibrary.frame;

/**
 * author: porter_king
 *  查询数量
 */
public interface QueryAmountCallBack {

    /**
     *
     * @param amount    数量
     * @param code      返回码
     * @param msg       返回信息
     */
    void callback(long amount, int code, String msg);
}
