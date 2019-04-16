package yin.deng.dyutils.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.utils.TbsLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import yin.deng.dyutils.R;
import yin.deng.dyutils.appUtil.AppActivityListManager;
import yin.deng.dyutils.appUtil.AppExit2Back;
import yin.deng.dyutils.base.fragment.SuperViewPagerSuperBaseFragment;
import yin.deng.dyutils.dialogAndNitifycation.DialogUtils;
import yin.deng.dyutils.http.BaseHttpInfo;
import yin.deng.dyutils.http.MyHttpUtils;
import yin.deng.dyutils.mzbanner.MZBannerView;
import yin.deng.dyutils.permission.PermissionListener;
import yin.deng.dyutils.permission.PermissionPageUtils;
import yin.deng.dyutils.refresh.SmartRefreshLayout;
import yin.deng.dyutils.refresh.api.RefreshLayout;
import yin.deng.dyutils.refresh.listener.OnLoadmoreListener;
import yin.deng.dyutils.refresh.listener.OnRefreshListener;
import yin.deng.dyutils.tips.ToastUtil;
import yin.deng.dyutils.utils.LogUtils;
import yin.deng.dyutils.utils.MyUtils;
import yin.deng.dyutils.utils.NetInfo;
import yin.deng.dyutils.utils.NetStateReceiver;
import yin.deng.dyutils.utils.SharedPreferenceUtil;
import yin.deng.dyutils.utils.StatuBarUtils;
import yin.deng.dyutils.web.X5WebView;


/**
 * Created by Administrator on 2018/4/12.
 * deng yin
 */
