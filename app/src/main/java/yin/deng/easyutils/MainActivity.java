package yin.deng.easyutils;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import androidx.viewpager.widget.ViewPager;
import yin.deng.dyutils.base.SuperSuperBaseActivity;
import yin.deng.dyutils.http.BaseHttpInfo;
import yin.deng.dyutils.tab_layout.TabUtils;

public class MainActivity extends SuperSuperBaseActivity implements View.OnClickListener {


    private ImageView iv_left;
    private TextView tv_left;
    private FrameLayout fm_left;
    private TextView tvTitle;
    private ImageView iv_right;
    private TextView tv_right;
    private FrameLayout fm_right;
    private LinearLayout title_bar;
    private TextView tv_line_title;
    private LinearLayout ll_title_root;
    private SmartTabLayout hm_tab_layout;
    private TextView tv_line_tab;
    private ViewPager viewpager;

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onMsgHere(BaseHttpInfo info) {

    }

    @Override
    protected void initFirst() {
        TabUtils utils = TabUtils.getInstance(MyPageFragment.class, getResources().getColor(R.color.black), getResources().getColor(R.color.re_2));
        String[] names = new String[]{"第一个", "第二个", "第三个", "第四个", "第五个"};
//        utils.initPageFg(hm_tab_layout, viewpager, this, names, getSupportFragmentManager());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO:OnCreate Method has been created, run FindViewById again to generate code
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_left = (TextView) findViewById(R.id.tv_left);
        fm_left = (FrameLayout) findViewById(R.id.fm_left);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        tv_right = (TextView) findViewById(R.id.tv_right);
        fm_right = (FrameLayout) findViewById(R.id.fm_right);
        title_bar = (LinearLayout) findViewById(R.id.title_bar);
        tv_line_title = (TextView) findViewById(R.id.tv_line_title);
        ll_title_root = (LinearLayout) findViewById(R.id.ll_title_root);
        hm_tab_layout = (SmartTabLayout) findViewById(R.id.hm_tab_layout);
        tv_line_tab = (TextView) findViewById(R.id.tv_line_tab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        fm_left.setOnClickListener(this);
        fm_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fm_left:

                break;
            case R.id.fm_right:

                break;
        }
    }
}
