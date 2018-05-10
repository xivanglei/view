package xl.gcs.com.viewpager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by xianglei on 2018/3/10.
 */

public class PageAdapter extends ViewPager.PagerAdapter {

    private List<ImageView> mImageList;

    public PageAdapter(List<ImageView> imageList) {
        mImageList = imageList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;       //设置成最大，就看不到边界效果了
//        return mImageList.size();      //返回数量，最后一张再移动就会看到边界效果
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        ImageView imageView = mImageList.get(position);
//        container.addView(imageView);
//        return imageView;

        //对ViewPager页号求模取出View列表中要显示的项
        //由于我们设置了count为 Integer.MAX_VALUE，因此这个position的取值范围很大很大，但我们实际要显示的内容肯定没这么多（往往只有几项），
        // 所以这里肯定会有求模操作。但是，简单的求模会出现问题：考虑用户向左滑的情形，则position可能会出现负值。所以我们需要对负值再处理一次，
        // 使其落在正确的区间内。
        int a = position;
        position %= mImageList.size();
        if (position<0){
            position = mImageList.size()+position;
        }
        ImageView view = mImageList.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp =view.getParent();
        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }
        container.addView(view);
        //add listeners here if necessary
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(mImageList.get(position));      //为了实现无限轮播，就不调用remove了
    }


    //下面介绍碎片的适配器
//    FragmentPagerAdapter适合页面较少，不会经常destroy FragmentStatePagerAdapter适合多页面，不用的会destroy
    private class ContentPagerAdapter extends FragmentStatePagerAdapter {
//    private class ContentPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> mFragmentsList;
        public ContentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            mFragmentsList = fragmentList;
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentsList.size();
        }
    }
}
