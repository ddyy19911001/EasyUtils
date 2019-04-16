package yin.deng.dyutils.tag_util;

import android.view.View;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TagUtils {
   static TagUtils tagUtils;
   List<String>datas=new ArrayList<>();
    private TagUtils() {
    }

    public static TagUtils getInstance(){
        if(tagUtils==null){
            tagUtils=new TagUtils();
        }
        return tagUtils;
    }

    /**
     * 是否在布局中使用默认选中效果
     * @param tagFlowLayout
     * @param isOpen
     */
    public void setAutoSelectEffet(TagFlowLayout tagFlowLayout,boolean isOpen){
        tagFlowLayout.setClickable(isOpen);
    }

    /**
     * 设置最大可选择标签个数 -1则不设置上限（默认）
     * @param tagFlowLayout
     * @param maxNum
     */
    public void setMaxSlectedTagNum(TagFlowLayout tagFlowLayout,int maxNum){
        tagFlowLayout.setMaxSelectCount(maxNum);
    }

    /**
     *
     * @param tagFlowLayout
     * @param tabRootView tab根view
     * @param tv   显示标签的textView
     * @param datas  显示的标签列表
     */
    public void initTagLayout(final TagFlowLayout tagFlowLayout, final View tabRootView, final TextView tv, List<String>datas){
        this.datas.clear();
        this.datas.addAll(datas);
        tagFlowLayout.setAdapter(new TagAdapter<String>(datas){
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                tv.setText(s);
                if(tabRootView==null){
                    return tv;
                }else {
                    return tabRootView;
                }
            }
        });
    }

    public void setOnTagClickListener(TagFlowLayout tagFlowLayout,TagFlowLayout.OnTagClickListener listener){
        tagFlowLayout.setOnTagClickListener(listener);
    }

    public void setOnSelectedListener(TagFlowLayout tagFlowLayout,TagFlowLayout.OnSelectListener listener){
        tagFlowLayout.setOnSelectListener(listener);
    }

    /**
     * 获取已经选择的tag
     * @param tagFlowLayout
     * @return
     */
    public List<String> getSelectedList(TagFlowLayout tagFlowLayout){
        Set<Integer> selecteds = tagFlowLayout.getSelectedList();
        List<String> selectedsStr=new ArrayList<>();
        for(int i:selecteds){
            selectedsStr.add(datas.get(i));
        }
        return selectedsStr;
    }


    /**
     * 设置先选中部分tag
     * @param tagFlowLayout
     * @param i
     */
    public void setSeletedSomeListTag(TagFlowLayout tagFlowLayout,int... i){
        TagAdapter mAdapter = tagFlowLayout.getAdapter();
        //预先设置选中
        mAdapter.setSelectedList(i);
    }
}
