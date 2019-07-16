package yin.deng.dyutils.utils;

import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import yin.deng.dyutils.base.SuperBaseActivity;

public class PermissionUtils {

    /**
     * 权限申请
     * @param permissions
     *            待申请的权限集合
     * @param listener
     *            申请结果监听事件
     */
    public static void requestRunTimePermission(String[] permissions,
                                            SuperBaseActivity listener) {
        if(permissions==null){
            LogUtils.e("权限列表为空");
            return;
        }
        PackageManager pkm = listener.getPackageManager();
        // 用于存放为授权的权限
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            // 判断是否已经授权，未授权，则加入待授权的权限集合中
            if (ContextCompat.checkSelfPermission(listener, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }else {
                if (pkm.checkPermission(permission, listener.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
        }

        // 判断集合
        if (!permissionList.isEmpty()) { // 如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(listener,
                    permissionList.toArray(new String[permissionList.size()]),
                    1);
        } else { // 为空，则已经全部授权
            listener.onGranted();
        }
    }

}
