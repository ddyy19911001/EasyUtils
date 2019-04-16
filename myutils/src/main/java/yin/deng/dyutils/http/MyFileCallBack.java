package yin.deng.dyutils.http;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.FileConvert;

import java.io.File;

import okhttp3.Response;

/**
 * Created by Administrator on 2019/3/31 0031.
 */
public abstract class MyFileCallBack extends AbsCallback<File> {
    private FileConvert convert;    //文件转换类

    public MyFileCallBack() {
        this(null);
    }

    public MyFileCallBack(String destFileName) {
        this(null, destFileName);
    }

    public MyFileCallBack(String destFileDir, String destFileName) {
        convert = new FileConvert(destFileDir, destFileName);
        convert.setCallback(this);
    }

    @Override
    public File convertResponse(Response response) throws Throwable {
        File file = convert.convertResponse(response);
        response.close();
        return file;
    }

}
