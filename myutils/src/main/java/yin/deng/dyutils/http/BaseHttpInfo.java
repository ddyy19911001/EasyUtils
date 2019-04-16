package yin.deng.dyutils.http;

import com.lzy.okgo.model.HttpParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/3/29 0029.
 */
public class BaseHttpInfo implements Serializable {
    private String _baseRequestUrl;
    private List<HttpParams> _baseParams=new ArrayList<>();
    private Map<String,String> _baseMapParams=new HashMap<>();


    public Map<String, String> get_baseMapParams() {
        return _baseMapParams;
    }

    public void set_baseMapParams(Map<String, String> _baseMapParams) {
        this._baseMapParams = _baseMapParams;
    }

    public List<HttpParams> getBaseParams() {
        return _baseParams;
    }

    public void setBaseParams(List<HttpParams> baseParams) {
        this._baseParams = baseParams;
    }

    public String get_baseRequestUrl() {
        return _baseRequestUrl;
    }

    public void set_baseRequestUrl(String _baseRequestUrl) {
        this._baseRequestUrl = _baseRequestUrl;
    }
}
