package xl.gcs.com.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xianglei on 2018/4/24.
 */

public class WheelViewPopupWindow extends PopupWindow{

    private View conentView;
    private Activity context;
    @BindView(R.id.wheel_view1)
    WheelView mWheelView1;
    @BindView(R.id.wheel_view2)
    WheelView mWheelView2;
    @BindView(R.id.wheel_view3)
    WheelView mWheelView3;

    private OnCommitClickListener listener;
    private Calendar mDate;
    private int mHourValue, mMinuteValue;
    private int mOldDateIndex;
    private int mMinHour;
    private int mMinMinute;
    private List<String> options = new ArrayList<>();
    private List<String> options2 = new ArrayList<>();
    private List<String> options3 = new ArrayList<>();

    public WheelViewPopupWindow(final Activity context) {
        this(context, null);
        this.context = context;
        mDate = Calendar.getInstance();
        initPopupWindow();
    }

    public WheelViewPopupWindow(Activity context, AttributeSet attrs) {
        this(context, attrs, R.attr.popupWindowStyle);
    }
    public WheelViewPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    private void initPopupWindow() {
        //使用view来引入布局
        LayoutInflater inflater = LayoutInflater.from(context);
        conentView = inflater.inflate(R.layout.dialog_wheelview_select_date, null);
        //获取手机屏幕的高度与宽度
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置显示的弹窗
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽,这里设置半个屏幕的宽度+50像素
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置点击外部就会消失
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        this.setAnimationStyle(R.style.AnimBottom);
        ButterKnife.bind(this, conentView);
        mWheelView1.setCyclic(false);
        mWheelView1.setGravity(Gravity.CENTER);
        updateDateControl();
        mWheelView1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mDate.add(Calendar.DAY_OF_YEAR,index - mOldDateIndex);      //选择后的位置-之前的位置
                updateDateControl();                                                //这里运行完又会记住现在选的位置
                if(mDate.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                    mMinHour = 0;
                    mWheelView2.setCyclic(true);
                } else {
                    mMinHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    mWheelView2.setCyclic(false);
                }
                //判断了日期设置了最小的小时数后，就初始化小时
                initHourData();
                //接着判断下分钟，如果是今天，就不能选择之前的时间，因为已经过去了，没必要选
                compareHour();
            }
        });
        mWheelView2.setCyclic(false);
        mWheelView2.setGravity(Gravity.CENTER);
        mMinHour = mDate.get(Calendar.HOUR_OF_DAY);
        mHourValue = mMinHour;                      //记下现在的时间，后面加日期用
        initHourData();
        mWheelView2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int newValue = Integer.parseInt(options2.get(mWheelView2.getCurrentItem()));    //得到选择的数，解析成int
                mDate.add(Calendar.HOUR_OF_DAY,newValue - mHourValue);                  //新小时数-旧的，就知道要加还是减多少时间了
                mHourValue = newValue;                                                          //新的小时数再存起来
                compareHour();

            }
        });
        mWheelView3.setCyclic(false);
        mWheelView3.setGravity(Gravity.CENTER);
        mMinMinute = Calendar.getInstance().get(Calendar.MINUTE);
        mMinuteValue = mMinMinute;
        initMinuteData();
        mWheelView3.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int newValue = Integer.parseInt(options3.get(mWheelView3.getCurrentItem()));
                mDate.add(Calendar.MINUTE,newValue - mMinuteValue);
                mMinuteValue = newValue;
            }
        });

    }

    private void initMinuteData() {
        options3.clear();
        for(int i = mMinMinute; i < 60; i++) {
            options3.add(i + "");
        }
        mWheelView3.setAdapter(new ArrayWheelAdapter(options3));
        selectMinuteData();
    }

    private void initHourData() {
        options2.clear();
        for(int i = mMinHour; i <= 23; i++) {
            options2.add(i + "");
        }
        mWheelView2.setAdapter(new ArrayWheelAdapter(options2));
        selectHourData();
    }

    private void selectHourData() {
        int newValue = Integer.parseInt(options2.get(0));
        int index = mHourValue - newValue;
        if(index > 0) {
            mWheelView2.setCurrentItem(index);
        } else {
            mWheelView2.setCurrentItem(0);
            mDate.set(Calendar.HOUR_OF_DAY, newValue);
            mHourValue = newValue;
        }
    }

    private void selectMinuteData() {
        int newValue = Integer.parseInt(options3.get(0));
        int index = mMinuteValue - newValue;
        if(index > 0) {
            mWheelView3.setCurrentItem(index);
        } else {
            mWheelView3.setCurrentItem(0);
            mDate.set(Calendar.MINUTE, newValue);
            mMinuteValue = newValue;
        }
    }

    private void updateDateControl() {
        Calendar cal = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        cal.setTimeInMillis(mDate.getTimeInMillis());
        int i = 21;
        cal.add(Calendar.DAY_OF_YEAR, -i / 2 - 1);
        options.clear();
        for(int a = 0; a < i; a++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            int c = cal.get(Calendar.DAY_OF_YEAR);
            int n = now.get(Calendar.DAY_OF_YEAR);
            if(c <  n) {
                a--;
                i--;
            } else {
                String display = (String) DateFormat.format("MM.dd EEEE", cal);
                options.add(display);
            }
        }
        mWheelView1.setAdapter(new ArrayWheelAdapter(options));
        int select = options.size() - 11;
        mWheelView1.setCurrentItem(select);
        mOldDateIndex = mWheelView1.getCurrentItem();               //记下老的时间，之后才能知道移动了多少位，就把日期加上
    }
    private void compareHour() {
        //如果日子大于今天，那就让分钟随便选，设为0
        if(mDate.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
            mMinMinute = 0;
            mWheelView3.setCyclic(true);
        } else {
            //如果日子=今天，就判断下小时如果等于现在的时间，就让分钟最小不能小于现在的分钟
            if (mDate.get(Calendar.HOUR_OF_DAY) > Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                mMinMinute = 0;
                mWheelView3.setCyclic(true);
            } else {
                mMinMinute = Calendar.getInstance().get(Calendar.MINUTE);
                mWheelView3.setCyclic(false);
            }
        }
        initMinuteData();
    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        if (listener!=null) {
            listener.onCommitClick(mDate.getTimeInMillis());
        }
        dismiss();
    }

    public void setOnCommitClickListener(OnCommitClickListener onCommitClickListener){
        this.listener = onCommitClickListener;
    }
    public interface OnCommitClickListener {
        void onCommitClick(long time);
    }
}
