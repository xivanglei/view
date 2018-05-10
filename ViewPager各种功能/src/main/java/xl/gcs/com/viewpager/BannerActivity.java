package xl.gcs.com.viewpager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerActivity extends AppCompatActivity {

    @BindView(R.id.main_viewpager)
    Banner mViewPager;
    @BindView(R.id.main_linear)
    LinearLayout mMain_linear;
    List<String> list_path;
    List<String> list_title;
    private int mNum = 0; //轮播图的指示标临时位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        ButterKnife.bind(this);
        initBanner();
    }


    private void initBanner() {

        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        list_title.add("好好学习");
        list_title.add("天天向上");
        list_title.add("热爱劳动");
        list_title.add("不搞对象");
        View view;
        //避免刷新的时候一直加，所以每次清空了再加
        mMain_linear.removeAllViews();
        for (int i = 0; i < list_path.size(); i++) {
            view = new View(this);
            //设置单个指示器，背景颜色可以自己做
            view.setBackgroundResource(R.drawable.bg_selected_h2_w21);
            view.setSelected(false);
            //设置指示器宽高和Margin等的参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(42, 4);
            //如果不是第一个view
            if (i != 0) {
                layoutParams.leftMargin = 20;
            }
            mMain_linear.addView(view, layoutParams);
        }
        if(list_path.size() > 0) {
            mMain_linear.getChildAt(0).setSelected(true);
        }
        //设置图片加载器，图片加载器在下方
        mViewPager.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        mViewPager.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        mViewPager.setBannerAnimation(Transformer.Default);
        //设置轮播间隔时间
        mViewPager.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        mViewPager.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        mViewPager.setIndicatorGravity(-2);
        mViewPager.setOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //如果3张图的话，这里显示图是1、2、3、4，而我们应该是0、1、2，所以要稍微进行逻辑调整
                int page = position % list_path.size();
                if(page > 0) {
                    page -= 1;
                } else {
                    page = list_path.size() - 1;
                }
                //原来的显示项设为不可点击或不可触摸
                mMain_linear.getChildAt(mNum).setSelected(false);
                //现在选的这个激活
                mMain_linear.getChildAt(page).setSelected(true);
                mNum = page;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //必须最后调用的方法，启动轮播图。
        mViewPager.start();
        //通过反射把自带的指示器给隐藏掉
        Class<Banner> clazz = Banner.class;                                     //反射类
        try {
            Field indicator = clazz.getDeclaredField("indicator");                 //调取mAudioManager属性
            indicator.setAccessible(true);                                                  //取消封装
            LinearLayout linearLayout = (LinearLayout) indicator.get(mViewPager);
            linearLayout.setVisibility(View.GONE);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(context).load((String) path).into(imageView);
        }
    }

}