public abstract class SuperSuperBaseActivity<T> extends AppCompatActivity implements SuperBaseConfig, OnRefreshListener, OnLoadmoreListener,PermissionListener {
    public X5WebView mWebView;
    public List<T> _infos = new ArrayList<>();
    public int _page;
    private ToastUtil toast;
    private long exitTime;
    private Dialog loadingDialog;
    private boolean isMainActivity;
    private NetStateReceiver netChangeReceiver;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private String title;
    public static final int OnlySendToCurrentAc = 1;
    public static final int OnlySendToCurrentFg = 0;//若有消息并且有fragment则消息传递到fg不传递到ac
    public static final int BothSendMsg = 2;
    private int fgSendMsgType = BothSendMsg;
    public static final int BothSend = 2;
    public static final int REFRESH = 0;
    public static final int LOADMORE = 1;
    public int smState = REFRESH;
    public MyHttpUtils httpUtils;
    MZBannerView bannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将所有activity列入activity管理器中方便退出时清理
        AppActivityListManager.getScreenManager().addActivity(this);
        setContentView(setLayout());
        bindViewWithId();
        //去除小米手机白色状态栏
        StatuBarUtils.setStatusBarTranslucent(this,true);
        EventBus.getDefault().register(this);
        //如果有用到x5webview会进行初始化设置
        initX5WebLive();
        //如果有用到bannerview会进行初始化设置
        initBannerView();
        initLeftClick();
        initTitle();
        initNetWork();
        //设置状态栏
        setStatusStyle(Color.WHITE);
        setLvListener();
        isMainActivity = setIsExitActivity();
        fgSendMsgType = BothSend;
        initFirst();
    }

    public void bindViewWithId() {

    }

    /**
     * 初始化bannerview
     */
    private void initBannerView() {
         bannerView = findViewById(R.id.banner);
    }



    public MyHttpUtils getHttpUtils() {
        return SuperBaseApp.app.getHttpUtils();
    }


    /**
     * 权限申请
     * @param permissions
     *            待申请的权限集合
     * @param listener
     *            申请结果监听事件
     */
    protected void requestRunTimePermission(String[] permissions,
                                            SuperSuperBaseActivity listener) {
        PackageManager pkm = getPackageManager();
        // 用于存放为授权的权限
        List<String> permissionList = new ArrayList<>();
        if(permissions==null){
            LogUtils.e("权限列表为空");
            return;
        }
        for (String permission : permissions) {
            // 判断是否已经授权，未授权，则加入待授权的权限集合中
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }else {
                if (pkm.checkPermission(permission, getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
        }

        // 判断集合
        if (!permissionList.isEmpty()) { // 如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]),
                    1);
        } else { // 为空，则已经全部授权
            listener.onGranted();
        }
    }


    /**
     * 权限申请结果
     * @param requestCode
     *            请求码
     * @param permissions
     *            所有的权限集合
     * @param grantResults
     *            授权结果集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    // 被用户拒绝的权限集合
                    List<String> deniedPermissions = new ArrayList<>();
                    // 用户通过的权限集合
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        // 获取授权结果，这是一个int类型的值
                        int grantResult = grantResults[i];

                        if (grantResult != PackageManager.PERMISSION_GRANTED) { // 用户拒绝授权的权限
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        } else { // 用户同意的权限
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()) { // 用户拒绝权限为空
                        onGranted();
                    } else { // 不为空
                        // 回调授权成功的接口
                        onDenied(deniedPermissions);
                        // 回调授权失败的接口
                        onGranted(grantedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

    //已经全部授权成功
    @Override
    public void onGranted() {

    }

    //已经授权成功的一些权限
    @Override
    public void onGranted(List<String> grantedPermission) {

    }

    //没有授权成功的一些权限
    @Override
    public void onDenied(List<String> deniedPermission) {
        for(int i=0;i<deniedPermission.size();i++){
            LogUtils.e("失败授权的有："+deniedPermission.get(i).toString());
        }
        DialogUtils.getInstance().showSelectDialog(this, "权限提示", "为了更好的为您服务，请授予应用" + deniedPermission.get(0) + "权限", "去开启", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PermissionPageUtils utils = new PermissionPageUtils(SuperSuperBaseActivity.this);
                utils.jumpPermissionPage();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DialogUtils.getInstance().showWarningTs(SuperSuperBaseActivity.this,"获取权限失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频选择回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    MediaListInfo listInfo = new MediaListInfo();
                    if (selectList != null) {
                        listInfo.getMedias().addAll(selectList);
                    }
                    listInfo.setRequestCode(PictureConfig.CHOOSE_REQUEST);
                    if (onMediaListInfoGetListener != null) {
                        onMediaListInfoGetListener.OnMediaInfoGet(listInfo);
                    }
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    // 图片、视频拍摄回调
                    List<LocalMedia> cselectList = PictureSelector.obtainMultipleResult(data);
                    MediaListInfo listInfo2 = new MediaListInfo();
                    if (cselectList != null) {
                        listInfo2.getMedias().addAll(cselectList);
                    }
                    listInfo2.setRequestCode(PictureConfig.REQUEST_CAMERA);
                    if (onMediaListInfoGetListener != null) {
                        onMediaListInfoGetListener.OnMediaInfoGet(listInfo2);
                    }
                    break;
            }
        }
    }

    OnMediaListInfoGetListener onMediaListInfoGetListener;

    public void setOnMediaListInfoGetListener(OnMediaListInfoGetListener onMediaListInfoGetListener) {
        this.onMediaListInfoGetListener = onMediaListInfoGetListener;
    }

    public interface OnMediaListInfoGetListener {
        void OnMediaInfoGet(MediaListInfo listInfo);
    }


    //如果有x5webview则需注册生命周期方法
    protected void initX5WebLive() {
        this.mWebView = findViewById(R.id.x5web_view);
        if(mWebView!=null) {
            WebSettings webSetting = mWebView.getSettings();
            webSetting.setSavePassword(true);
            webSetting.setSaveFormData(true);// 保存表单数据
            webSetting.setAllowUniversalAccessFromFileURLs(true);
            webSetting.setAllowFileAccess(true);
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSetting.setSupportZoom(true);
            webSetting.setBuiltInZoomControls(true);
            webSetting.setUseWideViewPort(true);
            webSetting.setSupportMultipleWindows(false);
            webSetting.setAppCacheEnabled(true);
            webSetting.setDomStorageEnabled(true);
            webSetting.setJavaScriptEnabled(true);
            webSetting.setGeolocationEnabled(true);
            webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
            webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
            webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
            webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                    .getPath());
            webSetting.setPluginState(WebSettings.PluginState.ON);
            long time = System.currentTimeMillis();
            TbsLog.d("time-cost", "cost time: "
                    + (System.currentTimeMillis() - time));
            CookieSyncManager.createInstance(this);
            CookieSyncManager.getInstance().sync();
        }
    }


    public int getFgSendMsgType() {
        return fgSendMsgType;
    }

    public void setFgSendMsgType(int fgSendMsgType) {
        this.fgSendMsgType = fgSendMsgType;
    }

    public SuperViewPagerSuperBaseFragment getCurrentFragment() {
        FragmentManager fragmentManager = SuperSuperBaseActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        Fragment fg = null;
        if (fragments != null && fragments.size() > 0) {
            for (int i = 0; i < fragments.size(); i++) {
                if (fragments.get(i) != null && fragments.get(i).isVisible()) {
                    fg = fragments.get(i);
                    break;
                }
            }
        }
        if(fg instanceof SuperViewPagerSuperBaseFragment) {
            return (SuperViewPagerSuperBaseFragment) fg;
        }else {
            return null;
        }
    }


    private void setLvListener() {
        SmartRefreshLayout sm = (SmartRefreshLayout) findViewById(R.id.smRf);
        if (sm != null) {
            sm.setOnRefreshListener(this);
            sm.setOnLoadmoreListener(this);
            sm.setEnableAutoLoadmore(true);
            sm.setEnableScrollContentWhenLoaded(true);//是否在加载完成时滚动列表显示新的内容
        }
    }

    protected void initTitle() {
        title = setTitle();
        if (!MyUtils.isEmpty(title)) {
            TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
            if (tvTitle != null) {
                tvTitle.setText(title);
            }
        }
    }


    private void initLeftClick() {
        FrameLayout fmLeft = (FrameLayout) findViewById(R.id.fm_left);
        if (fmLeft != null) {
            fmLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }


    public SharedPreferenceUtil getSharedPreferenceUtil() {
        if (sharedPreferenceUtil == null) {
            sharedPreferenceUtil = new SharedPreferenceUtil(this, getApplicationContext().getPackageName());
        }
        return sharedPreferenceUtil;
    }


    /**
     * 设置状态栏背景及字体颜色
     */
    public void setStatusStyle(int statusColorRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏底色白色
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(statusColorRes);
            // 设置状态栏字体黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    private void initNetWork() {
        IntentFilter intentFilter = new IntentFilter();
        //当前网络发生变化后，系统会发出一条值为android.net.conn.CONNECTIVITY_CHANGE的广播，所以要监听它
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netChangeReceiver = new NetStateReceiver();
        //进行注册
        registerReceiver(netChangeReceiver, intentFilter);
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onResponse(final BaseHttpInfo info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (info instanceof NetInfo) {
                    NetInfo netInfo = (NetInfo) info;
                    dealWithNet(netInfo);
                }
                SuperViewPagerSuperBaseFragment fragment = getCurrentFragment();
                //消息发送方式为含有Fragment的Ac中，只发送给当前fg，ac不接收消息
                if (fgSendMsgType == OnlySendToCurrentFg) {
                    if (fragment != null) {
                        fragment.onActivityMsgToHere(info);
                    }
                    //消息发送方式为含有Fragment的Ac中，只发送给当前Ac，fg不接收消息
                } else if (fgSendMsgType == OnlySendToCurrentAc) {
                    onMsgHere(info);
                } else {
                    //消息发送方式为含有Fragment的Ac中,两者均接收消息
                    onMsgHere(info);
                    if (fragment != null) {
                        fragment.onActivityMsgToHere(info);
                    }
                }
            }
        });
    }

    /**
     * 处理断网
     *
     * @param netInfo
     */
    private void dealWithNet(NetInfo netInfo) {

    }


    public void showTs(String msg) {
        if (toast == null) {
            toast = new ToastUtil(this, msg);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public String getPhoneType() {
        return Build.BRAND + "-" + Build.MODEL;
    }


    protected boolean setIsExitActivity() {
        return false;
    }

    /**
     * 重写onBackPressed监听返回，统一设置了返回动画
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!isMainActivity) {
            overridePendingTransition(R.anim.new_to_right, R.anim.old_to_right);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (mWebView != null) {
            mWebView.onResume();
        }
        if(bannerView!=null){
            bannerView.start();
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
            ViewGroup view = (ViewGroup) getWindow().getDecorView();
            view.removeAllViews();
            mWebView.destroy();
        }
        unregisterReceiver(netChangeReceiver);
        //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
        PictureFileUtils.deleteCacheDirFile(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (mWebView != null) {
            mWebView.onPause();
        }
        if(bannerView!=null){
            bannerView.pause();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    public String setTitle() {
        return null;
    }

    public abstract int setLayout();

    public abstract void onMsgHere(BaseHttpInfo info);

    protected abstract void initFirst();



    /**
     * 重写startActivity，统一设置启动Activity的动画
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.new_to_left, R.anim.old_to_left);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                    if (Integer.parseInt(Build.VERSION.SDK) >= 16)
                        return true;
            } else {
                if (isMainActivity) {
                        appExit();
                        return false;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void appExit() {
        AppExit2Back.exitApp(this);
    }


    /**
     * 获取版本号
     *
     * @return
     */
    public int getVersionNum() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }


    /**
     * 获取版本名称
     *
     * @return
     */
    public String getVersionName() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String versionName = info.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }


    /**
     * 获取应用名称
     *
     * @return
     */
    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }


    /**
     * 获取唯一设备Id
     *
     * @return
     */
    public String getPhoneId() {
        //权限校验
        return getUniquePsuedoID();
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 显示转圈的Dialog
     *
     * @return
     */
    public Dialog showLoadingDialog(String msg, boolean canDismiss) {
        closeDialog();
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(this, R.style.MyDialogStyle);// 创建自定义样式dialog
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
        this.loadingDialog = loadingDialog;
        return loadingDialog;
    }


    /**
     * 关闭dialog
     */
    public void closeDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            this.loadingDialog.dismiss();
            this.loadingDialog = null;
        }
    }





    public void sendMsgBase(){

    }

    public  void loadMoreOvr(RefreshLayout refreshlayout){
        _page ++;
        sendMsgBase();
    }

    public  void refeshOvr(RefreshLayout refreshlayout){
        _page = 1;
        sendMsgBase();
    }
    @Override
    public void onRefresh(final RefreshLayout refreshlayout) {
        smState=REFRESH;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refeshOvr(refreshlayout);
            }
        }, MsgDelayTime);
    }


    @Override
    public void onLoadmore(final RefreshLayout refreshlayout) {
        smState=LOADMORE;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMoreOvr(refreshlayout);
            }
        }, MsgDelayTime);
    }

}
