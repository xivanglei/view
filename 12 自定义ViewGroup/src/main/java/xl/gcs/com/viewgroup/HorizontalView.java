package xl.gcs.com.viewgroup;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import static android.content.ContentValues.TAG;

public class HorizontalView extends ViewGroup {
    private int lastX;
    private int lastY;
    private int currentIndex = 0; //当前子元素
    private int childWidth = 0;
    private Scroller scroller;
    private VelocityTracker tracker;    //增加速度检测,如果速度比较快的话,就算没有滑动超过一半的屏幕也可以
    private int lastInterceptX=0;
    private int lastInterceptY=0;
    public HorizontalView(Context context) {
        super(context);
        init();
    }
    public HorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public HorizontalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        scroller = new Scroller(getContext());
        tracker = VelocityTracker.obtain();
    }

    //这里处理滑动冲突，复写事件的拦截方法
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //先声明变量是否准备拦截false不拦截
        boolean intercept = false;
        //记录点击的位置
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //点下去先设为不拦截
                intercept = false;
                //如果scroller没有执行完毕,则打断，针对滑到一半又快速点他，不让他滑过去的
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //记录下往x移动了多少，y移动了多少距离，lastIntercept刚刚点下去，下面就会储存
                int deltaX = x - lastInterceptX;
                int deltaY = y - lastInterceptY;
                //如果水平方向距离比垂直方向长  MOVE中返回true一次,后续的MOVE和UP都不会收到此请求
                if (Math.abs(deltaX) - Math.abs(deltaY) > 0) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        lastX = x;
        lastY = y;
        lastInterceptX = x;
        lastInterceptY = y;
        return intercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //增加速度检测,如果速度比较快的话,就算没有滑动超过一半的屏幕也可以
        tracker.addMovement(event);
        //先记录点下去的位置
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //同样，滑动的动画没执行完毕，就打断，针对滑到一半又快速点他，不让他滑过去的
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //跟随手指滑动
                int deltaX = x - lastX;
                scrollBy(-deltaX, 0);
                break;
            //释放手指以后开始自动滑动到目标位置
            case MotionEvent.ACTION_UP:
                //相对于当前View滑动的距离,正为向左,负为向右，先乘除后加减，currentIndex默认是0,getScrollX()在第一个页面也是正常数值，就不减
                //第二个页面，getScrollX就会加上第一面的宽度，所以要减回去
                int distance = getScrollX() - currentIndex * childWidth;
                Log.d(TAG, "onTouchEvent: " + getScrollX());
                Log.d(TAG, "onTouchEvent: " + distance);
                Log.d(TAG, "onTouchEvent: " + currentIndex);

                //必须滑动的距离要大于1/2个宽度,否则不会切换到其他页面
                if (Math.abs(distance) > childWidth / 2) {
                    if (distance > 0) {
                        currentIndex++;
                    } else {
                        currentIndex--;
                    }
                } else {
                    //如果速度较快的话，不需要滑动一半也能记录接下来准备滑的位置，先获取水平方向的速度
                    tracker.computeCurrentVelocity(1000);
                    float xV = tracker.getXVelocity();
                    if (Math.abs(xV) > 50) {
                        if (xV > 0) {
                            currentIndex--;
                        } else {
                            currentIndex++;
                        }
                    }
                }
                //如果小于0，就设为0，如果大于子元素，就减一，让他变为最后页
                currentIndex = currentIndex < 0 ? 0 : currentIndex > getChildCount() - 1 ? getChildCount() - 1 : currentIndex;
                //y不滑动，x方向划过去
                smoothScrollTo(currentIndex * childWidth, 0);
                tracker.clear();
                break;
            default:
                break;
        }
        lastX = x;
        lastY = y;
        return true;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //测量所有子元素
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //处理wrap_content的情况，如果没有子元素，就把宽高设为0
        if (getChildCount() == 0) {
            setMeasuredDimension(0, 0);
            //如果宽高都是AT_MOST 则宽度设置为所有子元素宽度的和，高度设置为第一个子元素的高度
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            View childOne = getChildAt(0);
            int childWidth = childOne.getMeasuredWidth();
            int childHeight = childOne.getMeasuredHeight();
            setMeasuredDimension(childWidth * getChildCount(), childHeight);
            //如果宽度是AT_MOST,则宽度为所有子元素的宽度和
        } else if (widthMode == MeasureSpec.AT_MOST) {
            View childOne = getChildAt(0);
            int childWidth = childOne.getMeasuredWidth();
            setMeasuredDimension(childWidth * getChildCount(), heightSize);
            //如果高度是AT_MOST，则高度为第一个子元素的高度
        } else if (heightMode == MeasureSpec.AT_MOST) {
            int childHeight = getChildAt(0).getMeasuredHeight();
            setMeasuredDimension(widthSize, childHeight);
        }
    }
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
//            invalidate()得在UI线程中被调动，在工作者线程中可以通过Handler来通知UI线程进行界面更新。如果更新ui需要 handler，这里试过没关系
//            而postInvalidate()在工作者线程中被调用,所以都不需要handler
            postInvalidate();
        }
    }
    public void smoothScrollTo(int destX, int destY) {
        scroller.startScroll(getScrollX(), getScrollY(), destX - getScrollX(), destY - getScrollY(), 1000);
        invalidate();
    }

    //这是必须复写的方法
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = 0; //左边的距离
        View child;
        //遍历布局子元素
        for (int i = 0; i < childCount; i++) {
            //遍历子元素
            child = getChildAt(i);
            //如果子元素不是GONE
            if (child.getVisibility() != View.GONE) {
                //赋值给宽度变量
                int width = child.getMeasuredWidth();
                childWidth = width; //赋值给子元素宽度变量
                //子元素布局
                child.layout(left, 0, left + width, child.getMeasuredHeight());
                left += width;
            }
        }
    }
}
