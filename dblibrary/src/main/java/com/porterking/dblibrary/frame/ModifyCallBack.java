package com.porterking.dblibrary.frame;

/**
 * author: porter_king
 * 修改回调
 */
public interface ModifyCallBack {

    /**
     *
     * @param row           影响的行数
     * @param code          返回码
     * @param msg           返回信息
     */
    void callback(long row, int code, String msg);
}
