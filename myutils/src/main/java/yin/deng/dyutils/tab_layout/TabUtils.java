package yin.deng.dyutils.tab_layout;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import yin.deng.dyutils.R;
import yin.deng.dyutils.base.fragment.SuperViewPagerSuperBaseFragment;

public class TabUtils {
    static TabUtils tabUtils;
    static int normalColor;
    static int selectColor;
    static Class<? extends SuperViewPagerSuperBaseFragment> pageFragment;
    /**
     * 添加fragment到布局中
     */
    List<TextView> tvs = new ArrayList<>();//用于存储tab
    private Fragment currentPageFragment;

    private TabUtils(){

    }

    public static TabUtils getInstance(Class<? extends SuperViewPagerSuperBaseFragment> fragment,int normalTextColor,int selectTextColor){
        if(tabUtils==null) {
             tabUtils = new TabUtils();
        }
        normalColor=normalTextColor;
        selectColor=selectTextColor;
        pageFragment=fragment;
        return tabUtils;
    }

    public Fragment getCurrentPageFragment(){
        return currentPageFragment;
    }


    public void initPageFg(SmartTabLayout tab,ViewPager viewPager, final Context activity, String[] names, FragmentManager fragmentManager) {
        FragmentPagerItems.Creator items = FragmentPagerItems.with(activity);
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            Bundle bundle = new Bundle();
            bundle.putSerializable("name", name);
            bundle.putInt("position", i);
            items.add(name, pageFragment, bundle);
        }
        FragmentPagerItems c = items.create();
        final FragmentPagerItemAdapter pageAdapter = new FragmentPagerItemAdapter(
                fragmentManager, c);
        viewPager.setOffscreenPageLimit(names.length);
        viewPager.setAdapter(pageAdapter);
        tab.setCustomTabView(R.layout.hm_custom_tab_text, R.id.tv_tab);
        tab.setViewPager(viewPager);
        tvs.clear();
        for (int i = 0; i < names.length; i++) {
            tvs.add((TextView) ((LinearLayout) tab.getTabAt(i)).getChildAt(0));
        }
        tvs.get(0).setTextColor(activity.getResources().getColor(R.color.dy_them_color_blue));
        tab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                clearTextColor();
                TextView tv = tvs.get(position);
                tv.setTextColor(activity.getResources().getColor(R.color.dy_them_color_blue));
                currentPageFragment = pageAdapter.getPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
        currentPageFragment = pageAdapter.getPage(0);
    }


    public void clearTextColor() {
        for (int i = 0; i < tvs.size(); i++) {
            tvs.get(i).setTextColor(normalColor);
        }
    }
}
