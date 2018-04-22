package xl.gcs.com.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class RectView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mColor=Color.RED;
    public RectView(Context context) {
        super(context);
        initDraw();
    }

    public RectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取attrs里的RectView
        TypedArray mTypedArray=context.obtainStyledAttributes(attrs,R.styleable.RectView);
        //提取RectView属性集合的rect_color属性，如果没设置默认值为Color.RED
        mColor=mTypedArray.getColor(R.styleable.RectView_rect_color,Color.RED);
        //获取资源后要及时回收
        mTypedArray.recycle();
        initDraw();
    }

    public RectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDraw();
    }

    private void initDraw() {
        //画笔颜色
        mPaint.setColor(mColor);
        //画笔宽度
        mPaint.setStrokeWidth((float) 1.5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取参数模式
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取尺寸
        int widthSpecSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize=MeasureSpec.getSize(heightMeasureSpec);
        //如果模式是AT_MOST最大模式（对应wrap_content），需要有默认值，不然就会设为最大，另一种是EXACTLY精确模式，对应着match_parent和准确宽高
        //如果有确定值就不用设置默认值了，下面onDraw会画出来的
        if(widthSpecMode==MeasureSpec.AT_MOST&&heightSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(600,600);
        }else if(widthSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(600,heightSpecSize);
        }else if(heightSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,600);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //这里对paddingLeft尺寸进行处理，先获取
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        //计算宽度，=减去padding 左右，下面是减上下
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        //画矩形
        canvas.drawRect(0 + paddingLeft, 0 + paddingTop, width + paddingLeft, height + paddingTop, mPaint);
    }
}
