package com.maoyu.bean.common;

import java.io.Serializable;

/**
 * 返回值类型
 *
 * @author maoyu [2018-10-05 22:33]
 **/
public class Result<T> implements Serializable {

    /**
     * 返回结果 - 成功或失败
     */
    private Boolean success;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 错误信息
     */
    private String message = "操作成功";

    /**
     * 错误代码
     */
    private Integer code;

    public Result() {
    }

    private Result(Boolean success, String message, Integer code, T data) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    /**
     * @param success
     * @param message
     * @param code
     * @param data
     * @return
     */
    public static Result build(Boolean success, String message, Integer code, Object data) {
        return new Result(success, message, code, data);
    }

    /**
     * 操作成功
     *
     * @param message
     * @param data
     * @return
     */
    public static Result buildSuccess(String message, Object data) {
        return build(Boolean.TRUE, message, 0, data);
    }

    /**
     * 操作成功带信息
     *
     * @param message
     * @return
     */
    public static Result buildSuccess(String message) {
        return buildSuccess(message, null);
    }

    /**
     * 操作成功带数据
     *
     * @param data
     * @return
     */
    public static Result buildSuccess(Object data) {
        return buildSuccess(null, data);
    }

    /**
     * 操作失败
     *
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static Result buildFailure(Integer code, String message, Object data) {
        return build(Boolean.FALSE, message, code, data);
    }

    /**
     * 操作失败
     *
     * @param message
     * @param data
     * @return
     */
    public static Result buildFailure(String message, Object data) {
        return buildFailure(0, message, data);
    }

    /**
     * 操作失败带信息
     *
     * @param message
     * @return
     */
    public static Result buildFailure(String message) {
        return buildFailure(message, null);
    }

    /**
     * 操作失败带结果
     *
     * @param data
     * @return
     */
    public static Result buildFailure(Object data) {
        return buildFailure("", data);
    }

    public Boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
