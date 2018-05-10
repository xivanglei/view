package xl.gcs.com.viewpager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xl.gcs.com.viewpager.base.BaseActivity;
import xl.gcs.com.viewpager.base.BaseHttpActivity;

/*
ViewPager里面的最长滚动时间， MAX_SETTLE_DURATION，可以随便改
里面的PagerAdapter需要复制过去，主要是里面用的是系统的ViewPager
ViewPager需要复制过去，里面改了系统的几个参数
PagerAdapter如果要无限，就要有不同的设置，而且Fragment估计不行，只是轮播的话正常设置就好
 */
public class MainActivity extends BaseActivity {

    //git 学习中
    @BindView(R.id.main_viewpager)
    VerticalViewPager mViewPager;       //纵向ViewPager
    @BindView(R.id.main_linear)
    LinearLayout mLinearLayout;         //自定义指示器，可以加载自定义背景图，要自己设置宽高和margin属性
    PageAdapter mAdapter;

    List<ImageView> mImageList = new ArrayList<>();
    int mNum = 0;

    //加载图片
    private int[] mPicPages = new int[]{R.drawable.ic_launcher_background, R.mipmap.ic_launcher, R.drawable.ic_launcher_foreground};
    //初始化ImageHandler，自定义Handler主要是希望传入MainActivity的弱引用，具体作用下面有解释，为什么用这个，还不清楚
    //如果不用这个弱引用，用系统的Handler就好了
    private ViewPager.ImageHandler handler = new ViewPager.ImageHandler(new WeakReference<MainActivity>(this));
    @Override
    protected int getLayoutById() {
        return R.layout.activity_main;
    }
    @Override
    protected void initData(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();


            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);  //前面标志是布局全屏包括状态栏，后面标志是，状态栏不取消
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initAdapter();
    }
    private void initAdapter() {
        getData();
        mAdapter = new PageAdapter(mImageList);
        mViewPager.setAdapter(mAdapter);
        //加动画，自定义的
//        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        //设置滚动事件，最多两秒
//        handler.setDuration(2000);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //配合Adapter的currentItem字段进行设置。
            @Override
            public void onPageSelected(int arg0) {
                handler.sendMessage(Message.obtain(handler, ViewPager.ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
                int page = arg0 % mImageList.size();
                //原来的显示项设为不选中
                mLinearLayout.getChildAt(mNum).setSelected(false);
                //现在选的这个激活
                mLinearLayout.getChildAt(page).setSelected(true);
                mNum = page;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            //覆写该方法实现轮播效果的暂停和恢复
            @Override
            public void onPageScrollStateChanged(int arg0) {
                switch (arg0) {
                    //监听在拖拽中的状态
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        //发送消息，让他暂停轮播
                        handler.sendEmptyMessage(ViewPager.ImageHandler.MSG_KEEP_SILENT);
                        break;
                    //监听没有拖拽的动作
                    case ViewPager.SCROLL_STATE_IDLE:
                        //发送消息可以继续轮播了，应该就是这里跟发送消息重复了，所以下面需要移除先
                        handler.sendEmptyMessageDelayed(ViewPager.ImageHandler.MSG_UPDATE_IMAGE, ViewPager.ImageHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);//默认在中间，使用户看不到边界，因为第一页有边界，就不能左移了
        //开始轮播效果
//        handler.sendEmptyMessageDelayed(ViewPager.ImageHandler.MSG_UPDATE_IMAGE, ViewPager.ImageHandler.MSG_DELAY);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    private void getData() {
        ImageView imageView;
        View view;
        //创建ImageView，加入List
        for (int pic : mPicPages) {
            imageView = new ImageView(MainActivity.this);
            imageView.setBackgroundResource(pic);
            mImageList.add(imageView);

            view = new View(MainActivity.this);
            //设置单个指示器，背景颜色可以自己做
            view.setBackgroundResource(R.drawable.bg_selected_h2_w21);

            //这条在无限轮播里暂时没用
//            view.setSelected(false);
            //设置指示器宽高和Margin等的参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(42, 4);
            //如果不是第一个view
            if (pic != mPicPages[0]) {
                if (pic == mPicPages[mPicPages.length - 1]) {
                    //如果是最后一个，设置好最后的间距
//                    layoutParams.rightMargin = 150;
                }
                //左边间距除了第一个都要设置，没间距就贴紧了，很难看
                layoutParams.leftMargin = 20;
            }
            mLinearLayout.addView(view, layoutParams);
            mLinearLayout.getChildAt(0).setEnabled(true);
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();
            if (position < -1) { // [-Infinity,-1)
                // 页面远离左侧页面
                page.setAlpha(0);                                       //反正看不见，1也没用
            } else if (position <= 0) { // [-1,0]
                // 页面在由中间页滑动到左侧页面 或者 由左侧页面滑动到中间页
                page.setAlpha(1);
                page.setTranslationX(0);                                //差不多是滑完之后位置的改变 如果设为100，就明显看到往右偏了一个位置
                page.setScaleX(1);                                  //缩放比例，x,y都是1，说明正常大小，全部显示，2就是大一倍了，图片显示不下
                page.setScaleY(1);
            } else if (position <= 1) { // (0,1]

                //页面在由中间页滑动到右侧页面 或者 由右侧页面滑动到中间页
                // 淡入淡出效果
                page.setAlpha(1 - position);                        //动画效果，越到中间越浓，最中间就是1-0，就是1了，就是完全显示了
                // 反方向移动
                //不用无限轮播功能没关系，如果用了无限轮播，就要把这句注释掉，否则左滑显示有问题
                page.setTranslationY(pageWidth * -position);        //右边到中间就是*0，相当于不改变位置了，如果最右，就是1，相当于完全上去了
                // 0.75-1比例之间缩放
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - position);  //最小比例是0.75，只留0.25的空间让缩放，等于0的时候，刚好加0.25就是不错放，最右就是1-1，就只剩0.75了
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            } else { // (1,+Infinity]
                // 页面远离右侧页面
                page.setAlpha(0);                           //反正看不见，其实1也没用
            }

        }
    }

    @OnClick(R.id.text1)
    public void onClick() {
        startActivity(new Intent(this, BannerActivity.class));
    }
}
