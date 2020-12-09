package com.porterking.dblibrary.frame;

/**
 * author: porter_king
 * 数据库操作条件(where in ....)
 */
public interface Precondition {

    /**
     * 返回一个操作实例
     * @param constructor
     * @return
     */
    Precondition obtain(PrecConstructor constructor);

}
