package com.edinburgh.ewireless.Class.HTTP;

/**
 * Author: yijianzheng
 * Date: 23/03/2023 17:18
 *
 * Notes:
 */
public class ResponseMessage {

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
}
