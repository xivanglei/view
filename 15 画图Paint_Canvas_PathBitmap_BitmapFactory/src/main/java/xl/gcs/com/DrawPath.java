package xl.gcs.com;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xianglei on 2018/4/25.
 */

public class DrawPath extends View {
    private int view_width = 0;
    private int view_height = 0;
    private float preX;
    private float preY;
    private Path path;
    public Paint paint = null;
    private Bitmap cacheBitmap = null;
    Canvas cacheCanvas = null;
    public DrawPath(Context context, AttributeSet set) {
        super(context, set);
        //获取宽度像素
        view_width = context.getResources().getDisplayMetrics().widthPixels;
        //获取高度
        view_height = context.getResources().getDisplayMetrics().heightPixels;
        //创建位图，ALPHA_8 代表8位Alpha位图 ARGB_4444 代表16位ARGB位图 ARGB_8888 代表32位ARGB位图 RGB_565 代表8位RGB位图
        cacheBitmap = Bitmap.createBitmap(view_width, view_height, Bitmap.Config.ARGB_8888);
        //创建画布
        cacheCanvas = new Canvas();
        //设置位图，之后这画布上所有画的内容都会画到这位图上，可以储存位图，也可以画到其他画布上
        cacheCanvas.setBitmap(cacheBitmap);
        //初始化路径
        path = new Path();
        //初始化画笔
        paint = new Paint();
        //设置颜色
        paint.setColor(Color.RED);
        //设为描边模式，否则会跟着路径大范围填充颜色
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        //设为抗锯齿模式
        paint.setAntiAlias(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //把位图上的内容画到这画布上，下面会设定每次抬手都会画到这位图上，再重画，就通过这里画上去，就可以重置路径了，不然路径会储存的有点太多
        canvas.drawBitmap(cacheBitmap, 0, 0, null);
        //这里画路径为了每次连续画的时候都显示在画布上先，抬手的时候清空路径，就通过上面画位图画出来，这里就空画了
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //把路径移动到down的位置，储存这个位置
                preX = x;
                preY = y;
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                //算出移动的距离
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                //如果距离大于5像素就画过去，不要1像素也画，没必要
                if(dx >= 5 || dy >= 5) {
                    //该方法的实现是当我们不仅仅是画一条线甚至是画弧线时会形成平滑的曲线，该曲线又称为"贝塞尔曲线"(Bezier curve)，其中，x1，y1为控制点的坐标值，x2，y2为终点的坐标值；
                    path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
                    //再次记录x,y
                    preX = x;
                    preY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                //一抬手就要花路径了，这画布刚刚设置了位图，就会画到位图上，下面重画就会把位图的内容画到这view的画布上
                cacheCanvas.drawPath(path, paint);
                //因为已经画到位图上了，可以重置了
                path.reset();
                break;
            default:
                break;
        }
        //重画
        invalidate();
        return true;
    }

    public void clear() {
        if(cacheCanvas != null) {
            path.reset();
            //设背景为透明色，其实就随便设设，因为后面参数就是把所有内容都不提交到画布上，包括路径和透明色背景，所以设成黑色都没事
            cacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            //重画
            invalidate();
        }
    }
}
