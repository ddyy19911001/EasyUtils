package yin.deng.dyutils.http;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import yin.deng.dyutils.dialogAndNitifycation.DialogUtils;
import yin.deng.dyutils.utils.LogUtils;
import yin.deng.dyutils.utils.NetUtils;

/**
 * Created by Administrator on 2019/3/29 0029.
 */
public class MyHttpUtils {
    private static final String NoNETMSG = "网络连接异常";
    public static MyHttpUtils myHttpUtils;
    public static Application app;
    public static MyHttpUtils init(Application application){
        app=application;
        if(myHttpUtils!=null){
            return myHttpUtils;
        }
        myHttpUtils=new MyHttpUtils();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //使用数据库保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(application)));
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        headers.put("date",format.format(new Date()));
        headers.put("Content-Type","application/json");
        HttpParams params = new HttpParams();
        params.put("time",System.currentTimeMillis());
        //-------------------------------------------------------------------------------------//
        OkGo.getInstance().init(application)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数
        return myHttpUtils;
    }


    public interface OnHttpCallBackListener{
        void onSuccess(String data);
        void onFail(String erro);
    }



    public <T>void sendPostJsonObjectMsg(String url, JsonObject jsonObject, final OnHttpCallBackListener callBackListener){
        try {
            OkGo.<T>post(url).upJson(jsonObject.toString()).execute(new InfoCallBack<T>(){
                @Override
                public void onSuccess(Response<T> response) {
                    if(callBackListener!=null&&response!=null&&response.body()!=null){
                        callBackListener.onSuccess(response.body().toString());
                    }
                }

                @Override
                public void onError(Response<T> response) {
                    if(callBackListener!=null&&response!=null&&response.body()!=null){
                        callBackListener.onFail(response.body().toString());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T>void sendPostJsonObjectResponseInRawWay(final String url, final JsonObject jsonObject, final Class x){
        try {
            OkGo.<T>post(url).upJson(jsonObject.toString()).execute(new InfoCallBack<T>(){
                @Override
                public void onSuccess(Response<T> response) {
                    //注意这里已经是在主线程了
                        Reader r = response.getRawResponse().body().charStream();
                        BufferedReader bufferedReader = new BufferedReader(r);
                        StringBuffer sb = new StringBuffer();
                        String temp = null;
                        try {
                            while ((temp = bufferedReader.readLine()) != null) {
                                sb.append(temp);
                            }
                            bufferedReader.close();
                            LogUtils.d("请求成功:"+sb.toString());
                            Object searchInfo=new Gson().fromJson(sb.toString(),x);
                            EventBus.getDefault().post(searchInfo);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                }

                @Override
                public void onError(Response<T> response) {
                    BaseErroInfo erroInfo=new BaseErroInfo();
                    erroInfo.setBaseErroMsg("数据获取失败");
                    EventBus.getDefault().post(erroInfo);
                    LogUtils.e("数据解析错误："+erroInfo.getBaseErroMsg());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T>void sendPostJsonObjectMsg(final String url, final Object object){
        try {
            Gson gson=new Gson();
            final String jsonStr=gson.toJson(object);
            OkGo.<T>post(url).upJson(jsonStr).execute(new InfoCallBack<T>(){
                @Override
                public void onSuccess(Response<T> response) {
                    //注意这里已经是在主线程了
                    T data = response.body();//这个就是返回来的结果
                    initLogOnResponse(url,jsonStr,data);
                    if(data instanceof BaseHttpInfo){
                        BaseHttpInfo httpInfo= (BaseHttpInfo) data;
                        if(object!=null) {
                            HashMap<String, String> params = objToHttpParams(object);
                            httpInfo.set_baseMapParams(params);
                        }
                        httpInfo.set_baseRequestUrl(url);
                        EventBus.getDefault().post(httpInfo);
                    }else {
                        EventBus.getDefault().post(data);
                    }
                }

                @Override
                public void onError(Response<T> response) {
                    T data=response.body();
                    BaseErroInfo erroInfo=new BaseErroInfo();
                    erroInfo.setBaseErroMsg("数据获取失败");
                    EventBus.getDefault().post(erroInfo);
                    LogUtils.e("数据解析错误："+erroInfo.getBaseErroMsg());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void sendGetMsg(final String url, final List<HttpParams> paramses, final OnHttpCallBackListener callBackListener){
        if(!NetUtils.isNetworkConnected(app)){
            DialogUtils.getInstance().showWarningTs(app,NoNETMSG);
            return;
        }
        Request request=OkGo.<JsonObject>get(url).tag(url);
        if(paramses!=null){
            for(int i=0;i<paramses.size();i++) {
                request = request.params(paramses.get(i));
            }
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                //注意这里已经是在主线程了
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,paramses,data);
                if(callBackListener!=null){
                   callBackListener.onSuccess(data);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                String data = response.body();//这个就是返回来的结果
                if(callBackListener!=null){
                    callBackListener.onFail(data);
                }
            }
        });
    }



    public void sendGetMsg(final String url, final List<HttpParams> paramses, final Class x){
        if(!NetUtils.isNetworkConnected(app)){
            DialogUtils.getInstance().showWarningTs(app,NoNETMSG);
            return;
        }
        Request request=OkGo.<JsonObject>get(url).tag(url);
        if(paramses!=null){
            for(int i=0;i<paramses.size();i++) {
                request = request.params(paramses.get(i));
            }
        }
        request.execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String data = response.body();//这个就是返回来的结果
                        initLogOnResponse(url,paramses,data);
                        Gson gson=new Gson();
                        try {
                            Object o=gson.fromJson(data,x);
                            if(o instanceof BaseHttpInfo){
                                BaseHttpInfo httpInfo= (BaseHttpInfo) o;
                                httpInfo.set_baseRequestUrl(url);
                                httpInfo.setBaseParams(paramses);
                                EventBus.getDefault().post(httpInfo);
                            }else {
                                EventBus.getDefault().post(o);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            BaseErroInfo erroInfo=new BaseErroInfo();
                            erroInfo.setBaseErroMsg(e.getMessage());
                            EventBus.getDefault().post(erroInfo);
                            LogUtils.e("数据解析错误："+erroInfo.getBaseErroMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        String data = response.body();//这个就是返回来的结果
                        initLogOnResponse(url,paramses,data);
                        BaseErroInfo erroInfo=new BaseErroInfo();
                        erroInfo.setBaseErroMsg(data);
                        EventBus.getDefault().post(erroInfo);
                    }
                });
    }

    public void sendPostMsg(final String url, final List<HttpParams> paramses, final Class x){
        if(!NetUtils.isNetworkConnected(app)){
            DialogUtils.getInstance().showWarningTs(app,NoNETMSG);
            return;
        }
        Request request=OkGo.<JsonObject>post(url).tag(url);
        if(paramses!=null) {
            for (int i = 0; i < paramses.size(); i++) {
                request = request.params(paramses.get(i));
            }
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                //注意这里已经是在主线程了
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,paramses,data);
                Gson gson=new Gson();
                try {
                    Object o=gson.fromJson(data,x);
                    if(o instanceof BaseHttpInfo){
                        BaseHttpInfo httpInfo= (BaseHttpInfo) o;
                        httpInfo.set_baseRequestUrl(url);
                        httpInfo.setBaseParams(paramses);
                        EventBus.getDefault().post(httpInfo);
                    }else {
                        EventBus.getDefault().post(o);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    BaseErroInfo erroInfo=new BaseErroInfo();
                    erroInfo.setBaseErroMsg(e.getMessage());
                    EventBus.getDefault().post(erroInfo);
                    LogUtils.e("数据解析错误："+erroInfo.getBaseErroMsg());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,paramses,data);
                BaseErroInfo erroInfo=new BaseErroInfo();
                erroInfo.setBaseErroMsg(data);
                EventBus.getDefault().post(erroInfo);
            }
        });
    }

    public void sendPostMsg(final String url, final List<HttpParams> paramses, final OnHttpCallBackListener callBackListener){
        if(!NetUtils.isNetworkConnected(app)){
            DialogUtils.getInstance().showWarningTs(app,NoNETMSG);
            return;
        }
        Request request=OkGo.<JsonObject>post(url).tag(url);
        if(paramses!=null) {
            for (int i = 0; i < paramses.size(); i++) {
                request = request.params(paramses.get(i));
            }
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                //注意这里已经是在主线程了
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,paramses,data);
                if(callBackListener!=null){
                    callBackListener.onSuccess(data);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,paramses,data);
               if(callBackListener!=null){
                   callBackListener.onFail(data);
               }
            }
        });
    }





    public void sendGetMsg(final String url, final Object obj, final Class x){
        if(!NetUtils.isNetworkConnected(app)){
            DialogUtils.getInstance().showWarningTs(app,NoNETMSG);
            return;
        }
        Request request=OkGo.<JsonObject>get(url).tag(url);
        if(obj!=null) {
            request = request.params(objToHttpParams(obj));
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                //注意这里已经是在主线程了
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,obj,data);
                Gson gson=new Gson();
                try {
                    Object o=gson.fromJson(data,x);
                    if(o instanceof BaseHttpInfo){
                        BaseHttpInfo httpInfo= (BaseHttpInfo) o;
                        httpInfo.set_baseRequestUrl(url);
                        httpInfo.set_baseMapParams(objToHttpParams(obj));
                        EventBus.getDefault().post(httpInfo);
                    }else {
                        EventBus.getDefault().post(o);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    BaseErroInfo erroInfo=new BaseErroInfo();
                    erroInfo.setBaseErroMsg(e.getMessage());
                    EventBus.getDefault().post(erroInfo);
                    LogUtils.e("数据解析错误："+erroInfo.getBaseErroMsg());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,obj,data);
                BaseErroInfo erroInfo=new BaseErroInfo();
                erroInfo.setBaseErroMsg(data);
                EventBus.getDefault().post(erroInfo);
            }
        });
    }

    public void sendFilesWithObj(final String url, final Object obj, final Class x,String keyFilesName,List<File> files){
        if(!NetUtils.isNetworkConnected(app)){
            DialogUtils.getInstance().showWarningTs(app,NoNETMSG);
            return;
        }
        Request request=OkGo.<String>post(url).tag(url).addFileParams(keyFilesName,files);
        if(obj!=null) {
            request = request.params(objToHttpParams(obj));
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                //注意这里已经是在主线程了
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,obj,data);
                Gson gson=new Gson();
                try {
                    Object o=gson.fromJson(data,x);
                    if(o instanceof BaseHttpInfo){
                        BaseHttpInfo httpInfo= (BaseHttpInfo) o;
                        httpInfo.set_baseRequestUrl(url);
                        httpInfo.set_baseMapParams(objToHttpParams(obj));
                        EventBus.getDefault().post(httpInfo);
                    }else {
                        EventBus.getDefault().post(o);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    BaseErroInfo erroInfo=new BaseErroInfo();
                    erroInfo.setBaseErroMsg(e.getMessage());
                    EventBus.getDefault().post(erroInfo);
                    LogUtils.e("数据解析错误："+erroInfo.getBaseErroMsg());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,obj,data);
                BaseErroInfo erroInfo=new BaseErroInfo();
                erroInfo.setBaseErroMsg(data);
                EventBus.getDefault().post(erroInfo);
            }
        });
    }


    public void sendFilesWithPostParams(final String url, final List<HttpParams> paramses, final Class x,String keyFilesName,List<File> files){
        if(!NetUtils.isNetworkConnected(app)){
            DialogUtils.getInstance().showWarningTs(app,NoNETMSG);
            return;
        }
        Request request=OkGo.<String>post(url).tag(url).addFileParams(keyFilesName,files);
        if(paramses!=null) {
            for (int i = 0; i < paramses.size(); i++) {
                request = request.params(paramses.get(i));
            }
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                //注意这里已经是在主线程了
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,paramses,data);
                Gson gson=new Gson();
                try {
                    Object o=gson.fromJson(data,x);
                    if(o instanceof BaseHttpInfo){
                        BaseHttpInfo httpInfo= (BaseHttpInfo) o;
                        httpInfo.set_baseRequestUrl(url);
                        httpInfo.setBaseParams(paramses);
                        EventBus.getDefault().post(httpInfo);
                    }else {
                        EventBus.getDefault().post(o);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    BaseErroInfo erroInfo=new BaseErroInfo();
                    erroInfo.setBaseErroMsg(e.getMessage());
                    EventBus.getDefault().post(erroInfo);
                    LogUtils.e("数据解析错误："+erroInfo.getBaseErroMsg());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,paramses,data);
                BaseErroInfo erroInfo=new BaseErroInfo();
                erroInfo.setBaseErroMsg(data);
                EventBus.getDefault().post(erroInfo);
            }
        });
    }

    public void sendFilesWithPostParams(final String url, final List<HttpParams> paramses, final Class x,List<String> fileNames,List<File> files){
        if(!NetUtils.isNetworkConnected(app)){
            DialogUtils.getInstance().showWarningTs(app,NoNETMSG);
            return;
        }
        PostRequest<String> request=OkGo.<String>post(url).tag(url);
        if(paramses!=null) {
            for (int i = 0; i < paramses.size(); i++) {
                request = request.params(paramses.get(i));
            }
        }
        if(fileNames!=null&&files!=null&&fileNames.size()==files.size()) {
            for (int i = 0; i < fileNames.size(); i++) {
                request = request.params(fileNames.get(i),files.get(i));
            }
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                //注意这里已经是在主线程了
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,paramses,data);
                Gson gson=new Gson();
                try {
                    Object o=gson.fromJson(data,x);
                    if(o instanceof BaseHttpInfo){
                        BaseHttpInfo httpInfo= (BaseHttpInfo) o;
                        httpInfo.set_baseRequestUrl(url);
                        httpInfo.setBaseParams(paramses);
                        EventBus.getDefault().post(httpInfo);
                    }else {
                        EventBus.getDefault().post(o);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    BaseErroInfo erroInfo=new BaseErroInfo();
                    erroInfo.setBaseErroMsg(e.getMessage());
                    EventBus.getDefault().post(erroInfo);
                    LogUtils.e("数据解析错误："+erroInfo.getBaseErroMsg());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,paramses,data);
                BaseErroInfo erroInfo=new BaseErroInfo();
                erroInfo.setBaseErroMsg(data);
                EventBus.getDefault().post(erroInfo);
            }
        });
    }

    public void sendFileWithObj(final String url, final Object obj, final Class x,File file,String fileName){
        if(!NetUtils.isNetworkConnected(app)){
            DialogUtils.getInstance().showWarningTs(app,NoNETMSG);
            return;
        }
        PostRequest<String> request = OkGo.<String>post(url).tag(url).params(fileName, file);
        if(obj!=null) {
            request = request.params(objToHttpParams(obj));
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                //注意这里已经是在主线程了
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,obj,data);
                Gson gson=new Gson();
                try {
                    Object o=gson.fromJson(data,x);
                    if(o instanceof BaseHttpInfo){
                        BaseHttpInfo httpInfo= (BaseHttpInfo) o;
                        httpInfo.set_baseRequestUrl(url);
                        httpInfo.set_baseMapParams(objToHttpParams(obj));
                        EventBus.getDefault().post(httpInfo);
                    }else {
                        EventBus.getDefault().post(o);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    BaseErroInfo erroInfo=new BaseErroInfo();
                    erroInfo.setBaseErroMsg(e.getMessage());
                    EventBus.getDefault().post(erroInfo);
                    LogUtils.e("数据解析错误："+erroInfo.getBaseErroMsg());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,obj,data);
                BaseErroInfo erroInfo=new BaseErroInfo();
                erroInfo.setBaseErroMsg(data);
                EventBus.getDefault().post(erroInfo);
            }
        });
    }

