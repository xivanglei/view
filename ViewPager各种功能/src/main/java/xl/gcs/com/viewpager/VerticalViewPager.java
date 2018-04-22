package xl.gcs.com.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xianglei on 2018/3/31.
 */

public class VerticalViewPager extends ViewPager {

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(true, new DefaultTransformer());
    }

    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();                           //得到view的宽度
        float height = getHeight();                         //得到高度

        float swappedX = (event.getY() / height) * width;       //得到点击y方向的位置/y 就是得到了比例，按照这个比例*宽度，相当于这个y如果再x位置的话，是什么位置
        float swappedY = (event.getX() / width) * height;       //这里就是x的位置，转成在y的位置

        /*
        * event.setLocation 主要作用是对当前的event对象中的x和y的值进行重新设定，
        * 当这个event重新分发给其它的view时，就会按照设定后的值进行处理相关的逻辑。
        * 设定后，event这个对象通过event.getX()等方法获取到的值就是经过重新设定后的值了。和原来的event相关的值就不同了。
        */
        event.setLocation(swappedX, swappedY);
        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        swapTouchEvent(event);                                  //x,y位置变了，y往上移就相当于x上的往右移
        return super.onInterceptTouchEvent(swapTouchEvent(event));
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapTouchEvent(ev));
    }

    public class DefaultTransformer implements PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            float alpha = 0;
            if (0 <= position && position <= 1) {               //中间页划上，或上面页滑到中间，针对中间部分的显示
                alpha = 1 - position;                           //越到中间越浓，刚好中间就是 1 - 0
            } else if (-1 < position && position < 0) {         //往上滑的动作，针对上面的页
                alpha = position + 1;                           //越来越淡，最上就是-1 + 1 = 0,刚好滑上去了，透明度页刚好是0了
            }
            view.setAlpha(alpha);                               //设置透明度
            view.setTranslationX(view.getWidth() * -position);  //设置x方向，也就是横向的位移，设为-position,刚好跟viewpager的动作相抵消，横向就没有动画了
            float yPosition = position * view.getHeight();      //下面两条刚好是把y方向的动画加上了
            view.setTranslationY(yPosition);
        }
    }

}
