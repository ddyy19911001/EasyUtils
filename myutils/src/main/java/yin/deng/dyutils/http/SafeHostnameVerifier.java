package yin.deng.dyutils.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by Administrator on 2019/3/29 0029.
 */
public class SafeHostnameVerifier implements HostnameVerifier {
    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    @Override
    public boolean verify(String hostname, SSLSession session) {
        //验证主机名是否匹配
        //return hostname.equals("server.jeasonlzy.com");
        return true;
    }

}
