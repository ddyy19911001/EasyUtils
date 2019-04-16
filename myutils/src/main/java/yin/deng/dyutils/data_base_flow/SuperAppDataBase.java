package yin.deng.dyutils.data_base_flow;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Administrator on 2017/12/11.
 * deng yin
 */
@Database(name = SuperAppDataBase.NAME, version = SuperAppDataBase.VERSION)
public  class SuperAppDataBase {
    //数据库名称
    public static final String NAME = "SuperAppDataBase";
    //数据库版本号
    public static final int VERSION = 1;

}