    public void sendFilesWithPostParams(final String url, final List<HttpParams> paramses, final Class x,File file,String fileName){
        if(!NetUtils.isNetworkConnected(app)){
            DialogUtils.getInstance().showWarningTs(app,NoNETMSG);
            return;
        }
        PostRequest<String> request=OkGo.<String>post(url).tag(url);
        if(paramses!=null) {
            for (int i = 0; i < paramses.size(); i++) {
                request = request.params(paramses.get(i));
            }
        }
        request.params(fileName,file);
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                //注意这里已经是在主线程了
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,paramses,data);
                Gson gson=new Gson();
                try {
                    Object o=gson.fromJson(data,x);
                    if(o instanceof BaseHttpInfo){
                        BaseHttpInfo httpInfo= (BaseHttpInfo) o;
                        httpInfo.set_baseRequestUrl(url);
                        httpInfo.setBaseParams(paramses);
                        EventBus.getDefault().post(httpInfo);
                    }else {
                        EventBus.getDefault().post(o);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    BaseErroInfo erroInfo=new BaseErroInfo();
                    erroInfo.setBaseErroMsg(e.getMessage());
                    EventBus.getDefault().post(erroInfo);
                    LogUtils.e("数据解析错误："+erroInfo.getBaseErroMsg());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,paramses,data);
                BaseErroInfo erroInfo=new BaseErroInfo();
                erroInfo.setBaseErroMsg(data);
                EventBus.getDefault().post(erroInfo);
            }
        });
    }


    public void sendFileWithObj(final String url, final Object obj, final Class x,List<String> fileNames,List<File> files){
        if(!NetUtils.isNetworkConnected(app)){
            DialogUtils.getInstance().showWarningTs(app,NoNETMSG);
            return;
        }
        PostRequest<String> request = OkGo.<String>post(url).tag(url);
        if(obj!=null) {
            request = request.params(objToHttpParams(obj));
        }
        if(fileNames!=null&&files!=null&&fileNames.size()==files.size()) {
            for (int i = 0; i < fileNames.size(); i++) {
                request = request.params(fileNames.get(i),files.get(i));
            }
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                //注意这里已经是在主线程了
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,obj,data);
                Gson gson=new Gson();
                try {
                    Object o=gson.fromJson(data,x);
                    if(o instanceof BaseHttpInfo){
                        BaseHttpInfo httpInfo= (BaseHttpInfo) o;
                        httpInfo.set_baseRequestUrl(url);
                        httpInfo.set_baseMapParams(objToHttpParams(obj));
                        EventBus.getDefault().post(httpInfo);
                    }else {
                        EventBus.getDefault().post(o);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    BaseErroInfo erroInfo=new BaseErroInfo();
                    erroInfo.setBaseErroMsg(e.getMessage());
                    EventBus.getDefault().post(erroInfo);
                    LogUtils.e("数据解析错误："+erroInfo.getBaseErroMsg());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                String data = response.body();//这个就是返回来的结果
                initLogOnResponse(url,obj,data);
                BaseErroInfo erroInfo=new BaseErroInfo();
                erroInfo.setBaseErroMsg(data);
                EventBus.getDefault().post(erroInfo);
            }
        });
    }


    public HashMap<String,String>  objToHttpParams(Object object){
        HashMap<String, String> maps=new HashMap<>();
        try {
           maps= ObjectUtils.objectToMap(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return maps;
    }


    /**
     * FileCallback(String destFileName)：可以额外指定文件下载完成后的文件名
     * FileCallback(String destFileDir, String destFileName)：可以额外指定文件的下载目录和下载完成后的文件名
     * 必须先创建好文件夹
     * @param url
     * @param paramses
     * @param callback
     */
    public void downloadFile(final String url, final List<HttpParams> paramses, FileCallback callback){
        if(!NetUtils.isNetworkConnected(app)){
            DialogUtils.getInstance().showWarningTs(app,NoNETMSG);
            return;
        }
        Request request=OkGo.<String>get(url).tag(url);
        if(paramses!=null) {
            for (int i = 0; i < paramses.size(); i++) {
                request = request.params(paramses.get(i));
            }
        }
        request.execute(callback);
        initLogOnResponse(url,paramses,"下载任务--任务开始后重写callback查看downloadprogress");
    }


    private void initLogOnResponse(String url, List<HttpParams> paramses, Object o) {
        LogUtils.i("数据响应：\nurl="+url+"\n参数："+paramses+"\n响应体："+o);
    }
    private void initLogOnResponse(String url, Object object, Object o) {
        LogUtils.i("数据响应：\nurl="+url+"\n参数："+object+"\n响应体："+o);
    }

}
