package yin.deng.easyutils;

import android.widget.TextView;

import yin.deng.dyutils.base.fragment.SuperViewPagerSuperBaseFragment;
import yin.deng.dyutils.http.BaseHttpInfo;

public class MyPageFragment extends SuperViewPagerSuperBaseFragment {
    private TextView tvContent;


    @Override
    protected int setContentView() {
        return R.layout.page_fragment;
    }

    @Override
    public void onActivityMsgToHere(BaseHttpInfo info) {
    }

    @Override
    protected void init() {
        tvContent = (TextView)getRootView().findViewById(R.id.tv_content);
        String name=getArguments().getString("name");
        tvContent.setText(name);
    }
}
