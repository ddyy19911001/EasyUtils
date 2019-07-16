package yin.deng.easyutils;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import yin.deng.dyutils.base.SuperBaseActivity;
import yin.deng.dyutils.http.BaseHttpInfo;
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
    }


    @Override
    public void onGranted() {
        LogUtils.i("授权成功");
        tvContent.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                getHttpUtils().download("http://p.gdown.baidu.com/50b689a32183224df9c41613d13be2c7cd0bb79bfa82410927aef40074893fb4183e5d14eb4135930e6441693b5502c7ce6774473a9fcfe3b9f14925aa7018a8ddc9c9fb09708a598d09d4cfadfcf9ac9388039d0d8387c752651f73c5d0e634e3aca94aeb14952cb5831b0a554f2803266ee20c0802107e2950af6e47c5119a0cc9f936c3a6e00a71f0e667f4ebb5dc92b48beff16d6c0b75302946e5741763be32b4a1df43ce9ee5a320716d893ef1","qq.apk");
            }
        });
    }



    private void initView() {
        tvContent = (TextView) findViewById(R.id.tv_content);
    }

}
