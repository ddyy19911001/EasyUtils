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
	        implementation 'com.github.ddyy19911001:EasyUtils:1.0'
	}
# Step 3. add bottom in gradle.properties

	android.useAndroidX=true
	android.enableJetifier=true

# Step 4. add these in main project's build.gradle

	android{
	....
	defaultConfig {
        ndk { abiFilters "armeabi" }
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
	packagingOptions {//加上这些代码
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



