package com.xiaobaitiao.springbootinit.common;

import java.io.Serializable;
import java.util.HashMap;

import lombok.Data;

/**
 * 通用返回类
 *
 * @param <T>
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    private HashMap<String,Object> hashMap = new HashMap<>();
    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

    public BaseResponse(int code, T data, String message,HashMap<String,Object> hashMap) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.hashMap = hashMap;
    }
}
