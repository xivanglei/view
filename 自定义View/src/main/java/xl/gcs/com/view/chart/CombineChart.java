package xl.gcs.com.view.chart;

/**
 * Created by xianglei on 2018/5/15.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import xl.gcs.com.view.R;

import static android.content.ContentValues.TAG;


/**
 * Created by xianglei on 2018/5/14.
 */

public class CombineChart extends View {

    private OnInsideTouchListener listener;     //点住柱子回调
    private boolean inside;     //判断有没有点到柱子里
    //控件的宽高
    private int screenWidth, screenHeight;
    //柱子比对的最大数值
    private float maxTelephone_charge;
    private int maxTelephone_count = 0;
    private float aveTelephone_charge = 0.0f;
    private float aveTelephone_count = 0.0f;
    //柱子最大高度
    private float maxHeight;
    //柱子的画笔，文字的画笔，线的画笔，点的画笔
    private Paint barPaint, textPaint, pointPaint;
    //柱子
    private RectF barRect;
    //底部偏移量
    private int botMargin, topMargin;
    //柱子的宽度
    private int barWidth;
    //柱子旁边的空白宽度
    private int barSpace;
    //起始点坐标
    private float xStartIndex=0;
    //1.数据集合
    List<OperatorChartBean> list;

    public CombineChart(Context context) {
        this(context, null);
    }

