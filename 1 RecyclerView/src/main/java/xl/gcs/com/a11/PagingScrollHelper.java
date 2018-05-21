package xl.gcs.com.a11;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Created by xianglei on 2018/5/17.
 */

public class PagingScrollHelper {

    private RecyclerView mRecyclerView = null;
    private int currentIndex = 0;
    private int distance;
    private boolean pageChanged;
    private onPageChangeListener mOnPageChangeListener;
    private MyOnScrollListener mOnScrollListener = new MyOnScrollListener();
    private MyOnFlingListener mOnFlingListener = new MyOnFlingListener();
    private MyOnTouchListener mOnTouchListener = new MyOnTouchListener();
    private int offsetY = 0;
    private int offsetX = 0;
    private int oldOffsetX = 0;
    private int oldOffsetY = 0;
    ValueAnimator mAnimator = null;


    public PagingScrollHelper() {
        mAnimator = new ValueAnimator();
        //动画时长
        mAnimator.setDuration(300);
        //监听属性变化，就是开始动画了并发生动作了
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //得到当前值
                int nowPoint = (int) animation.getAnimatedValue();

                if (mOrientation == ORIENTATION.VERTICAL) {
                    int dy = nowPoint - offsetY;
                    //这里通过RecyclerView的scrollBy方法实现滚动。注意看里面的参数是x,y,移动到xy点，并不是从参1移到参2
                    mRecyclerView.scrollBy(0, dy);
                } else {
                    int dx = nowPoint - offsetX;
                    mRecyclerView.scrollBy(dx, 0);
                }
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            //这里是动画结束了
            @Override
            public void onAnimationEnd(Animator animation) {
                //回调监听
                if (null != mOnPageChangeListener) {
                    //传入页面
                    mOnPageChangeListener.onPageChange(currentIndex);
                }
                //修复双击item bug
                mRecyclerView.stopScroll();
                //储存距离，不然点击的时候没有触发滑动也就记录不了距离了，就一直是原来的距离，就会点下就超点下就超
                oldOffsetX = offsetX;
                oldOffsetY = offsetY;
            }
        });
    }


    enum ORIENTATION {
        HORIZONTAL, VERTICAL, NULL
    }

    private ORIENTATION mOrientation = ORIENTATION.HORIZONTAL;

    public void setUpRecycleView(RecyclerView recycleView) {
        if (recycleView == null) {
            throw new IllegalArgumentException("recycleView must be not null");
        }
        mRecyclerView = recycleView;
        //处理滑动
        recycleView.setOnFlingListener(mOnFlingListener);
        //设置滚动监听，记录滚动的状态，和总的偏移量
        recycleView.setOnScrollListener(mOnScrollListener);
        //记录滚动开始的位置
        recycleView.setOnTouchListener(mOnTouchListener);
        //获取滚动的方向
        updateLayoutManger();

    }

    public void updateLayoutManger() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager.canScrollVertically()) {
                mOrientation = ORIENTATION.VERTICAL;
            } else if (layoutManager.canScrollHorizontally()) {
                mOrientation = ORIENTATION.HORIZONTAL;
            } else {
                mOrientation = ORIENTATION.NULL;
            }
            if (mAnimator != null) {
                mAnimator.cancel();
            }
        }
    }

    /**
     * 获取总共的页数
     */
    public int getPageCount() {
        if (mRecyclerView != null) {
            if (mOrientation == ORIENTATION.NULL) {
                return 0;
            }
            //computeVerticalScrollExtent()是当前的显示区域 computeVerticalScrollRange()是整个View控件的高度
            if (mOrientation == ORIENTATION.VERTICAL && mRecyclerView.computeVerticalScrollExtent() != 0) {
                return mRecyclerView.computeVerticalScrollRange() / mRecyclerView.computeVerticalScrollExtent();
            } else if (mRecyclerView.computeHorizontalScrollExtent() != 0) {
                Log.i("zzz","rang="+mRecyclerView.computeHorizontalScrollRange()+" extent="+mRecyclerView.computeHorizontalScrollExtent());
                return mRecyclerView.computeHorizontalScrollRange() / mRecyclerView.computeHorizontalScrollExtent();
            }
        }
        return 0;
    }

    public void scrollToPosition(int position) {
        if (mAnimator != null) {
            currentIndex = position;
            //如果小于0，就设为0，如果大于子元素，就减一，让他变为最后页
            currentIndex = currentIndex < 0 ? 0 : currentIndex > getPageCount() - 1 ? getPageCount() - 1 : currentIndex;
            int startPoint = mOrientation == ORIENTATION.VERTICAL ? offsetY : offsetX;
            int endPoint = 0;
            if (mOrientation == ORIENTATION.VERTICAL) {
                endPoint = mRecyclerView.getHeight() * currentIndex;
            } else {
                endPoint = mRecyclerView.getWidth() * currentIndex;
            }
            if (startPoint != endPoint) {
                //使用动画处理滚动，如果偏移量是50，最后判定是0页也就是0，就会让页面从50的位置滑到0，这里的页面是画布，想象成让外层的布从50到0，那recyclerView就是从0到50了，就是右移，相当于点屏幕左边滑到右边的手势
                mAnimator.setIntValues(startPoint, endPoint);
                mAnimator.start();
            }
        }
    }

    //onFling是监控页面滑动后的状态，通常onFling的调用发生在滑动事件MotionEvent.ACTION_UP时也就是抬手时，这里如果换页，换页完也会在onScrollStateChanged中调用onFling
    public class MyOnFlingListener extends RecyclerView.OnFlingListener {
        //每秒x轴方向移动的像素,和每秒y轴方向移动的像素,应该是很快才会触发记录值，比较慢的话就是0,如果比较慢都不会执行onFling，这里主要判断速度
        @Override
        public boolean onFling(int velocityX, int velocityY) {
            Log.d(TAG, "onFling: ");
            if(!pageChanged && Math.abs(distance) > 150) {
                if (mOrientation == ORIENTATION.NULL) {
                    return false;
                }
                //如果是垂直方向
                if (mOrientation == ORIENTATION.VERTICAL) {
//                如果有速度，右滑到左是正，就要+1页
                    if (velocityY < 0) {
                        currentIndex--;
                    } else if (velocityY > 0) {
                        currentIndex++;
                    }
                } else {
                    if (velocityX < 0) {
                        currentIndex--;
                    } else if (velocityX > 0) {
                        currentIndex++;
                    }
                }
                //页面确定了就让属性动画滑过去
                scrollToPosition(currentIndex);
            }
            return false;
        }

    }


    //记录滚动的状态和总的偏移量
    public class MyOnScrollListener extends RecyclerView.OnScrollListener {
        //在滚动开始和停止滚动时才会回调，一次滚动就回调两次
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //开始滚动newState = 1,结束滚动=0，开始滚动的时候先记录下现在的滚动位置是多少，滚动结束之前给onTouch方法的距离做比对
            if (newState != 0) {
                oldOffsetX = offsetX;
                oldOffsetY = offsetY;
            }
        }

        //在滚动的时候连续不断在回调，dx,dy是滚动距离
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //滚动结束记录滚动的偏移量,这里不是加绝对值，所以offset值不管怎么滑都是偏移量，把右边页面滑到左是正，
            // y方向下页滑到上面是正，第二页滑动也会直接计算出一共滑了多少，相当于还没滑已经有一个页面的宽度滑动距离了
            offsetY += dy;
            offsetX += dx;
        }
    }

    public class MyOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(mOrientation == ORIENTATION.VERTICAL) {
                    distance = offsetY - oldOffsetY;
                    //必须滑动的距离要大于1/2个宽度,否则不会切换到其他页面
                    if (Math.abs(distance) > mRecyclerView.getHeight() / 2) {
                        if (distance > 0) {
                            currentIndex++;
                        } else {
                            currentIndex--;
                        }
                        //通知后面的onFling回调，已经在这里改变页面了，让他不用动了
                        pageChanged = true;
                    } else {
                        //通知后面的onFling回调，页面没改，让他判断改不改
                        pageChanged = false;
                    }
                    scrollToPosition(currentIndex);
                } else {
                    distance = offsetX - oldOffsetX;
                    //必须滑动的距离要大于1/2个宽度,否则不会切换到其他页面
                    if (Math.abs(distance) > mRecyclerView.getWidth() / 2) {
                        if (distance > 0) {
                            currentIndex++;
                        } else {
                            currentIndex--;
                        }
                        pageChanged = true;
                    } else {
                        pageChanged = false;
                    }
                    //页面确定了就让属性动画滑过去
                    scrollToPosition(currentIndex);
                }
            }
            return false;
        }
    }
    public void setOnPageChangeListener(onPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    public interface onPageChangeListener {
        void onPageChange(int index);
    }

}
