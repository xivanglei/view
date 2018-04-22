package xl.gcs.com.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;


/*
获取触摸点到屏幕绝对原点的位置距离，event.getRawX() event.getRawY()
获得触摸点到View的相对原点的距离，event.getX(),event.getY()
获得View到父控件ViewGroup的距离，View内部直接getTop()getLeft()，如果外部就是mView.getRight()
getTop()是获取View的顶边到父控件的顶边，getBottom，View的底边到父控件的顶边，getLeft()左边到左边，getRight()子右到父左



 */
public class CustomView extends View {
    private int lastX;
    private int lastY;
    //实现有过渡效果的滑动
    private Scroller mScroller;

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public CustomView(Context context) {
        super(context);
    }

    public int getWidthTest() {
        return 100;
    }

    public void setWidthTest(int width) {
        //动画会不断传入值，然后把宽度变为传入的宽度
        getLayoutParams().width = width;
        //请求重新布局
        requestLayout();
    }


    public boolean onTouchEvent(MotionEvent event) {
        //获取到手指处到自身View原点的XY距离，每次点或移动或抬起，都会从这里开始运行
        int x = (int) event.getX();
        int y = (int) event.getY();

        //获取动作
        switch (event.getAction()) {
            //如果是点下去的动作
            case MotionEvent.ACTION_DOWN:
                //记录XY
                lastX = x;
                lastY = y;

                break;

                //如果是移动的动作
            case MotionEvent.ACTION_MOVE:

                //计算移动的距离，往左和往上移动是负数(比如x100，记录下来，往左边移到90,就是现在的90减刚刚的记录100是-10)
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                //调用layout方法来重新放置它的位置,如果父控件不是全屏，能把View全部移出去，只要不抬起就还能移回来，一抬起，再点View就点不中了
                //会完全移出父控件，移到外面即使点外面也点不中了，而且父控件的上层父控件不会显示这个View
                //按照指定左右上下重新画一次,左右同时偏移，如果偏移单边，右边不动，就会出现以不懂得边为固定，变长或变短
//                layout(getLeft()+offsetX, getTop()+offsetY,
//                       getRight()+offsetX , getBottom()+offsetY);

                //这种方法跟上面的方法类似，只是偏移一个位置
                //对left和right进行偏移,只能是LeftAndRight，没有offsetLeft方法，因为宽度是固定的，不会拉长
//                offsetLeftAndRight(offsetX);
                //对top和bottom进行偏移
//                offsetTopAndBottom(offsetY);


                //使用LayoutParams,父布局控件是FrameLayout就只能是FrameLayout.LayoutParams，否则会崩溃，书上用的是LinearLayout,就用LinearLayout.LayoutParams
                //先获取父布局参数
//                FrameLayout.LayoutParams layoutParams= (FrameLayout.LayoutParams) getLayoutParams();
//                //改变marginLeft值，同样marginLeft允许负数，所以也能移除布局
//                layoutParams.leftMargin = getLeft() + offsetX;
//                layoutParams.topMargin = getTop() + offsetY;
//                setLayoutParams(layoutParams);

                //我们还可以用下面这方法实现，效果都差不多
                //使用MarginLayoutParams
//                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//                layoutParams.leftMargin = getLeft() + offsetX;
//                layoutParams.topMargin = getTop() + offsetY;
//                setLayoutParams(layoutParams);

                //使用scrollBy,移动的是子View,所以要先调出getParent()父控件再scrollBy移动，而且，父控件虽然不动，但要假想成是父控件在动
                //所以里面的参数要用负值-offsetX
                ((View)getParent()).scrollBy(-offsetX,-offsetY);
                break;
        }

        return true;
    }

    //同样的，虽然父控件不动，但要假想成是父控件在动，所以要传入负值
    public void smoothScrollTo(int destX,int destY){
        //如果在滑动中，就记录下现在的位置
        int scrollX=getScrollX();
        int scrollY = getScrollY();
        //记录下还需要滑动多少距离
        int delta=destX-scrollX;
        int deltaY = destY - scrollY;
        //4000秒内滑向destX,destY，调用Scroller.startScroll方法只是存入值，关键还是下面的invalidate()来实现动画
        mScroller.startScroll(scrollX,scrollY,delta,destY,4000);
        //进行重绘，会调用draw()方法，draw方法里面会执行下面的computeScroll()方法，所以要移动就要复写这方法
        invalidate();
    }

    //系统在绘制View 的时候在draw()方法中调用该方法
    @Override
    public void computeScroll() {
        super.computeScroll();
        //如果mScroller滚动还未完成,就会根据分段滚动时间算出滚动的举例，再加给参数，如果算出距离已经没有了，说明完成了，会在下次调用返回false
        if(mScroller.computeScrollOffset()){
            //返回true就调用方法，让View按照这时段的x,y位置移动
            ((View) getParent()).scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            //再重画，画完又会调用computeScroll()，通过不断的重绘不断的调用computeScroll方法，直到mScroller.computeScrollOffset()为false
            invalidate();
        }
    }


}