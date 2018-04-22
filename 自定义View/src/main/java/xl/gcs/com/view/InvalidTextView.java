package xl.gcs.com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by xianglei on 2018/4/22.
 * 大概系统推荐继承android.support.v7.widget.AppCompatTextView，书上继承TextView能运行，但下面有红线
 * 其他全部跟正常一样用，就是TextView中间会有红线
 */

public class InvalidTextView extends android.support.v7.widget.AppCompatTextView {

    //画笔 使位图抗锯齿的标志
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public InvalidTextView(Context context) {
        super(context);
        initDraw();
    }

    public InvalidTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDraw();
    }

    public InvalidTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDraw();
    }

    private void initDraw() {
        //设置颜色为红色
        mPaint.setColor(Color.RED);
        //设置画笔的宽度为1.5
        mPaint.setStrokeWidth((float) 1.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        //画直线
        canvas.drawLine(0, height / 2, width, height / 2, mPaint);
    }
}
