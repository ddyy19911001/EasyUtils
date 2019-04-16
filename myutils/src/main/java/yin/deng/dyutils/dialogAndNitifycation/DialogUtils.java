package yin.deng.dyutils.dialogAndNitifycation;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.listener.OnMenuItemClickListener;
import com.kongzue.dialog.v2.BottomMenu;
import com.kongzue.dialog.v2.CustomDialog;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.InputDialog;
import com.kongzue.dialog.v2.MessageDialog;
import com.kongzue.dialog.v2.Notification;
import com.kongzue.dialog.v2.Pop;
import com.kongzue.dialog.v2.SelectDialog;
import com.kongzue.dialog.v2.TipDialog;
import com.kongzue.dialog.v2.WaitDialog;

import java.util.List;

import yin.deng.dyutils.R;
import yin.deng.dyutils.base.SuperSuperBaseActivity;

public class DialogUtils {
    private static DialogUtils utils;
    private DialogUtils() {
    }

    public static DialogUtils getInstance(){
        if(utils==null){
            utils=new DialogUtils();
        }
        DialogSettings.style = DialogSettings.STYLE_KONGZUE;
        DialogSettings.tip_theme = DialogSettings.THEME_LIGHT;         //设置提示框主题为亮色主题
        DialogSettings.use_blur = true;                 //设置是否启用模糊
        /*
         *  决定等待框、提示框以及iOS风格的对话框的模糊背景透明度（50-255）
         */
        DialogSettings.blur_alpha = 200;
        return utils;
    }


    /**
     * 消息提示框
     * @param context
     * @param title  标题
     * @param msg    内容
     * @param buttonText  按钮文字
     * @param listener  监听
     */
    public  void showMsgDialog(Context context,String title,String msg,String buttonText,DialogInterface.OnClickListener listener,boolean cancleAble){
        MessageDialog.show(context, title, msg, buttonText, listener).setCanCancel(cancleAble);
    }


    /**
     * 包含确定取消完全自定义对话框
     * @param context
     * @param title
     * @param msg
     * @param btTextOne
     * @param btTextTwo
     * @param listenerOne
     * @param listenerTwo
     */
    public  void showSelectDialog(Context context,String title,String msg,String btTextOne,String btTextTwo,DialogInterface.OnClickListener listenerOne,DialogInterface.OnClickListener listenerTwo){
        SelectDialog.show(context, title, msg, btTextOne, listenerOne, btTextTwo, listenerTwo);
    }

    /**
     * 只包含确定和取消的对话框
     * @param context
     * @param title
     * @param msg
     * @param listener
     */
    public  void showSelectDialog(Context context,String title,String msg,DialogInterface.OnClickListener listener,boolean cancleAble){
        SelectDialog.show(context, title, msg, listener).setCanCancel(cancleAble);
    }

    /**
     * 包含编辑输入文字的对话框
     * @param context
     * @param title
     * @param msg
     * @param btTextOne
     * @param btTextTwo
     * @param sureListener
     * @param cancleListener
     */
    public   void showEditDialog(final Context context, String title, String msg, String btTextOne, String btTextTwo,InputDialogOkButtonClickListener sureListener,DialogInterface.OnClickListener cancleListener) {
        InputDialog.show(context, title, msg, btTextOne, sureListener, btTextTwo,cancleListener);
    }

    public  void showEditDialog(final Context context, String title, String msg,InputDialogOkButtonClickListener sureListener,boolean cancleAble){
        InputDialog.show(context, title, msg, sureListener).setCanCancel(cancleAble);
    }

    /**
     * 显示等待view
     * @param context
     * @param msg
     */
    public  void showWatingDialog(Context context,String msg,boolean cancleAble){
        WaitDialog.setCanCancelGlobal(cancleAble);
        WaitDialog.show(context, msg).setCanCancel(cancleAble);
    }

    /**
     * 显示等待view
     * @param context
     * @param msg
     */
    public  void showWatingDialog(Context context, String msg, View customView,boolean cancleAble){
        WaitDialog.setCanCancelGlobal(cancleAble);
        WaitDialog.show(context, msg,customView).setCanCancel(cancleAble);
    }

    public  void closeWatingDialog(){
        WaitDialog.dismiss();
    }

    /**
     * 显示完成提示
     * @param context
     * @param msg
     */
    public  void showFinishTs(Context context, String msg){
        TipDialog.show(context, msg, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
    }

    /**
     * 显示警告提示
     * @param context
     * @param msg
     */
    public  void showWarningTs(Context context, String msg){
        TipDialog.show(context, msg, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
    }

    /**
     * 显示错误提示
     * @param context
     * @param msg
     */
    public  void showErroTs(Context context,String msg){
        TipDialog.show(context,msg,TipDialog.SHOW_TIME_SHORT,TipDialog.TYPE_ERROR);
    }

    /**
     * 显示通知  -->注意检查通知权限
     * @param context
     * @param msg    通知内容
     * @param id     通知id用于回调点击事件
     * @param resId  通知栏图标
     */
    public  void showNotificationTs(final Context context,String msg, int id, int resId,Notification.OnNotificationClickListener listener){
        Notification.show(context, id, resId, context.getString(R.string.app_name), msg, Notification.SHOW_TIME_LONG, Notification.TYPE_NORMAL)
                .setOnNotificationClickListener(listener)
        ;
    }

    /**
     * 显示底部菜单
     * @param context
     * @param list  菜单选项
     * @param onMenuItemClickListener  菜单点击事件
     * @param isShowCancle  是否显示取消按钮
     */
    public   void showBottomMenuDialog(SuperSuperBaseActivity context, String title, List<String> list, OnMenuItemClickListener onMenuItemClickListener, boolean isShowCancle) {
        if(list==null||list.isEmpty()){
            return;
        }
        BottomMenu.show(context, list, onMenuItemClickListener,isShowCancle).setTitle(title);
    }


    /**
     * 在任意view周围显示一个气泡View
     * @param context
     * @param fromView  参照物
     * @param msg  显示的内容
     * @param showWhere 显示的位置
     * @param colorType  颜色
     */
    public  void showPopTip(Context context,View fromView,String msg,int showWhere, int colorType){
        Pop.show(context, fromView, msg, showWhere, colorType);
    }

    /**
     * 显示自定义的Dialog
     * @param context
     * @param customLayoutId
     * @param bindView
     */
    public  void showCustomDialog(Context context,int customLayoutId,CustomDialog.BindView bindView,boolean cancleAble){
        CustomDialog.show(context, customLayoutId,bindView).setCanCancel(cancleAble);
    }
}