    public CombineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CombineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //关闭硬件加速，不过不执行这行，drawLine画出的虚线会是实线，drawCircle描边正常
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        initData(context, attrs, defStyleAttr);

    }
    private void initData(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//         是否这个视图自己绘制，如果这个视图没有自己做任何绘图，那么设置这个标志 ，允许进一步的优化。 默认情况下，该标志未设置，查看，但可以在某些View子类（如ViewGroup）上设置。
        setWillNotDraw(false);//-------------------------------------这个必须有
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CombineChart);
        //柱子颜色
        int columnColor = typedArray.getColor(R.styleable.CombineChart_columnColor, 0);
        //点的颜色
        int pointColor = typedArray.getColor(R.styleable.CombineChart_pointColor, 0);
        //文字颜色
        int textColor = typedArray.getColor(R.styleable.CombineChart_textColor, 0);
        typedArray.recycle();
        //柱子的画笔
        barPaint = new Paint();
        if(columnColor == 0) {
            barPaint.setColor(Color.parseColor("#6FC5F4"));//淡蓝色
        } else {
            barPaint.setColor(columnColor);
        }
        //文字的画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        if(textColor != 0) {
            textPaint.setColor(textColor);
        }

        //点的画笔
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        if(pointColor != 0) {
            pointPaint.setColor(pointColor);
        } else {
            pointPaint.setColor(Color.parseColor("#6FC5F4"));
        }

        barRect = new RectF();//这是柱子

        //底部偏移20dp，顶部偏移30dp
        botMargin=ScreenUtils.dp2px(context, 50);
        topMargin= ScreenUtils.dp2px(context, 35);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //获取控件的宽高
        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();
        //设置矩形的顶部 底部 ，最大高度
        getStatusHeight();
        super.onSizeChanged(w, h, oldw, oldh);
        //获取柱子宽度和空白区域的宽度
        getItemsWidth(screenWidth, list.size());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景颜色
        canvas.drawColor(Color.parseColor("#FFFFFF"));
        if (list == null||list.size()==0) return;
        //画矩形，划线，花点
        drawBars(canvas);
        canvas.save();
    }


    private void drawBars(Canvas canvas) {
        for (int i = 0; i < list.size(); i++) {
            //x起点 + 柱子宽度 * i（0的时候就是第一个，就不用偏移了） + 空白区域*（i+1），这里0的时候也要偏移一个左边空白
            barRect.left = (int) (xStartIndex + barWidth * i + barSpace * (i + 1));
            //柱子最大高度 - 这条柱子与最长柱子的比例 * 控件最大高度，相当于最大高度就留给最长柱子，其他都按比例减 + 偏移量，相当于所有子项都要偏移一个偏移量
            barRect.top = (int) maxHeight - (int) (maxHeight * (list.get(i).getTelephone_charge() / maxTelephone_charge))+topMargin;
            //右边等于左边加柱子宽度，底部都是一样，刚刚已经设置过了，就不再设置了
            barRect.right = barRect.left + barWidth;
            //画圆角矩形
            canvas.drawRoundRect(barRect, 6,6,barPaint);
            //再在下面画个矩形，把下面的圆角变直角
            if((barRect.bottom - barRect.top) > 12) {
                canvas.drawRect(barRect.left, barRect.bottom - 6, barRect.right, barRect.bottom , barPaint);
            }
            //确定线形图的路径 和 画圆点 1和2是原点位置，3是半径，4是画笔
            //这里y值有点绕，先算出数值/最大数值，再*柱子的最大数值，再用最长柱子数减去这个数，再加上偏移量 再减半个柱子宽（为了跟柱子一样高的时候全跑上去，不然在底部会跑到线下面）
            canvas.drawCircle(barRect.left + barWidth / 2, maxHeight - ((float) list.get(i).getTelephone_count() / (float) maxTelephone_count * maxHeight) + topMargin  - barWidth / 2, barWidth / 2, pointPaint);
            //设置文字大小
            textPaint.setTextSize(ScreenUtils.dp2px(getContext(), 14));
            //计算文本宽度，measureText() 是针对于特定字体大小来测量字符串宽度的，否则按默认测量得到的值就不一定有意义了。所以需要添加对待测量字符串字体大小的设置：
            float tsize = textPaint.measureText(list.get(i).getYear() + "");
            //先算出文字最左边点，找到柱子中间点 再减去文字的一半宽度
            Float tx = barRect.left+barWidth / 2-tsize/2;
            //点上方的文字，1文字，2，x位置，3，y位置是顶部偏移量上去三分之一
            canvas.drawText(list.get(i).getYear() + "",tx, screenHeight - botMargin / 4,textPaint);
            //月
            tsize = textPaint.measureText(list.get(i).getMonth() + "月");
            tx = barRect.left+barWidth / 2-tsize/2;
            canvas.drawText(list.get(i).getMonth() + "月",tx,screenHeight - botMargin / 1.7f, textPaint);
        }
        float lineWidth = 4f;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineWidth);
        paint.setColor(Color.parseColor("#EBEBEB"));
        //  下面画3条线
        float baseLineStartX = xStartIndex;
        float baseLineY = barRect.bottom + lineWidth / 2;
        float baseLineStopX = xStartIndex + barWidth * list.size() + barSpace * (list.size() + 1);
        //画底线
        canvas.drawLine(baseLineStartX, baseLineY, baseLineStopX, baseLineY, paint);
        //DashPathEffect是PathEffect类的一个子类,可以使paint画出类似虚线的样子,并且可以任意指定虚实的排列方式.
        PathEffect effects = new DashPathEffect(new float[] {10, 5}, 0);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(effects);
        Paint textPaint = new Paint();
        textPaint.setTextSize(ScreenUtils.dp2px(getContext(), 14));
        float tsize = textPaint.measureText(aveTelephone_count + "次");
        //算出文字最大高度
        double tHeight = getTextHeight(textPaint);
        //画月均通话次数,写字
        paint.setColor(Color.parseColor("#FFD300"));
        textPaint.setColor(Color.parseColor("#FFD300"));
        canvas.drawLine(baseLineStartX, baseLineY - aveTelephone_count / maxTelephone_count * maxHeight, baseLineStopX, baseLineY - aveTelephone_count / maxTelephone_count * maxHeight, paint);
        canvas.drawText(String.format("%.0f次", aveTelephone_count), baseLineStopX - tsize, baseLineY - aveTelephone_count / maxTelephone_count * maxHeight - ((float) tHeight / 4), textPaint);
        //画月均话费，写字
        tsize = textPaint.measureText(aveTelephone_charge + "元");
        paint.setColor(Color.parseColor("#F77304"));
        textPaint.setColor(Color.parseColor("#F77304"));
        canvas.drawLine(baseLineStartX, baseLineY - aveTelephone_charge / maxTelephone_charge * maxHeight, baseLineStopX, baseLineY - aveTelephone_charge / maxTelephone_charge * maxHeight, paint);
        canvas.drawText(String.format("%.2f元", aveTelephone_charge), 0, baseLineY - aveTelephone_charge / maxTelephone_charge * maxHeight - ((float) tHeight / 4), textPaint);
    }

    /**
     * 设置矩形的顶部 底部 右边Y轴的3部分每部分的高度
     */
    private void getStatusHeight() {
        //顶部等于偏移一个topMargin
        barRect.top =topMargin;
        //底部等于总高减下margin
        barRect.bottom = screenHeight-botMargin;
        //控件最大高度
        maxHeight = barRect.bottom - barRect.top;
    }

    public void setChartData(List<OperatorChartBean> list) {
        if (list==null||list.size()==0){
            throw new RuntimeException("数据不能为null");
        }
        this.list = list;
        int sumTelephone_count = 0;
        float sumTelephone_charge = 0.0f;
        for (OperatorChartBean item : list){
            //先获取要比对的数值
            float val = item.getTelephone_charge();
            //记录总话费
            sumTelephone_charge += val;
            //记录下最大值
            if (val>maxTelephone_charge){
                maxTelephone_charge=val;
            }
            //记录下最大通话数量
            int valCount = item.getTelephone_count();
            //记录总通话数
            sumTelephone_count += valCount;
            if(valCount > maxTelephone_count) {
                maxTelephone_count = valCount;
            }
        }
        aveTelephone_charge = sumTelephone_charge / list.size();
        aveTelephone_count = sumTelephone_count / list.size();
        invalidate();
    }

    /**
     * 设定每个bar的宽度
     *
     * @param screenWidth
     * @param size
     */
    private void getItemsWidth(int screenWidth, int size) {
        //总宽度/大小和间距
        barWidth = screenWidth / (4 * size);//每个柱子的宽度
        barSpace = (screenWidth - barWidth * size) / (size + 1);//空白区域的宽度
    }

    public double getTextHeight(Paint mPaint) {
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        return fm.bottom - fm.top;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < list.size(); i++) {
                    //x起点 + 柱子宽度 * i（0的时候就是第一个，就不用偏移了） + 空白区域*（i+1），这里0的时候也要偏移一个左边空白
                    barRect.left = (int) (xStartIndex + barWidth * i + barSpace * (i + 1));
                    //柱子最大高度 - 这条柱子与最长柱子的比例 * 控件最大高度，相当于最大高度就留给最长柱子，其他都按比例减 + 偏移量，相当于所有子项都要偏移一个偏移量
                    barRect.top = (int) maxHeight - (int) (maxHeight * (list.get(i).getTelephone_charge() / maxTelephone_charge)) + topMargin;
                    //右边等于左边加柱子宽度，底部都是一样，刚刚已经设置过了，就不再设置了
                    barRect.right = barRect.left + barWidth;
                    if (event.getX() >= barRect.left && event.getX() <= barRect.right && event.getY() >= barRect.top && event.getY() <= barRect.bottom) {
                        inside = true;
                        if (null != listener) {
                            //返回第几个柱子，和顶部中间的坐标位，方便显示对话框
                            listener.show(i, (barRect.left + barRect.right) / 2, barRect.top);
                        }
                        break;
                    }
                }
                break;
                //这里放手或滑动取消，都要关掉窗口
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (inside) {
                    if (null != listener) {
                        listener.dismiss();
                    }
                }
                break;
        }
        return true;
    }

    public interface OnInsideTouchListener {
        void show(int index, float x, float y);
        void dismiss();
    }

    public void setOnTouchListener(OnInsideTouchListener listener) {
        this.listener = listener;
    }
}
