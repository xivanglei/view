package xl.gcs.com.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



/**
 * Created by admin on 2017/12/4.
 * 指示器
 */

public class SwitchIndicator extends View {
    private boolean rightShow = true;
    private Paint mNoramlPaint;
    private Paint mSelectPaint;
    private float mWidth;
    private float mHeight;
    private String mOpentext = "关闭";
    private String mCloseText = "打开";
    private Paint mBackGroud;
    private float mMoveX;
    private float mLeft = 0;
    private float selectWidth;
    private int mStartX;
    private int mMax_left;
    private boolean isOpen = false;
    private boolean isClick = false;
    private OnCheckChangeListener listener;
    private Paint leftTextPaint;
    private Paint rightTextPaint;
    private Paint mIndicatorPaint;
    private int mNormalSize;
    private int mSelectorSize;

    public SwitchIndicator(Context context) {
        this(context, null);
    }

    public SwitchIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs, defStyleAttr);
    }

    private void initData(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.SwitchIndicator);
        //选中的文字颜色
        int selectorColor = typedArray.getColor(R.styleable.SwitchIndicator_selectorColor, 0);
        //正常的文字颜色
        int normalColor = typedArray.getColor(R.styleable.SwitchIndicator_normalColor, 0);
        //正常文字大小
        mNormalSize = typedArray.getDimensionPixelSize(R.styleable.SwitchIndicator_normalSize, 0);
        //选中文字大小
        mSelectorSize = typedArray.getDimensionPixelSize(R.styleable.SwitchIndicator_selectorSize, 0);
        //指标背景
        int indicatorColor = typedArray.getColor(R.styleable.SwitchIndicator_bgIndicator, 0);
        //外圈描边背景
        int bgColor = typedArray.getColor(R.styleable.SwitchIndicator_bgColor, 0);
        //左边文字
        String leftText = typedArray.getString(R.styleable.SwitchIndicator_closeText);
        //右边文字
        String rightText = typedArray.getString(R.styleable.SwitchIndicator_openText);
        //资源回收
        typedArray.recycle();
        if (!TextUtils.isEmpty(leftText)) {
            mCloseText = leftText;
        }
        if (!TextUtils.isEmpty(rightText)) {
            mOpentext = rightText;
        }
        //初始化正常状态的文字画笔，参数构造是开启抗锯齿
        mNoramlPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNoramlPaint.setColor(0xff50a9fb);
        mNoramlPaint.setStyle(Paint.Style.FILL);
        //设为中对齐
        mNoramlPaint.setTextAlign(Paint.Align.CENTER);
        //如果有设置正常的文字颜色
        if (normalColor != 0) {
            mNoramlPaint.setColor(normalColor);
        }

        //外圈的边画笔，开启抗锯齿
        mBackGroud = new Paint(Paint.ANTI_ALIAS_FLAG);
        //画笔设为描边样式
        mBackGroud.setAntiAlias(true);
        mBackGroud.setStyle(Paint.Style.STROKE);
        //设置描边宽度
        mBackGroud.setStrokeWidth(4);
        mBackGroud.setColor(0xff50a9fb);
        //如果有传进来颜色
        if (bgColor!=0) {
            mBackGroud.setColor(bgColor);
        }
        //指示标的画笔
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setColor(0xff50a9fb);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setTextAlign(Paint.Align.CENTER);
        if (indicatorColor!=0) {
            mIndicatorPaint.setColor(indicatorColor);
        }
        //选中文字的画笔
        mSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectPaint.setColor(0xffffffff);
        mSelectPaint.setTextAlign(Paint.Align.CENTER);

        //设置点击事件
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是点击事件与右边显示才执行下面的操作
                if (isClick&&rightShow) {
                    //如果开关为开，则关，反之，mStartX每次按下去都会记录，如果小于一半说明在左边，就关掉
                    if (isOpen && mStartX < mWidth / 2) {
                        isOpen = false;
                        //左边等于0
                        mLeft = 0;
                        //左边准备画字的画笔设为选中的颜色
                        leftTextPaint = mSelectPaint;
                        //右边设为正常显示的颜色
                        rightTextPaint = mNoramlPaint;
                    } else if (!isOpen && mStartX >= mWidth / 2) {
                        isOpen = true;
                        mLeft = mMax_left;
                        leftTextPaint = mNoramlPaint;
                        rightTextPaint = mSelectPaint;
                    }
                    if (listener != null) {
                        //如果有设置点击事件的话就回调状态改变
                        listener.onCheckChanged(mLeft == mMax_left);
                    }

                    invalidate();//刷新方法，强制view进行重新绘制，重新调用ondraw方法
                }
            }
        });
        //默认设置字的画笔
        leftTextPaint = mSelectPaint;
        rightTextPaint = mNoramlPaint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽高度模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取宽高度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //如果是wrap_content
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(350, 87);
        } else if(heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, 87);
        } else if(widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(350, height);
        }

        //获取宽高
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        //指示标的宽度,这里圆角倒掉了，看上去一半一半，不太美观，就让倒圆角的指示标那半往那边偏一点，看上去更像一半一半
        selectWidth = 1 / 2.0f * mWidth + mHeight / 4;
        //记录如果指示器在右边的话，最左的位置
        mMax_left = (int) (mWidth - selectWidth );
        //如果打开状态，就把左边缘设为右边指示器的位置
        if(isOpen) {
            mLeft = mMax_left;
        }
        //如果有设置正常文字的尺寸
        if (mNormalSize == 0) {
            mNoramlPaint.setTextSize(mHeight / 2);
        } else {
            mNoramlPaint.setTextSize(mNormalSize);
        }
        if (mSelectorSize == 0) {
            mSelectPaint.setTextSize(mHeight / 1.8f);
        } else {
            mSelectPaint.setTextSize(mSelectorSize);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //算出左半边的中心线，一会写字的时候用
        float openLeft = selectWidth / 2.0f;
        //算出右半边的中心线，一会写字的时候用
        float closeLeft = mWidth - selectWidth / 2;
        if (rightShow) {
            //画背景描边，这里注意描边是一半留里面，一半多出来的留外面，left如果设为0，那描边多出来的部分就显示到外面了，所以这里设置2，留2宽度给描边，描边超过4也会有部分不显示
            canvas.drawRoundRect(new RectF(2, 2, mWidth - 2, mHeight - 2), mHeight / 2 , mHeight / 2.0f , mBackGroud);
            //画指示标
            canvas.drawRoundRect(new RectF(mLeft , 0, selectWidth + mLeft , mHeight ), mHeight / 2, mHeight / 2.0f, mIndicatorPaint);
            //因为画笔设为中对齐，所以x位置只要找到中心线位置就行了，如果是正常显示的，还需要往边上靠一点点
            // y的中心线+ y的位置是算出字的最大高度/4(下是正方向),如果/2就太往下了，因为/2只能算出最低线，跟基准线不是一个概念
            canvas.drawText(mCloseText, leftTextPaint.equals(mNoramlPaint) ? openLeft - mHeight / 10 : openLeft, (float) (mHeight / 2 + getTextHeight(leftTextPaint) / 4), leftTextPaint);
            canvas.drawText(mOpentext, rightTextPaint.equals(mNoramlPaint) ? closeLeft + mHeight / 10 : closeLeft, (float) (mHeight / 2 + getTextHeight(rightTextPaint) / 4), rightTextPaint);
        } else {
            //如果右边不显示，只要写字就行了
            canvas.drawText(mCloseText,mWidth/2.0f,(float) (mHeight / 2 + getTextHeight(mNoramlPaint) / 4),mIndicatorPaint);
        }
    }


    //算出字符最大高度（不是正常高度，正常高度还有一部分会在外面）
    public double getTextHeight(Paint mPaint) {
        //字体的度量，是指对于指定字号的某种字体，在度量方面的各种属性，其描述参数包括：baseline：字符基线,ascent：字符最高点到baseline的推荐距离,top：字符最高点到baseline的最大距离
        //descent：字符最低点到baseline的推荐距离,bottom：字符最低点到baseline的最大距离,leading：行间距，即前一行的descent与下一行的ascent之间的距离
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        return fm.bottom - fm.top;
    }

    //触摸方法，当手指触摸控件时调用这个方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!rightShow) {
            return super.onTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1、记录起始点：按下的点startX，如果不是触摸事件也可以交给点击事件做判断，判断是点哪边
                mStartX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //记录移动位置
                int moveX = (int) event.getX();
                //记录下移动的距离
                int diffX = moveX - mStartX;
                //记录手指移动的总间距叠加(注意间距是有正负,所以用绝对值)
                mMoveX = mMoveX + Math.abs(diffX);
                //4、更新指示标的左边界，准备跟着手指滑动
                mLeft = mLeft + diffX;
                //如果小于0，也没必要跑出去
                if (mLeft < 0) {//设置左边界
                    mLeft = 0;
                }
                //如果大于最大边界，会导致右边画不下，有部分画到外面去，所以不能大于最大边界
                if (mLeft > mMax_left) {//设置右边界
                    mLeft = mMax_left;
                }
                //5、即时刷新界面
                invalidate();
                //6、更新起始点
                mStartX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                if (mMoveX > 8) {//移动的总间距大于8个像素，触摸事件
                    System.out.println("触摸事件");
                    isClick = false;
                } else {//移动的总间距小于等于8个像素，点击事件
                    System.out.println("点击事件");
                    isClick = true;
                }

                //手指抬起后，总间距归0
                mMoveX = 0;

                if (!isClick) {//如果是触摸事件才走下面的代码
                    //计算出中心线，差不多是4分之1宽
                    int center = mMax_left / 2;
                    //如果左边线大于这条线
                    if (mLeft > center) {
                        //就要跑到右边去，让状态为打开状态
                        isOpen = true;
                        mLeft = mMax_left;
                        //左边字设为正常的
                        leftTextPaint = mNoramlPaint;
                        //右边字的画笔设为，选中子的画笔
                        rightTextPaint = mSelectPaint;
                    } else {
                        //否则相反
                        leftTextPaint = mSelectPaint;
                        rightTextPaint = mNoramlPaint;
                        isOpen = false;
                        mLeft = 0;
                    }
                    //重画
                    invalidate();
                    //如果设置了回调，就通知数据改变,isOpen来判断打开还是关闭状态
                    if (listener != null) {
                        listener.onCheckChanged(isOpen);
                    }
                }

                break;
        }
        return super.onTouchEvent(event);//交给上层处理，不消费
    }

    public void setOnCheckChangeListener(OnCheckChangeListener listener) {
        this.listener = listener;
    }

    public void setRightShow(boolean rightShow) {
        this.rightShow = rightShow;
        invalidate();
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
        mLeft = mMax_left;
        leftTextPaint = mNoramlPaint;
        rightTextPaint = mSelectPaint;
        invalidate();
    }



    public interface OnCheckChangeListener {
        public void onCheckChanged(boolean isOpen);
    }

    public void setOpen() {
        isOpen = true;
        leftTextPaint = mNoramlPaint;
        rightTextPaint = mSelectPaint;
        invalidate();
    }
}
