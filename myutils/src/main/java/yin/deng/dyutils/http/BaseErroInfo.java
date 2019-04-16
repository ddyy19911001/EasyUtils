package yin.deng.dyutils.http;

/**
 * Created by Administrator on 2019/3/29 0029.
 */
public class BaseErroInfo extends BaseHttpInfo{
    private String baseErroMsg;

    public String getBaseErroMsg() {
        return baseErroMsg;
    }

    public void setBaseErroMsg(String baseErroMsg) {
        this.baseErroMsg = baseErroMsg;
    }

    @Override
    public String toString() {
        return "BaseErroInfo{" +
                "baseErroMsg='" + baseErroMsg + '\'' +
                '}';
    }
}
