package com.porterking.dblibrary.frame;

/**
 * author: porter_king
 * 操作后的回调，返回相应信息
 */
public interface DBCallBack {

    int SUCCESS_CODE        = 0;
    String SUCCESS_MSG      = "operation is successful";

    /**
     *
     * @param code      返回码
     * @param msg       返回信息
     */
    void callback(int code, String msg);
}
