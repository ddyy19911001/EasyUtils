package yin.deng.dyutils.UiLayoutUtil;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewScrollDistanceUtils {
    /**
     * 获取已经滑动的距离
     * @param recyclerView
     * @return
     */
    public static int getScrollDistance(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View firstVisibItem = recyclerView.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemCount = layoutManager.getItemCount();
        int recycleViewHeight = recyclerView.getHeight();
        int itemHeight = firstVisibItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibItem);
        return (firstItemPosition + 1)*itemHeight - firstItemBottom;
    }


    /**
     * 获取RecyclerView滚动距离
     */
    private int getScrollY(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View firstVisibItem = recyclerView.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemCount = layoutManager.getItemCount();
        int recycleViewHeight = recyclerView.getHeight();
        int itemHeight = firstVisibItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibItem);
        return (itemCount - firstItemPosition - 1)* itemHeight - recycleViewHeight;
    }


    /**
     * 还能向下滑动多少
     */
    private int getLeaveDistance(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View firstVisibItem = recyclerView.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemCount = layoutManager.getItemCount();
        int recycleViewHeight = recyclerView.getHeight();
        int itemHeight = firstVisibItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibItem);
        return (itemCount - firstItemPosition - 1)* itemHeight - recycleViewHeight+firstItemBottom;
    }

}
