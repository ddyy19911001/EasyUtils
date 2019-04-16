package yin.deng.dyutils.base;



/**
 * Created by Administrator on 2018/4/25.
 * deng yin
 */
public interface SuperBaseConfig {
    String tag="dyTag";
    String sharedprefrenceName="MySaveData";
    String downLoadDirName="MyDownLoad";
    String DB_NAME="MyDb";
    //消息发送延迟
    long MsgDelayTime=500;
    int wifi=0;
    int moblie=1;
    int noNet=-1;
    String downLoadUrl="";
}
