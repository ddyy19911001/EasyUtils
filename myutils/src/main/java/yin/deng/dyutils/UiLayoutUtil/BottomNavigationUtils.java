package yin.deng.dyutils.UiLayoutUtil;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.next.easynavigation.constant.Anim;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.List;

import androidx.fragment.app.Fragment;
import yin.deng.dyutils.base.SuperSuperBaseActivity;

public class BottomNavigationUtils {
    public static void initBottomUi(SuperSuperBaseActivity activity, EasyNavigationBar navigationBar, String[] tabText, int[] normalIcon, int[] selectIcon, List<Fragment> fragments){
        navigationBar.titleItems(tabText)      //必传  Tab文字集合
                .normalIconItems(normalIcon)   //必传  Tab未选中图标集合
                .selectIconItems(selectIcon)   //必传  Tab选中图标集合
                .fragmentList(fragments)       //必传  fragment集合
                .fragmentManager(activity.getSupportFragmentManager())     //必传
                .iconSize(32)     //Tab图标大小
                .tabTextSize(10)   //Tab文字大小
                .tabTextTop(1)     //Tab文字距Tab图标的距离
                .normalTextColor(Color.parseColor("#DE000000"))   //Tab未选中时字体颜色
                .selectTextColor(Color.parseColor("#FF008AFF"))   //Tab选中时字体颜色
                .scaleType(ImageView.ScaleType.CENTER_INSIDE)  //同 ImageView的ScaleType
                .navigationBackground(Color.parseColor("#FFFFFF"))   //导航栏背景色
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {   //Tab点击事件  return true 页面不会切换
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        return false;
                    }
                })
                .smoothScroll(true)  //点击Tab  Viewpager切换是否有动画
                .canScroll(false)    //Viewpager能否左右滑动
                .mode(EasyNavigationBar.MODE_NORMAL)   //默认MODE_NORMAL 普通模式  //MODE_ADD 带加号模式
                .anim(Anim.Landing)                //点击Tab时的动画
//                .addIconSize(36)    //中间加号图片的大小
//                .addLayoutHeight(100)   //包含加号的布局高度 背景透明  所以加号看起来突出一块
                .navigationHeight(49)  //导航栏高度
                .lineHeight(1)         //分割线高度  默认1px
                .lineColor(Color.parseColor("#FFEEEEEE"))
                .addLayoutRule(EasyNavigationBar.RULE_BOTTOM) //RULE_CENTER 加号居中addLayoutHeight调节位置 EasyNavigationBar.RULE_BOTTOM 加号在导航栏靠下
                .addLayoutBottom(10)   //加号到底部的距离
                .hasPadding(true)    //true ViewPager布局在导航栏之上 false有重叠
                .hintPointLeft(-3)  //调节提示红点的位置hintPointLeft hintPointTop（看文档说明）
                .hintPointTop(-3)
                .hintPointSize(6)    //提示红点的大小
                .msgPointLeft(-10)  //调节数字消息的位置msgPointLeft msgPointTop（看文档说明）
                .msgPointTop(-10)
                .msgPointTextSize(9)  //数字消息中字体大小
                .msgPointSize(18)    //数字消息红色背景的大小
//                .addAlignBottom(true)  //加号是否同Tab文字底部对齐  RULE_BOTTOM时有效；
//                .addTextTopMargin(50)  //加号文字距离加号图片的距离
//                .addTextSize(15)      //加号文字大小
//                .addNormalTextColor(Color.parseColor("#ff0000"))    //加号文字未选中时字体颜色
//                .addSelectTextColor(Color.parseColor("#00ff00"))    //加号文字选中时字体颜色
                .build();
    }

    /**
     * 设置数字消息提示
     * @param navigationBar
     * @param position
     * @param count
     */
    public static void setMessageCount(EasyNavigationBar navigationBar,int position,int count){
        //数字消息大于99显示99+ 小于等于0不显示，取消显示则可以navigationBar.setMsgPointCount(2, 0)
        navigationBar.setMsgPointCount(position, count);
    }

    /**
     * 设置红点消息提示
     * @param navigationBar
     * @param position
     * @param showHide
     */
    public static void setRundPointShowHide(EasyNavigationBar navigationBar,int position,boolean showHide){
        //红点提示 第二个参数控制是否显示
        navigationBar.setHintPoint(position, showHide);
    }


    /**
     * 清除消息提示
     * @param navigationBar
     * @param position
     */
    public static void clearMsgPoint(EasyNavigationBar navigationBar,int position){
        //清除第一个数字消息
        navigationBar.clearMsgPoint(position);
    }


    /**
     * 清除红点提示
     * @param navigationBar
     * @param position
     */
    public static void clearRundPoint(EasyNavigationBar navigationBar,int position){
        //清除第四个红点提示
        navigationBar.clearHintPoint(position);
    }
}
