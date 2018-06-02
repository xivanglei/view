package xl.gcs.com.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class MyViewPager extends ViewGroup {
    private final int VERTICAL = 0x11;
    private final int HORIZONTAL = VERTICAL + 1;
    private int orientation;
    private int downX;
    private int downY;
    private int lastX;
    private int lastY;
    private int childCount;     //子元素数量
    private int currentIndex = 0; //当前子元素
    private int childWidth = 0;
    private int childHeight = 0;
    private Scroller scroller;
    private VelocityTracker tracker;    //增加速度检测,如果速度比较快的话,就算没有滑动超过一半的屏幕也可以
    private int lastInterceptX=0;
    private int lastInterceptY=0;
    public MyViewPager(Context context) {
        super(context);
        init();
    }
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        scroller = new Scroller(getContext());
        tracker = VelocityTracker.obtain();
        //默认显示页，这里主要是用来加一个空的View，让这个View滑入滑出的效果，第二个右边是有用的View所以先显示右边页，往右滑让他滑出视图，往左滑再滑进去
        currentIndex = 0;
        //滑动方向
        orientation = HORIZONTAL;

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
                //记录点下去的位置，给onTouchEvent中快速滑动时做判断
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //记录下往x移动了多少，y移动了多少距离，lastIntercept刚刚点下去，下面就会储存
                int deltaX = x - lastInterceptX;
                int deltaY = y - lastInterceptY;
                if(orientation == VERTICAL) {
                    //如果垂直方向距离比水平方向长  MOVE中返回true一次,后续的MOVE和UP都不会收到此请求,右边加!的括号里意思是，只要不是上边上滑，或下边下滑，都拦，拦下来让父层滑，是的话，父层反正划不动，就让子层滑
                    if (Math.abs(deltaY) - Math.abs(deltaX) > 0 && !((getScrollY() <= 0 && deltaY > 0) || (getScrollY() >= (childCount - 1) * childHeight && deltaY < 0))) {
                        intercept = true;
                    } else {
                        intercept = false;
                    }
                } else {
                    //如果水平方向距离比垂直方向长  MOVE中返回true一次,后续的MOVE和UP都不会收到此请求，右边加!的括号里意思是，只要不是左边左滑，或右边右滑，都拦，拦下来让父层滑，是的话，父层反正划不动，就让子层滑
                    if (Math.abs(deltaX) - Math.abs(deltaY) > 0 && !((getScrollX() <= 0 && deltaX > 0) || (getScrollX() >= (childCount - 1) * childWidth && deltaX < 0))) {
                        intercept = true;
                    } else {
                        intercept = false;
                    }
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
            //上面要判断滑动方向决定是否拦截，所以ACTION_DOWN一定不会拦截，所以这里的ACTION_DOWN永远不会运行到，只会交给子View
//            case MotionEvent.ACTION_DOWN:
//                //滑动的动画没执行完毕，就打断，针对滑到一半又快速点他，不让他滑过去的
//                if (!scroller.isFinished()) {
//                    scroller.abortAnimation();
//                }
//                break;
            case MotionEvent.ACTION_MOVE:
                if(orientation == VERTICAL) {
                    //跟随手指滑动
                    int deltaY = y - lastY;
                    //如果滑动会超界
                    if(getScrollY() - deltaY < 0 || getScrollY() - deltaY > (childCount - 1) * childHeight) {
                        //如果滑动超一半，就说明要滑到下边界（因为移动是慢慢分步移的，不可能一下移动超过一半距离），移动距离就等于下边界减一个面宽度减现在偏移量，再给负值，下面再会负回来的
                        deltaY = getScrollY() > getHeight() / 2 ? -((childCount - 1) * childHeight - getScrollY()) : 0 + getScrollY();
                    }
                        scrollBy(0, -deltaY);
                } else {
                    //跟随手指滑动
                    int deltaX = x - lastX;
                    //如果滑动会超界
                    if(getScrollX() - deltaX < 0 || getScrollX() - deltaX > (childCount - 1) * childWidth) {
                        //如果滑动超一半，就说明要滑到右边界（因为移动是慢慢分步移的，不可能一下移动超过一半距离），移动距离就等于右边界减一个面宽度减现在偏移量，再给负值，下面再会负回来的
                        deltaX = getScrollX() > getWidth() / 2 ? -((childCount - 1) * childWidth - getScrollX()) : 0 + getScrollX();
                    }
                    //在现有偏移量的基础上再加上x,y的距离，-deltaX就是时刻获取的移动的距离，这里不能用scrollTo，用scrollTo就是移动到这个点了
                    scrollBy(-deltaX, 0);
                }
                break;
            //释放手指以后开始自动滑动到目标位置
            case MotionEvent.ACTION_UP:
                if(orientation == VERTICAL) {
                    //相对于当前View滑动的距离,正为向上,负为向下，先乘除后加减，currentIndex默认是0,getScrollY()在第一个页面也是正常数值，就不减
                    //第二个页面，getScrollY就会加上第一面的宽度，所以要减回去
                    int distance = getScrollY() - currentIndex * childHeight;
                    //必须滑动的距离要大于1/3个高度,否则不会切换到其他页面
                    if (Math.abs(distance) > childHeight / 2) {
                        if (distance > 0) {
                            currentIndex++;
                        } else {
                            currentIndex--;
                        }
                    } else {
                        //如果速度较快的话，不需要滑动一半也能记录接下来准备滑的位置，先获取垂直方向的速度
                        tracker.computeCurrentVelocity(1000);
                        float yV = tracker.getYVelocity();
                        if (Math.abs(yV) > 50) {
                            //如果大于0还要看下手指移动方向是不是往上划，上划才会减一页，有时候到尾页，下滑的最后一下不经意停留一下，就会让yV的方向误判，误让页面减一页，所以这里增加一个判断
                            if (yV > 0 && downY - y < 0) {
                                currentIndex--;
                            } else if (yV < 0 && downY - y > 0) {
                                currentIndex++;
                            }
                        }
                    }
                } else {
                    //相对于当前View滑动的距离,正为向左,负为向右，先乘除后加减，currentIndex默认是0,getScrollX()在第一个页面也是正常数值，就不减
                    //第二个页面，getScrollX就会加上第一面的宽度，所以要减回去
                    int distance = getScrollX() - currentIndex * childWidth;
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
                            //如果大于0还要看下手指移动方向是不是往左划，左划才会减一页，有时候到尾页，右滑的最后一下不经意停留一下，就会让xV的方向误判，误让页面减一页，所以这里增加一个判断
                            if (xV > 0 && downX - x < 0) {
                                currentIndex--;
                            } else if (xV < 0 && downX - x > 0) {
                                currentIndex++;
                            }
                        }
                    }
                }
                //滑到currentIndex页，小于0页，大于length页，下面有判断
                scrollToPosition(currentIndex);
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
            //scroller位置参数时刻发生变化，时刻移动到x,y的位置
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
        childCount = getChildCount();
        View child;
        if(orientation == VERTICAL) {
            int top = 0; //上边的距离
            int height = 0;     //高度变量
            //遍历布局子元素
            for (int i = 0; i < childCount; i++) {
                //遍历子元素
                child = getChildAt(i);
                //如果子元素不是GONE
                if (child.getVisibility() != View.GONE) {
                    //赋值给高度变量
                    height = child.getMeasuredHeight();
                    childHeight = height; //赋值给子元素高度变量
                    //子元素布局
                    child.layout(0, top, child.getMeasuredWidth(), top + height);
                    top += height;
                }
            }
            //移动到x,y坐标,这里移动到整个高度，相当于把第一页翻过去，这里不能用scrollBy，因为onLayout会运行两次，第一次跑过去，第二次还要再一次移动到那个位置，就翻倍移动了
            scrollTo(0,currentIndex * childHeight);
        } else {
            int left = 0; //左边的距离
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
            //移动到x,y坐标,这里移动到整个宽度，相当于把第一页翻过去，这里不能用scrollBy，因为onLayout会运行两次，第一次跑过去，第二次还要再一次移动到那个位置，就翻倍移动了
            scrollTo(currentIndex * childWidth, 0);
        }
    }

    public void scrollToPosition(int position) {
        currentIndex = position;
        //如果小于0，就设为0，如果大于子元素，就减一，让他变为最后页
        currentIndex = currentIndex < 0 ? 0 : currentIndex > getChildCount() - 1 ? getChildCount() - 1 : currentIndex;
        //动画滑到至x,y的坐标
        if(orientation == VERTICAL) {
            smoothScrollTo(0,currentIndex * childHeight);
        } else {
            smoothScrollTo(currentIndex * childWidth, 0);
        }
    }

    public int getCurrentIndex() {
        if(getChildCount() == 0) {
            return -1;
        } else {
            return currentIndex;
        }
    }
}
