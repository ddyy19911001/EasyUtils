package yin.deng.dyutils.http;

import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

public abstract class InfoCallBack<T> implements Callback<T> {
    @Override
    public void onStart(Request<T, ? extends Request> request) {

    }

    @Override
    public abstract void onSuccess(Response<T> response);

    @Override
    public void onCacheSuccess(Response<T> response) {

    }

    @Override
    public abstract void onError(Response<T> response);

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadProgress(Progress progress) {

    }

    @Override
    public void downloadProgress(Progress progress) {

    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        return null;
    }
}
