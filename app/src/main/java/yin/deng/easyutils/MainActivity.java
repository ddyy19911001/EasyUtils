package yin.deng.easyutils;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import yin.deng.dyutils.base.SuperBaseActivity;
import yin.deng.dyutils.http.BaseHttpInfo;
import yin.deng.dyutils.http.JsoupUtils;
import yin.deng.dyutils.utils.LogUtils;
import yin.deng.dyutils.utils.NoDoubleClickListener;

public class MainActivity extends SuperBaseActivity {


    private TextView tvContent;

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onMsgHere(BaseHttpInfo info) {

    }

    @Override
    protected void initFirst() {
        initView();
        String []permisions={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        requestRunTimePermission(permisions,this);
        JsoupUtils.getHtmlDoctmentNoParams(this, "https://porn.jmmdh.xyz/search/" + "女" + "/" + 0 + "/", new JsoupUtils.OnDataGetListener() {
            @Override
            public void onGetData(Document data) {
                showTs("获取到数据了");
                LogUtils.i("data："+data);
            }

            @Override
            public void onErro(String erro) {
                showTs("获取数据失败了："+erro);
            }
        });
    }


    @Override
    public void onGranted() {
        LogUtils.i("授权成功");
    }



    private void initView() {
        tvContent = (TextView) findViewById(R.id.tv_content);
    }

}
