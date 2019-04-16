package yin.deng.dyutils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import yin.deng.dyutils.R;
import yin.deng.dyutils.refresh.api.RefreshFooter;
import yin.deng.dyutils.refresh.api.RefreshKernel;
import yin.deng.dyutils.refresh.api.RefreshLayout;
import yin.deng.dyutils.refresh.constant.RefreshState;
import yin.deng.dyutils.refresh.constant.SpinnerStyle;


/**
 * Created by Administrator on 2019/3/25 0025.
 */
public class MyFooterView extends LinearLayout implements RefreshFooter {
    private static final String L = "MyTag";
    private  TextView headText;
    private  View footerText;
    private GifView gifView;
    private View view;
    //数据是否已经完全加载
    private boolean isFinished=false;
    public MyFooterView(Context context) {
        super(context);
        setGravity(Gravity.CENTER_HORIZONTAL);
        //自定义下部显示控件
        view=View.inflate(context, R.layout.footer_animate_view,null);
        gifView=view.findViewById(R.id.gif);
        headText=view.findViewById(R.id.tv_head_text);
        footerText=view.findViewById(R.id.ll_footer);
        footerText.setVisibility(GONE);
        gifView.pause();//暂停动画
        gifView.setGifResource(R.drawable.bottom);
        headText.setVisibility(GONE);
        addView(view);
    }

    @Override@Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    public MyFooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MyFooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public MyFooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
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
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
        if(isFinished){
                gifView.setVisibility(GONE);
        }else {
                gifView.setVisibility(VISIBLE);
                gifView.play();//开始动画
        }
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        if(isFinished){
                gifView.setVisibility(GONE);
        }else {
                gifView.setVisibility(VISIBLE);
                gifView.pause();//开始动画
        }
        if (success){
            Log.d(L,"刷新完成");
        } else {
            Log.d(L,"刷新完成");
        }
        return 500;//延迟500毫秒之后再弹回
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
                    Log.i(L, "下拉开始刷新");
                    break;
                case Refreshing:
                    Log.i(L, "正在刷新");
                    if (footerText != null && footerText.getVisibility() == VISIBLE) {
                        footerText.setVisibility(GONE);
                    }
                    break;
                case ReleaseToRefresh:
                    Log.i(L, "释放立即刷新");
                    break;
            }
    }

    @Override
    public void onPullingUp(float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onPullReleasing(float percent, int offset, int footerHeight, int extendHeight) {

    }



    @Override
    public boolean setLoadmoreFinished(boolean finished) {
        this.isFinished=finished;
        if(finished) {
            footerText.setVisibility(VISIBLE);
        }else{
            footerText.setVisibility(GONE);
        }
        return finished;
    }
}
