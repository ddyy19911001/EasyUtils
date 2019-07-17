# EasyUtils

适用于andoirdx版本的开发工具utils

# 如何使用

To get a Git project into your build:

# Step 1. Add the JitPack repository to your build file

gradle
maven
sbt
leiningen
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
# Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.ddyy19911001:EasyUtils:1.0.3'
	}
# Step 3. add bottom in gradle.properties

	android.useAndroidX=true
	android.enableJetifier=true

# Step 4. add these in main project's build.gradle

	android{
	....
	defaultConfig {
        ndk{abiFilters "armeabi", "armeabi-v7a", "x86", "mips"}
        //注意添加到主项目的gradle设置中
        //图片选择框架需要添加这两句
        javaCompileOptions {
            annotationProcessorOptions {
                includeCompileClasspath true
            }
        }
        //dialog框架需要添加这两句
        renderscriptTargetApi 19
        renderscriptSupportModeEnabled true
	
	....
	
	
	//去除so包冲突
	packagingOptions {
  	   pickFirst 'lib/armeabi-v7a/libRSSupport.so'
   	   pickFirst 'lib/arm64-v8a/libRSSupport.so'
  	   pickFirst 'lib/x86_64/libRSSupport.so'
 	   pickFirst 'lib/x86/libRSSupport.so'
 	   pickFirst 'lib/arm64-v8a/librsjni.so'
  	   pickFirst 'lib/x86/librsjni.so'
  	   pickFirst 'lib/x86_64/librsjni.so'
 	   pickFirst 'lib/armeabi-v7a/librsjni.so'
 	   pickFirst 'lib/x86_64/librsjni_androidx.so'
 	   pickFirst 'lib/armeabi-v7a/librsjni_androidx.so'
 	   pickFirst 'lib/x86/librsjni_androidx.so'
 	   pickFirst 'lib/arm64-v8a/librsjni_androidx.so'
	
	}	

# 主要API

打开源码查看，有各自功能的分包

主要用到的有SuperBaseActivity/SuperBaseFragment/SuperBaseApp这三个需要被继承(加入泛型便于使用刷新和加载功能)

请求只需getHttpUtils（Bean类需要继承BaseHttpInfo）

请求权限（继承SuperBaseActivity）requestRunTimePermission

需要使用x5webview将id设置为x5web_view（可以直接include一个default的x5webview）

数据库使用Bean类上面需要添加@Table(database = SuperAppDataBase.class)
各个键值需要添加 @Column 主键添加 @PrimaryKey(autoincrement = true) 
增删改查创建DbMainHelper<T>即可，需要先进行编译
	
弹框通知等工具使用DialogUtils的静态方法

存储SD卡获取文件空间，保存文件，获取文件路径名字等等各类和文件相关的都用MyFileUitls

需要Banner的地方，只需要在布局文件中使用MZBannerView并将其id设置为banner（会自动调用其生命周期方法），然后在Activity初始化的时候调用MyBannerUtils.initBannerUtils()即可创建出banner并同时启动banner（可以直接include一个default_banner_view）

选择图片或者视频的时候可以用PhotoUtils的静态方法，回调在SuperSuperBaseActivity的onActivityResult中，需要在activity中设置
setOnMediaListInfoGetListener来进行回调后的操作。

批量压缩图片工具CompressPhotoUtils，也可以获取sd卡的图片再将其压缩后的图片返回，图片压缩的必备神器

自动上啦下拉刷新框架，只需在布局中创建include(smart_layout_rc)所有刷新的布局统一使用recycleView
可以重写onRefresh和onLoadMore、或者重新onRefreshOvr/onLoadMoreOvr(最后执行的)

ShortCutUtils快捷方式生成工具

TabUtils可以用于添加头部的Tab，这个工具需要配合fragment使用，用布局上面可以用default_tab_layout

TagUtils配合布局上面用default_tag_layout,获取其中的TagFlowLayout再用其中的方法设置各类型的标签

SnakeBarUtils和ToastUtils都可以显示消息

BottomNavigationUtils首页底部的几个按钮，快速创建底部菜单（在布局文件中可以使用default_navigation_layout）

RecycleViewScrollDistanceUtils这个工具可以用来计算RecycleView的滑动距离便于做一些头部布局的隐藏和显示操

MyUtils可以用于验证空和显示吐司和显示加载中的转圈动

NetUtils用于做网络状态判断

ScreenUtils用于做屏幕分辨率和键盘弹出收回操作

CircleImageView显示圆形图片

JsoupUtils可以直接获取到Doctment

# 如果要用到refreshHeader可以设置静态默认的header

 	 static {
       	 //设置全局的Header构建器
      	 SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.normal_bg, R.color.normal_4a);//全局设置主题颜色
                MyHeaderView head = new MyHeaderView(context);
                return head;//指定为经典Header，默认是 贝塞尔雷达Header
            }
    	    });
   	   //设置全局的Footer构建器
     	   SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                layout.setPrimaryColorsId(R.color.normal_bg, R.color.normal_4a);//全局设置主题颜色
                MyFooterView footer = new MyFooterView(context);
                return footer;
      	      }
      	  });
   	  }
# 数据库相关

可以参考
https://guides.codepath.com/android/DBFlow-Guide

