package yin.deng.dyutils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import yin.deng.dyutils.utils.LogUtils;


/**
 * Created by Administrator on 2018/4/27.
 * deng yin
 */
public class MyListView extends ListView {
    public boolean isTopAlign=true;
    private int lastVisibleItemPosition;
    private int firstVisibleItemPosition;
    private boolean isMoveTop;
    public MyListView(Context context) {
        super(context);
        initScoller();
    }


    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScoller();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScoller();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initScoller();
    }

    public int getMyListViewScrollY() {
        View c = getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }


    private void initScoller() {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                //判断ListView是否滑动到第一个Item的顶部
                if (getChildCount() > 0 && getFirstVisiblePosition() == 0
                        && getChildAt(0).getTop() >= getPaddingTop()) {
                    //解决滑动冲突，当滑动到第一个item，下拉刷新才起作用
                    isTopAlign=true;
                    LogUtils.v("到顶部了！");
                } else {
                    isTopAlign=false;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    firstVisibleItemPosition=firstVisibleItem;
                    if (firstVisibleItem > lastVisibleItemPosition) {
                        LogUtils.d("上滑,第一个item位置"+firstVisibleItem);
                        isMoveTop=true;
                    }
                    if (firstVisibleItem < lastVisibleItemPosition) {
                        LogUtils.i("下滑，第一个item位置"+firstVisibleItem);
                        isMoveTop=false;
                    }
                    if(listener!=null){
                        //当前item包括头部
                        listener.onFirstItemVisiable(firstVisibleItem,isMoveTop,isTopAlign);
                    }
                if (firstVisibleItem == lastVisibleItemPosition) {
                    return;
                }
                lastVisibleItemPosition = firstVisibleItem;
        }});
    }

    OnFirstItemVisiableListener listener;

    public void setOnFirstItemVisiableListener(OnFirstItemVisiableListener listener) {
        this.listener = listener;
    }

    public interface  OnFirstItemVisiableListener{
        void onFirstItemVisiable(int firstVisibleItem, boolean isMoveTop, boolean isTopAlign);
    }

    public boolean isTopAlign() {
        return isTopAlign;
    }


    public int getFirstVisibleItemPosition() {
        return firstVisibleItemPosition;
    }

}
