package com.porterking.dblibrary.assit;

/**
 * author: porter_king
 * 数据库自定义异常
 */
public class CustomException extends Exception {


    public static final int CODE_3000 = -3000;                     //表示其他异常
    public static final int CODE_3001 = -3001;                     //表示该表不符合规则，没有字段信息
    public static final int CODE_3002 = -3002;                     //表示该表未设置主键
    public static final int CODE_3003 = -3003;                     //表示主键不是long类型
    public static final int CODE_3004 = -3004;                     //数据库初始化失败
    public static final int CODE_3005 = -3005;                     //创建或更新表失败
    public static final int CODE_3006 = -3006;                     //插入表失败
    public static final int CODE_3007 = -3007;                     //修改或删除表失败
    public static final int CODE_3008 = -3008;                     //查询失败

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误信息
     */
    private String msg;

    public CustomException(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
