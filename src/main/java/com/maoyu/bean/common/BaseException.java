package com.maoyu.bean.common;

/**
 * @author maoyu [2018-10-05 23:18]
 **/
public class BaseException extends RuntimeException {

    /**
     * 错误代码
     */
    private Integer code = 0;

    /**
     * 错误信息
     */
    private String message;

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
