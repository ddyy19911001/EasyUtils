package yin.deng.dyutils.data_base_flow;

import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/12.
 * deng yin
 */
public  class DbMainHelper<T extends BaseModel> {
    Class<T> dbInfo;

    public DbMainHelper(Class<T> x) {
        this.dbInfo = x;
    }


    public void saveSingleInfo(T info) {
        info.insert();
    }

    public void updateSingleInfo(T info) {
        info.update();
    }

    public void deleteSingleInfo(T info) {
        info.delete();
    }


    public void deleteListWhereEqInt(Property<Integer> x, int y){
        List<T> list = findAllListByEqInt(x, y);
        if(list==null||list.size()==0){
            return;
        }
        for(int i=0;i<list.size();i++){
            list.get(i).delete();
        }
    }

    public void deleteListWhereEqString(Property<String> x, String y){
        List<T> list = findAllListByEqString(x, y);
        if(list==null||list.size()==0){
            return;
        }
        for(int i=0;i<list.size();i++){
            list.get(i).delete();
        }
    }

    public void deleteListWhereEqLong(Property<Long> x, Long y){
        List<T> list = findAllListByEqLong(x, y);
        if(list==null||list.size()==0){
            return;
        }
        for(int i=0;i<list.size();i++){
            list.get(i).delete();
        }
    }


    public void deleteListWhereEqBoolean(Property<Boolean> x, Boolean y){
        List<T> list = findAllListByEqBoolean(x, y);
        if(list==null||list.size()==0){
            return;
        }
        for(int i=0;i<list.size();i++){
            list.get(i).delete();
        }
    }

    public void saveListInfoInUiThread(final List<T> list){
        if(list==null){
            Log.e("exc","保存失败，保存的集合为空！");
            return;
        }
        ProcessModelTransaction<T> processModelTransaction = new ProcessModelTransaction.Builder<T>(new ProcessModelTransaction.ProcessModel<T>() {
            @Override
            public void processModel(T o, DatabaseWrapper wrapper) {
                o.insert();
            }
        }).addAll(list).build();
        //批量插入
        //同步事务
        FlowManager.getDatabase(SuperAppDataBase.class)
                .executeTransaction(processModelTransaction);
    }





    //删除
    public synchronized void clearAllTable() {
        SQLite.delete().from(dbInfo).execute();
    }


    /**
     * @return list
     */
    public List<T> findAllList() {
        List<T> baseInfos = new Select().from(dbInfo).queryList();
        if (baseInfos == null) {
            baseInfos = new ArrayList<>();
        }
        return baseInfos;
    }


    /**
     * @param x BaseInfo_Table.id
     * @param y id
     * @return list
     */
    public List<T> findAllListByEqLong(Property<Long> x, long y) {
        List<T> baseInfos = new Select().from(dbInfo).where(x.is(y)).queryList();
        if (baseInfos == null) {
            baseInfos = new ArrayList<>();
        }
        return baseInfos;
    }

    /**
     * @param x BaseInfo_Table.id
     * @param y id
     * @return list
     */
    public List<T> findAllListByEqString(Property<String> x, String y) {
        List<T> baseInfos = new Select().from(dbInfo).where(x.eq(y)).queryList();
        if (baseInfos == null) {
            baseInfos = new ArrayList<>();
        }
        return baseInfos;
    }

    /**
     * @param x BaseInfo_Table.id
     * @param y id
     * @return list
     */
    public List<T> findAllListByEqInt(Property<Integer> x, int y) {
        List<T> baseInfos = new Select().from(dbInfo).where(x.eq(y)).queryList();
        if (baseInfos == null) {
            baseInfos = new ArrayList<>();
        }
        return baseInfos;
    }

    /**
     * @param x BaseInfo_Table.id
     * @param y id
     * @return list
     */
    public List<T> findAllListByEqBoolean(Property<Boolean> x, boolean y) {
        List<T> baseInfos = new Select().from(dbInfo).where(x.eq(y)).queryList();
        if (baseInfos == null) {
            baseInfos = new ArrayList<>();
        }
        return baseInfos;
    }


    /**
     * 替换或新增拥有某个属性的数据
     *
     * @param x 匹配的属性
     * @param y 属性值条件
     * @param t 对象
     */
    public void replaceOrAddByEqInt(Property<Integer> x, int y, T t,OnSingleFindListener listener) {
        T info = new Select().from(dbInfo).where(x.eq(y)).querySingle();
        if (info != null) {
            if(listener!=null){
                listener.onFind(info);
            }else{
                t.insert();
            }
        }
    }

    /**
     * 替换或新增拥有某个属性的数据
     *
     * @param x 匹配的属性
     * @param y 属性值条件
     * @param t 对象
     */
    public void replaceOrAddByEqString(Property<String> x, String y, T t,OnSingleFindListener listener) {
        T info = new Select().from(dbInfo).where(x.eq(y)).querySingle();
        if (info != null) {
            if(listener!=null){
                listener.onFind(info);
            }else{
                t.insert();
            }
        }
    }


    /**
     * 替换或新增拥有某个属性的数据
     *
     * @param x 匹配的属性
     * @param y 属性值条件
     * @param t 对象
     */
    public void replaceOrAddByEqBoolean(Property<Boolean> x, Boolean y, T t,OnSingleFindListener listener) {
        T info = new Select().from(dbInfo).where(x.eq(y)).querySingle();
        if (info != null) {
            if(listener!=null){
                listener.onFind(info);
            }else{
                t.insert();
            }
        }
    }

    /**
     * 替换或新增拥有某个属性的数据
     *
     * @param x 匹配的属性
     * @param y 属性值条件
     * @param t 对象
     */
    public void replaceOrAddByEqLong(Property<Long> x, Long y, T t,OnSingleFindListener listener) {
        T info = new Select().from(dbInfo).where(x.eq(y)).querySingle();
        if (info != null) {
            if(listener!=null){
                listener.onFind(info);
            }else{
                t.insert();
            }
        }
    }

    public  interface OnSingleFindListener<T>{
         void onFind(T t);
    }


}
