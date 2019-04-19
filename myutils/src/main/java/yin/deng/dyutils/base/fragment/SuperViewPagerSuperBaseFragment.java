package yin.deng.dyutils.base.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.utils.TbsLog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import yin.deng.dyutils.R;
import yin.deng.dyutils.base.SuperBaseConfig;
import yin.deng.dyutils.base.SuperBaseApp;
import yin.deng.dyutils.http.BaseHttpInfo;
import yin.deng.dyutils.http.MyHttpUtils;
import yin.deng.dyutils.mzbanner.MZBannerView;
import yin.deng.dyutils.refresh.SmartRefreshLayout;
import yin.deng.dyutils.refresh.api.RefreshLayout;
import yin.deng.dyutils.refresh.listener.OnLoadmoreListener;
import yin.deng.dyutils.refresh.listener.OnRefreshListener;
import yin.deng.dyutils.tips.ToastUtil;
import yin.deng.dyutils.utils.SharedPreferenceUtil;
import yin.deng.dyutils.web.X5WebView;

/**
 * 这个fragment是android.V4中的fragment不是android.app包里面的Fragment
 */
public abstract class SuperViewPagerSuperBaseFragment<T> extends Fragment implements SuperBaseConfig,OnRefreshListener,OnLoadmoreListener {
    private static final String TAG = SuperViewPagerSuperBaseFragment.class.getSimpleName();
    private boolean isFragmentVisible;
    private boolean isReuseView;
    private boolean isFirstVisible;
    private View rootView;
    int Res;
    View view;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private ToastUtil toast;
    public static final int REFRESH=0;
    public static final int LOADMORE=1;
    public int smState=REFRESH;
    public List<T> _infos=new ArrayList<>();
    public int _size;
    public MyHttpUtils httpUtils;
    private X5WebView mWebView;
    private MZBannerView bannerView;

    public SharedPreferenceUtil getSharedPreferenceUtil(){
        if(sharedPreferenceUtil==null){
            sharedPreferenceUtil = SuperBaseApp.app.getSharedPreferenceUtil();
        }
        return sharedPreferenceUtil;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Res=setContentView();
        View view = inflater.inflate(Res, container, false);
        bindViewWithId(view);
        setLvListener(view);
        //如果有用到x5webview会进行初始化设置
        initX5WebLive(view);
        //如果有用到bannerview会进行初始化设置
        initBannerView(view);
        this.view=view;
        return view;
    }

    public void bindViewWithId(View view) {

    }

    //setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
    //如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
    //如果Fragment从可见-&gt;不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
    //总结：setUserVisibleHint()除了Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
    //如果我们需要在 Fragment 可见与不可见时干点事，用这个的话就会有多余的回调了，那么就需要重新封装一个
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //setUserVisibleHint()有可能在fragment的生命周期外被调用
        if (rootView == null) {
            return;
        }
        if (isFirstVisible && isVisibleToUser) {
            onFragmentFirstVisible();
            isFirstVisible = false;
        }
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            isFragmentVisible = false;
            onFragmentVisibleChange(false);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //如果setUserVisibleHint()在rootView创建前调用时，那么
        //就等到rootView创建完后才回调onFragmentVisibleChange(true)
        //保证onFragmentVisibleChange()的回调发生在rootView创建完成之后，以便支持ui操作
        if (rootView == null) {
            rootView = view;
            if (getUserVisibleHint()) {
                if (isFirstVisible) {
                    onFragmentFirstVisible();
                    isFirstVisible = false;
                }
                onFragmentVisibleChange(true);
                isFragmentVisible = true;
            }
        }
        super.onViewCreated(isReuseView ? rootView : view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initVariable();
    }

    private void initVariable() {
        isFirstVisible = true;
        isFragmentVisible = false;
        rootView = null;
        isReuseView = true;
    }

    /**
     * 设置是否使用 view 的复用，默认开启
     * view 的复用是指，ViewPager 在销毁和重建 Fragment 时会不断调用 onCreateView() -&gt; onDestroyView()
     * 之间的生命函数，这样可能会出现重复创建 view 的情况，导致界面上显示多个相同的 Fragment
     * view 的复用其实就是指保存第一次创建的 view，后面再 onCreateView() 时直接返回第一次创建的 view
     *
     * @param isReuse
     */
    protected void reuseView(boolean isReuse) {
        isReuseView = isReuse;
    }

    /**
     * 去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
     * 回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     *
     * 可在该回调方法里进行一些ui显示与隐藏，比如加载框的显示和隐藏
     *
     * @param isVisible true  不可见 -&gt; 可见
     *                  false 可见  -&gt; 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    /**
     * 在fragment首次可见时回调，可在这里进行加载数据，保证只在第一次打开Fragment时才会加载数据，
     * 这样就可以防止每次进入都重复加载数据
     * 该方法会在 onFragmentVisibleChange() 之前调用，所以第一次打开时，可以用一个全局变量表示数据下载状态，
     * 然后在该方法内将状态设置为下载状态，接着去执行下载的任务
     * 最后在 onFragmentVisibleChange() 里根据数据下载状态来控制下载进度ui控件的显示与隐藏
     */
    protected void onFragmentFirstVisible() {

    }

    public  void loadMoreOvr(RefreshLayout refreshlayout){
        _size = _infos.size();
        sendMsgBase();
    }

    public  void refeshOvr(RefreshLayout refreshlayout){
        _size = _infos.size();
        sendMsgBase();
    }

    protected boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    public MyHttpUtils getHttpUtils() {
        if(httpUtils==null){
            httpUtils=new MyHttpUtils(getActivity().getApplication());
        }
        return httpUtils;
    }



    //如果有x5webview则需注册生命周期方法
    protected void initX5WebLive(View view) {
        this.mWebView = view.findViewById(R.id.x5web_view);
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
            webSetting.setAppCachePath(getActivity().getDir("appcache", 0).getPath());
            webSetting.setDatabasePath(getActivity().getDir("databases", 0).getPath());
            webSetting.setGeolocationDatabasePath(getActivity().getDir("geolocation", 0)
                    .getPath());
            webSetting.setPluginState(WebSettings.PluginState.ON);
            long time = System.currentTimeMillis();
            TbsLog.d("time-cost", "cost time: "
                    + (System.currentTimeMillis() - time));
            CookieSyncManager.createInstance(getActivity());
            CookieSyncManager.getInstance().sync();
        }
    }

    /**
     * 初始化bannerview
     */
    private void initBannerView(View view) {
        bannerView = view.findViewById(R.id.banner);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
        if(bannerView!=null){
            bannerView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
        if(bannerView!=null){
            bannerView.start();
        }
    }

    private void setLvListener(View view) {
        SmartRefreshLayout sm= (SmartRefreshLayout) view.findViewById(R.id.smRf);
        if(sm!=null){
            sm.setOnRefreshListener(this);
            sm.setOnLoadmoreListener(this);
        }
    }

    protected abstract int setContentView();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    public abstract void onActivityMsgToHere(BaseHttpInfo info);

    public void showTs(String msg) {
        if (toast == null) {
            toast = new ToastUtil(getActivity(),msg);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }




    public View getRootView(){
        return view;
    }


    public void sendMsgBase(){

    }




    @Override
    public void onRefresh(final RefreshLayout refreshlayout) {
        smState=REFRESH;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                _infos.clear();
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


    /**
     * 初始化数据可写在这里
     */
    protected abstract void init();

}
