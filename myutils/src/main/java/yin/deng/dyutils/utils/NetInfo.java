package yin.deng.dyutils.utils;


import yin.deng.dyutils.http.BaseHttpInfo;

/**
 * Created by Administrator on 2017/4/12.
 */

public class NetInfo extends BaseHttpInfo {
    private int netState;
    private String msg;
    /**
     * message : 您已投票,不能再次投票
     * success : false
     * total : 0
     */

    private String message;
    private boolean success;
    private int total;


    public NetInfo(int netState, String msg) {
        this.netState = netState;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public NetInfo(int netState) {
        this.netState = netState;
    }

    public int getNetState() {
        return netState;
    }

    public void setNetState(int netState) {
        this.netState = netState;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
