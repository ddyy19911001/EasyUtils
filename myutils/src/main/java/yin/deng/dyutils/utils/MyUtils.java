package yin.deng.dyutils.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import yin.deng.dyutils.R;
import yin.deng.dyutils.tips.ToastUtil;

/**
 * Created by Administrator on 2019/3/29 0029.
 */
public class MyUtils {
    private static Dialog loadingDialog;
    public static boolean isEmpty(String text){
        if(TextUtils.isEmpty(text)||TextUtils.isEmpty(text.trim())){
            return true;
        }
        return false;
    }


    public static boolean isEmpty(TextView text){
        if(TextUtils.isEmpty(text.getText().toString())||TextUtils.isEmpty(text.getText().toString().trim())){
            return true;
        }
        return false;
    }

    public static boolean isEmpty(List<Object> lists){
        if(lists==null||lists.size()==0){
            return true;
        }
        return false;
    }


    /**
     * 格式化时间
     * @param strFormat yyyy-MM-dd HH:mm:ss
     * @param time  102252300
     * @return
     */
    public static String formatTime(String strFormat,long time){
        String s="";
        try {
            SimpleDateFormat format = new SimpleDateFormat(strFormat);
            s=format.format(new Date(time));
        }catch (Exception e){
            e.printStackTrace();
            s="格式化时间错误";
        }
        return s;
    }

    static ToastUtil tUtil;
    public static void showTs(Context context,String msg) {
        if(tUtil==null){
            tUtil=new ToastUtil(context,msg);
        }
        tUtil.setText(msg);
        tUtil.show();
    }


    /**
     * 显示转圈的Dialog
     * @return
     */
    public static Dialog showLoadingDialog(Context context,String msg, boolean canDismiss) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息
        if(loadingDialog!=null&&loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
        loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(canDismiss); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        return loadingDialog;
    }


    /**
     * 关闭dialog
     */
    public static void closeDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog=null;
        }
    }
}
