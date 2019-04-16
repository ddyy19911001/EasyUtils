package yin.deng.dyutils.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import yin.deng.dyutils.utils.LogUtils;

public class X5WebView extends WebView {
	Context context;
	private WebViewClient client = new WebViewClient() {
		/**
		 * 防止加载网页时调起系统浏览器
		 */
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	};

	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		this.setWebViewClient(client);
		// this.setWebChromeClient(chromeClient);
		// WebStorage webStorage = WebStorage.getInstance();
		initWebViewSettings();
		this.context=arg0;
		this.getView().setClickable(true);
	}

	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
//		webSetting.setMediaPlaybackRequiresUserGesture(false);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(true);
		webSetting.setAppCacheEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, String url) {
				if (url.startsWith("http:") || url.startsWith("https:")) {
					return false;
				}
				try {
					LogUtils.i("action:--->"+url);
					final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					context.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		});
	}


	public X5WebView(Context arg0) {
		super(arg0);
		setBackgroundColor(85621);
	}


	public void enableX5FullscreenFunc(int type) {

		if (getX5WebViewExtension() != null) {
			Bundle data = new Bundle();

			data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

			data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

			data.putInt("DefaultVideoScreen", type);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

			getX5WebViewExtension().invokeMiscMethod("setVideoParams",
					data);
		}
	}


}
