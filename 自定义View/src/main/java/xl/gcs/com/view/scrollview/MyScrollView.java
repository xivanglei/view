package xl.gcs.com.view.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by xianglei on 2018/5/22.
 */

public class MyScrollView extends ScrollView {

    private GestureDetector mGestureDetector;
    private OnMyScrollViewChangeListener listener;

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化手势
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
    }

    /**
     * touch事件的拦截函数
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //根据手势决定是否拦截子控件的onTouch事件
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    /**
     * 控件的手势监听
     */
    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        //// 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
        @Override
        //e1是前点击事件，e2是现在点击事件，
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //当纵向滑动的距离大于横向滑动的距离的时候，返回true
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                return true;
            }
            return false;
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener!=null) {
            listener.onScrollChanged(t);
        }
    }

    public void setOnMyScrollViewChangeListener(OnMyScrollViewChangeListener listener) {
        this.listener = listener;
    }
    public interface OnMyScrollViewChangeListener{
        void onScrollChanged(int t);
    }
}