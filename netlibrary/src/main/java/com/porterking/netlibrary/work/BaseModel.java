package com.porterking.netlibrary.work;

import java.io.Serializable;

/**
 * Created by Jam on 16-8-12
 * Description:
 */
public class BaseModel<T> implements Serializable {
    public String msg;
    public int code;
    public T data;


    public boolean success() {
        if (code == 200 || code ==0) {
            return true;
        }
        return false;
    }


}
