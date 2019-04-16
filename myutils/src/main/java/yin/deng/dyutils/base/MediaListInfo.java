package yin.deng.dyutils.base;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import yin.deng.dyutils.http.BaseHttpInfo;

/**
 * Created by Administrator on 2019/4/1 0001.
 */
public class MediaListInfo extends BaseHttpInfo {
    private List<LocalMedia> medias=new ArrayList<>();
    private int requestCode;

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public List<LocalMedia> getMedias() {
        return medias;
    }

    public void setMedias(List<LocalMedia> medias) {
        this.medias = medias;
    }
}
