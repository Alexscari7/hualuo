package com.alex.wx.hualuo.model.result;

//import lombok.Data;

/**
 * @author wusd
 * @description 空
 * @create 2021/03/17 15:09
 */
public class Result<T> {
    private String code;
    private String msg;
    private T data;

    public Result() {
    }

    public Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Result<T> succeed() {
        return succeed("200", "请求成功", null);
    }

    public static <T> Result<T> succeed(T data) {
        return succeed("200", "请求成功", data);
    }

    public static <T> Result<T> succeed(String msg) {
        return succeed("200", msg, null);
    }

    public static <T> Result<T> succeed(String msg, T data) {
        return succeed("200", msg, data);
    }

    public static <T> Result<T> succeed(String code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    public static <T> Result<T> failed() {
        return succeed("500", "请求失败", null);
    }

    public static <T> Result<T> failed(T data) {
        return succeed("500", "请求失败", data);
    }

    public static <T> Result<T> failed(String msg) {
        return succeed("500", msg, null);
    }

    public static <T> Result<T> failed(String msg, T data) {
        return succeed("500", msg, data);
    }

    public static <T> Result<T> failed(String code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

}
