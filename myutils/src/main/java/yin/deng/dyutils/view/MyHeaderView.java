package yin.deng.dyutils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.cunoraz.gifview.library.GifView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import yin.deng.dyutils.R;
import yin.deng.dyutils.refresh.api.RefreshHeader;
import yin.deng.dyutils.refresh.api.RefreshKernel;
import yin.deng.dyutils.refresh.api.RefreshLayout;
import yin.deng.dyutils.refresh.constant.RefreshState;
import yin.deng.dyutils.refresh.constant.SpinnerStyle;


/**
 * Created by Administrator on 2019/3/25 0025.
 */
public class MyHeaderView extends LinearLayout implements RefreshHeader {
    private static final String L = "MyTag";
    public GifView gifView;
    public View view;
    public MyHeaderView(Context context) {
        super(context);
        setGravity(Gravity.CENTER_HORIZONTAL);
        view=View.inflate(context, R.layout.head_animate_view,null);
        gifView=view.findViewById(R.id.gif);
        gifView.pause();//暂停动画
        gifView.setGifResource(R.drawable.g);
        addView(view);
    }

    public MyHeaderView(Context context,View customView,GifView gifView) {
        super(context);
        if(customView==null||gifView==null){
            throw new NullPointerException("view can not be null on create MyHeadView");
        }
        this.view=customView;
        this.gifView=gifView;
        setGravity(Gravity.CENTER_HORIZONTAL);
        gifView.pause();//暂停动画
        addView(customView);
    }

    public MyHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MyHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public MyHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
        gifView.play();//开始动画
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        gifView.pause();//停止动画
        if (success){
            Log.d(L,"刷新完成");
        } else {
            Log.d(L,"刷新完成");
        }
        return 300;//延迟300毫秒之后再弹回
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                Log.i(L,"下拉开始刷新");
                break;
            case Refreshing:
                Log.i(L,"正在刷新");
                break;
            case ReleaseToRefresh:
                Log.i(L,"释放立即刷新");
                break;
        }
    }
}
