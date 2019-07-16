package yin.deng.dyutils.http;

import android.app.Activity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import yin.deng.dyutils.utils.LogUtils;
import yin.deng.dyutils.utils.MyUtils;

public class JsoupUtils {
    public static  int TIME_OUT_SECONDE=12000;

    public interface OnDataGetListener{
        void onGetData(Document data);
        void onErro(String erro);
    }


    public static void getHtmlDoctment(final Activity context, final String requestUrl, final boolean isGet
            , final List<JsoupParams> heads, final List<JsoupParams> bodys, final OnDataGetListener onDataGetListener){
        new Thread(){
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect(requestUrl);
                    connection.timeout(TIME_OUT_SECONDE);
                    if(!MyUtils.isEmpty(heads)){
                        for(int i=0;i<heads.size();i++){
                            if(MyUtils.isEmpty(heads.get(i).getKey())){
                                continue;
                            }
                            connection.header(heads.get(i).getKey(),heads.get(i).getValues());
                        }
                    }
                    if(!MyUtils.isEmpty(bodys)){
                        for(int i=0;i<bodys.size();i++){
                            if(MyUtils.isEmpty(bodys.get(i).getKey())){
                                continue;
                            }
                            connection.data(bodys.get(i).getKey(),bodys.get(i).getValues());
                        }
                    }
                    final Document document;
                    if(isGet) {
                        document=connection.get();
                    }else {
                        document=connection.post();
                    }
                    LogUtils.i("OnDoctmentGet:+\n"+document);
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onGetData(document);
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onErro(e.getMessage());
                            }
                        });
                    }
                }
            }
        }.start();
    }


    public static void getHtmlDoctmentNeedParams(final Activity context, final String requestUrl
            , final List<JsoupParams> bodys, final OnDataGetListener onDataGetListener){
        new Thread(){
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect(requestUrl);
                    connection.timeout(TIME_OUT_SECONDE);
                    if(!MyUtils.isEmpty(bodys)){
                        for(int i=0;i<bodys.size();i++){
                            if(MyUtils.isEmpty(bodys.get(i).getKey())){
                                continue;
                            }
                            connection.data(bodys.get(i).getKey(),bodys.get(i).getValues());
                        }
                    }
                    final Document document;
                    document=connection.get();
                    LogUtils.i("OnDoctmentGet:+\n"+document);
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onGetData(document);
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onErro(e.getMessage());
                            }
                        });
                    }
                }
            }
        }.start();
    }

    public static void getHtmlDoctmentNoParams(final Activity context, final String requestUrl, final OnDataGetListener onDataGetListener){
        new Thread(){
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect(requestUrl);
                    connection.timeout(TIME_OUT_SECONDE);
                    final Document document;
                    document=connection.get();
                    LogUtils.i("OnDoctmentGet:+\n"+document);
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onGetData(document);
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onErro(e.getMessage());
                            }
                        });
                    }
                }
            }
        }.start();
    }


    public static void getHtmlDoctmentNeedParams(final Activity context, final String requestUrl, final boolean isGet
            , final List<JsoupParams> bodys, final OnDataGetListener onDataGetListener){
        new Thread(){
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect(requestUrl);
                    connection.timeout(TIME_OUT_SECONDE);
                    if(!MyUtils.isEmpty(bodys)){
                        for(int i=0;i<bodys.size();i++){
                            if(MyUtils.isEmpty(bodys.get(i).getKey())){
                                continue;
                            }
                            connection.data(bodys.get(i).getKey(),bodys.get(i).getValues());
                        }
                    }
                    final Document document;
                    if(isGet) {
                        document=connection.get();
                    }else {
                        document=connection.post();
                    }
                    LogUtils.i("OnDoctmentGet:+\n"+document);
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onGetData(document);
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onErro(e.getMessage());
                            }
                        });
                    }
                }
            }
        }.start();
    }

    public static void getHtmlDoctmentNeedHeaders(final Activity context, final String requestUrl, final boolean isGet
            , final List<JsoupParams> heads, final OnDataGetListener onDataGetListener){
        new Thread(){
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect(requestUrl);
                    connection.timeout(TIME_OUT_SECONDE);
                    if(!MyUtils.isEmpty(heads)){
                        for(int i=0;i<heads.size();i++){
                            if(MyUtils.isEmpty(heads.get(i).getKey())){
                                continue;
                            }
                            connection.header(heads.get(i).getKey(),heads.get(i).getValues());
                        }
                    }
                    final Document document;
                    if(isGet) {
                        document=connection.get();
                    }else {
                        document=connection.post();
                    }
                    LogUtils.i("OnDoctmentGet:+\n"+document);
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onGetData(document);
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onErro(e.getMessage());
                            }
                        });
                    }
                }
            }
        }.start();
    }


}
