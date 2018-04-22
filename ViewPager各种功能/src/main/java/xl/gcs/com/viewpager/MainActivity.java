package xl.gcs.com.viewpager;

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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
ViewPager里面的最长滚动时间， MAX_SETTLE_DURATION，可以随便改
里面的PagerAdapter需要复制过去，主要是里面用的是系统的ViewPager
ViewPager需要复制过去，里面改了系统的几个参数
PagerAdapter如果要无限，就要有不同的设置，而且Fragment估计不行，只是轮播的话正常设置就好
 */
public class MainActivity extends AppCompatActivity {

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
    private ImageHandler handler = new ImageHandler(new WeakReference<MainActivity>(this));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();


            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);  //前面标志是布局全屏包括状态栏，后面标志是，状态栏不取消
            getWindow().setStatusBarColor(Color.TRANSPARENT);


        }
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        bindID();
        getData();
        initAdapter();
        mLinearLayout.getChildAt(0).setEnabled(true);

    }

    private void bindID() {
        ButterKnife.bind(this);
    }

    private void initAdapter() {
        mAdapter = new PageAdapter(mImageList);
        mViewPager.setAdapter(mAdapter);
        //加动画，自定义的
//        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        //设置滚动事件，最多两秒
        handler.setDuration(2000);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //配合Adapter的currentItem字段进行设置。
            @Override
            public void onPageSelected(int arg0) {
                handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
                int page = arg0 % mImageList.size();
                //原来的显示项设为不可点击或不可触摸
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
                        handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                        break;
                    //监听没有拖拽的动作
                    case ViewPager.SCROLL_STATE_IDLE:
                        //发送消息可以继续轮播了，应该就是这里跟发送消息重复了，所以下面需要移除先
                        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);//默认在中间，使用户看不到边界，因为第一页有边界，就不能左移了
        //开始轮播效果
        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
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
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(21, 2);
            //如果不是第一个view
            if (pic != mPicPages[0]) {
                if (pic == mPicPages[mPicPages.length - 1]) {
                    //如果是最后一个，设置好最后的间距
//                    layoutParams.rightMargin = 150;
                }
                //左边间距除了第一个都要设置，没间距就贴紧了，很难看
                layoutParams.leftMargin = 10;
            }
            mLinearLayout.addView(view, layoutParams);
        }
    }

    //这个类主要用来发送消息让viewPager换页面的
    private static class ImageHandler extends Handler {

        private boolean check = false;
        //播放时间，5328通过ViewPager源码计算后会是 500ms 就是半秒
        private int duration = 5328;

        /**
         * 请求更新显示的View。
         */
        protected static final int MSG_UPDATE_IMAGE  = 1;
        /**
         * 请求暂停轮播。
         */
        protected static final int MSG_KEEP_SILENT   = 2;
        /**
         * 请求恢复轮播。
         */
        protected static final int MSG_BREAK_SILENT  = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        protected static final int MSG_PAGE_CHANGED  = 4;

        //轮播间隔时间
        protected static final long MSG_DELAY = 3000;

        //弱引用对象的存在不会阻止它所指向的对象变被垃圾回收器回收。
        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
        //强引用（Strong Reference）：通常我们通过new来创建一个新对象时返回的引用就是一个强引用，若一个对象通过一系列强引用可到达，它就是强可达的(strongly reachable)，那么它就不被回收
        //软引用（Soft Reference）：软引用和弱引用的区别在于，若一个对象是弱引用可达，无论当前内存是否充足它都会被回收，而软引用可达的对象在内存不充足时才会被回收，因此软引用要比弱引用“强”一些
        //虚引用（Phantom Reference）：虚引用是Java中最弱的引用，那么它弱到什么程度呢？它是如此脆弱以至于我们通过虚引用甚至无法获取到被引用的对象，虚引用存在的唯一作用就是当它指向的对象被回收后，虚引用本身会被加入到引用队列中，用作记录它指向的对象已被销毁。
        private WeakReference<MainActivity> weakReference;
        private int currentItem = 0;

        protected ImageHandler(WeakReference<MainActivity> wk){
            weakReference = wk;
        }

        //设置滚动时间单位是毫秒
        protected void setDuration(int duration) {
            this.duration = this.duration / (duration / 500);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //这样获取的activity就是弱引用的对象，变为null就会被回收
            MainActivity activity = weakReference.get();
            if (activity==null){
                //Activity已经回收，无需再处理UI了
                return ;
            }
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。但第一次就不判断，因为肯定是有的，并只有一个，被移除了就会刚开始不动
            //检查下如果有message的what==MSG_UPDATE_IMAGE，如果不是第一次，就移除掉这次message
            //因为没有拖拽动作就会发送一次消息，3秒后又发消息，等于发重复了
            if (activity.handler.hasMessages(MSG_UPDATE_IMAGE) && check){
                activity.handler.removeMessages(MSG_UPDATE_IMAGE);
            } else {
                //第一次就不移除了，因为是主动要发的
                check = true;
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    activity.mViewPager.mPopulatePending = false;
                    activity.mViewPager.setCurrentItemInternal(currentItem, !activity.mViewPager.mFirstLayout, false, duration);
                    //准备下次播放，sendEmptyMessageDelayed表示延迟发送空的message,设置what参数为MSG_UPDATE_IMAGE
                    activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    currentItem = msg.arg1;
                    break;
                default:
                    break;
            }
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
                //不用无限轮播功能没关系，如果用了无限轮播，就要把这句注释掉，否则左边显示有问题
//                page.setTranslationY(pageWidth * -position);        //右边到中间就是*0，相当于不改变位置了，如果最右，就是1，相当于完全上去了
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
}
