package yin.deng.dyutils.view;

import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import yin.deng.dyutils.R;
import yin.deng.dyutils.base.SuperSuperBaseActivity;

/**
 * Created by Administrator on 2017/10/12.
 * deng yin
 */
public class PopuwindowUtils {
    private SuperSuperBaseActivity mContext;
    private PopupWindow popupWindow;
    private View contentView;
    private ViewHolder holder;

    public PopuwindowUtils(SuperSuperBaseActivity mContext) {
        this.mContext = mContext;
    }


    public View getContentView() {
        return contentView;
    }

    public void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        contentView = LayoutInflater.from(mContext).inflate(
                R.layout.popwindow_layout, null);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        holder=new ViewHolder(contentView);
        holder.ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.showTs("点击第一个");
                dismissPop();
            }
        });
        holder.ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.showTs("点击第二个");
                dismissPop();
            }
        });

        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(null);

        // 设置好参数之后再show
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                dismissPop();
            }
        });
        backgroundAlpha(0.5f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(view, -20, 0, Gravity.RIGHT);
        }
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
    }


    public void dismissPop() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    static class ViewHolder {
        ImageView iv1;
        TextView tv1;
        LinearLayout ll1;
        ImageView iv2;
        TextView tv2;
        LinearLayout ll2;
        ImageView iv3;
        TextView tv3;
        LinearLayout ll3;

        ViewHolder(View view) {
            iv1=view.findViewById(R.id.iv1);
            iv2=view.findViewById(R.id.iv2);
            iv3=view.findViewById(R.id.iv3);
            tv1=view.findViewById(R.id.tv1);
            tv2=view.findViewById(R.id.tv2);
            tv3=view.findViewById(R.id.tv3);
            ll1=view.findViewById(R.id.ll1);
            ll2=view.findViewById(R.id.ll2);
            ll3=view.findViewById(R.id.ll3);
        }
    }
}
