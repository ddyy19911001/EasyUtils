package yin.deng.dyutils.data_base_flow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import yin.deng.dyutils.http.BaseHttpInfo;

/**
 * Created by Administrator on 2019/1/6 0006.
 * 需要类继承自BaseModel才可以使用DmMainHelper,或者调用类自身的保存方法，注意不能保存List<Object>类型，但可以保存String类型
 */
@Table(database = SuperAppDataBase.class)
public class DemoInfo extends BaseHttpInfo {
    @Column
    @PrimaryKey(autoincrement = true)
    public long _id;
    @Column
    public String movieTitle;
    @Column
    public String movieUrl;
    @Column
    public String playTime;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieUrl() {
        return movieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    @Override
    public String toString() {
        return "DemoInfo{" +
                "_id=" + _id +
                ", movieTitle='" + movieTitle + '\'' +
                ", movieUrl='" + movieUrl + '\'' +
                ", playTime='" + playTime + '\'' +
                '}';
    }
}
