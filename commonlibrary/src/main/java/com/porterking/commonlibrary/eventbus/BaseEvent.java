package com.porterking.commonlibrary.eventbus;

import java.util.HashMap;

/**
 * Created by Poterking on 19-7-20.
 */
public class BaseEvent {

    private int id;
    private String msg;

    public BaseEvent(int id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    private HashMap<String,Object> mParam;

    public HashMap<String, Object> getParam() {
        return mParam;
    }

    public void setParam(HashMap<String, Object> mParam) {
        this.mParam = mParam;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
