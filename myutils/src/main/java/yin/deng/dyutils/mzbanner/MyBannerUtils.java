package yin.deng.dyutils.mzbanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import yin.deng.dyutils.R;
import yin.deng.dyutils.mzbanner.holder.MZHolderCreator;
import yin.deng.dyutils.mzbanner.holder.MZViewHolder;
import yin.deng.dyutils.pictureUtils.PicassoUtils;
import yin.deng.dyutils.utils.MyUtils;

public class MyBannerUtils  {
    private static final int DElAY_TIME = 4000;
    private static final int SPEED_TIME_OF_ANIMATE = 1000;

    private MyBannerUtils() {
    }

    public static MyBannerUtils initBannerUtils(MZBannerView mMZBanner, List<SuperBannerInfo> datas) {
            MyBannerUtils utils = new MyBannerUtils();
            mMZBanner.setIndicatorAlign(MZBannerView.IndicatorAlign.CENTER);
            mMZBanner.setIndicatorPadding(0,0,0,10);
            mMZBanner.setCanLoop(true);
            mMZBanner.setDelayedTime(DElAY_TIME);
            mMZBanner.setDuration(SPEED_TIME_OF_ANIMATE);
            mMZBanner.setIndicatorVisible(true);
            mMZBanner.setPages(datas, new MZHolderCreator<BannerViewHolder>() {
                @Override
                public BannerViewHolder createViewHolder() {
                    return new BannerViewHolder();
                }
            });
            mMZBanner.start();
        return utils;
    }

    public static MyBannerUtils initBannerUtils(MZBannerView mMZBanner, List<SuperBannerInfo> datas,MZBannerView.BannerPageClickListener listener) {
        MyBannerUtils utils = new MyBannerUtils();
        mMZBanner.setIndicatorAlign(MZBannerView.IndicatorAlign.CENTER);
        mMZBanner.setIndicatorPadding(0,0,0,10);
        mMZBanner.setCanLoop(true);
        mMZBanner.setDelayedTime(DElAY_TIME);
        mMZBanner.setDuration(SPEED_TIME_OF_ANIMATE);
        mMZBanner.setIndicatorVisible(true);
        mMZBanner.setBannerPageClickListener(listener);
        mMZBanner.setPages(datas, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        mMZBanner.start();
        return utils;
    }

    public static MyBannerUtils initBannerUtils(MZBannerView mMZBanner, List<SuperBannerInfo> datas,MZBannerView.BannerPageClickListener listener,MZHolderCreator creator) {
        MyBannerUtils utils = new MyBannerUtils();
        mMZBanner.setIndicatorAlign(MZBannerView.IndicatorAlign.CENTER);
        mMZBanner.setIndicatorPadding(0,0,0,10);
        mMZBanner.setCanLoop(true);
        mMZBanner.setDelayedTime(DElAY_TIME);
        mMZBanner.setDuration(SPEED_TIME_OF_ANIMATE);
        mMZBanner.setIndicatorVisible(true);
        mMZBanner.setBannerPageClickListener(listener);
        mMZBanner.setPages(datas, creator);
        mMZBanner.start();
        return utils;
    }


    public static class BannerViewHolder implements MZViewHolder<SuperBannerInfo> {
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, SuperBannerInfo data) {
            if(data.getResPic()!=0) {
                // 数据绑定
                mImageView.setImageResource(data.getResPic());
            }else if(!MyUtils.isEmpty(data.getPicUrl())){
                PicassoUtils.getinstance().LoadImage(context,data.getPicUrl(),mImageView,R.drawable.image_replace,R.drawable.image_replace,PicassoUtils.PICASSO_BITMAP_SHOW_NORMAL_TYPE,0);
            }
        }
    }
}
