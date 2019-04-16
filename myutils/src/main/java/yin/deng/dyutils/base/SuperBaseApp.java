package yin.deng.dyutils.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import yin.deng.dyutils.R;
import yin.deng.dyutils.appUtil.CrashHandlerUtil;
import yin.deng.dyutils.http.MyHttpUtils;
import yin.deng.dyutils.refresh.SmartRefreshLayout;
import yin.deng.dyutils.refresh.api.DefaultRefreshFooterCreater;
import yin.deng.dyutils.refresh.api.DefaultRefreshHeaderCreater;
import yin.deng.dyutils.refresh.api.RefreshFooter;
import yin.deng.dyutils.refresh.api.RefreshHeader;
import yin.deng.dyutils.refresh.api.RefreshLayout;
import yin.deng.dyutils.utils.LogUtils;
import yin.deng.dyutils.utils.SharedPreferenceUtil;
import yin.deng.dyutils.view.MyFooterView;
import yin.deng.dyutils.view.MyHeaderView;


/**
 * Created by Administrator on 2018/4/12.
 * deng yin
 */
public class SuperBaseApp extends Application {
    public static SuperBaseApp app;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private MyHttpUtils httpUtils;
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("执行BaseAppInit");
        closeAndroidPDialog();
        handleSSLHandshake();
        initCrashHandler();
        app=this;
        initX5();
        httpUtils=MyHttpUtils.init(this);
        FlowManager.init(this);
    }

    private void initCrashHandler() {
        //崩溃处理
        CrashHandlerUtil crashHandlerUtil = CrashHandlerUtil.getInstance();
        crashHandlerUtil.init(this);
        crashHandlerUtil.setCrashTip("程序异常，即将退出");
    }

    public MyHttpUtils getHttpUtils() {
        if(httpUtils==null){
            httpUtils=MyHttpUtils.init(this);
        }
        return httpUtils;
    }


    private void initX5() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                LogUtils.i("X5浏览器初始化：完成");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                LogUtils.i("X5浏览器初始化："+b);
            }
        });
    }




    //关闭android P上使用x5弹出对话框的问题
    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public SharedPreferenceUtil getSharedPreferenceUtil() {
        if (sharedPreferenceUtil == null) {
            sharedPreferenceUtil = new SharedPreferenceUtil(this,getApplicationContext().getPackageName());
        }
        return sharedPreferenceUtil;
    }







    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.normal_bg, R.color.normal_4a);//全局设置主题颜色
                MyHeaderView head = new MyHeaderView(context);
                return head;//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                layout.setPrimaryColorsId(R.color.normal_bg, R.color.normal_4a);//全局设置主题颜色
                MyFooterView footer = new MyFooterView(context);
                return footer;
            }
        });
    }

}
