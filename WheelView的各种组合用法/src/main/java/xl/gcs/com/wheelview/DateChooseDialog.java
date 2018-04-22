package xl.gcs.com.wheelview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;


import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xianglei on 2018/3/30.
 */

public class DateChooseDialog extends Dialog {

    @BindView(R.id.wheel_view1)
    WheelView mWheelView1;
    @BindView(R.id.wheel_view2)
    WheelView mWheelView2;
    @BindView(R.id.wheel_view3)
    WheelView mWheelView3;

    private Context mContext;
    private Calendar mDate;
    private int mMaxDay;
    private List<String> options = new ArrayList<>();
    private List<String> options2 = new ArrayList<>();
    private List<String> options3 = new ArrayList<>();
    private OnCommitClickListener mListener;

    public DateChooseDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        mContext = context;
        mDate = Calendar.getInstance();
        mDate.set(1920, 01, 01);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_wheelview_option3, null);
        setContentView(view);
        ButterKnife.bind(this);
        initData();
        mWheelView1.setCyclic(false);
        mWheelView1.setAdapter(new ArrayWheelAdapter(options));
        mWheelView1.setGravity(Gravity.CENTER);
        mWheelView1.setCurrentItem(70);
        mWheelView1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                changeDayData();
            }
        });
        mWheelView2.setCyclic(false);
        mWheelView2.setAdapter(new ArrayWheelAdapter(options2));
        mWheelView2.setGravity(Gravity.CENTER);
        mWheelView2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                changeDayData();
            }
        });
        mWheelView3.setCyclic(false);
        mWheelView3.setAdapter(new ArrayWheelAdapter(options3));
        mWheelView3.setGravity(Gravity.CENTER);
    }

    private void initData() {
        initYear();
        initMonth();
        initDay();
    }

    private void initYear() {
        int startYear = mDate.get(Calendar.YEAR);
        int nowYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = startYear; i < nowYear; i++) {
            options.add(i + "年");
        }
    }

    private void initMonth() {
        for(int i = 1; i < 13; i++) {
            options2.add(i + "月");
        }
    }

    //年是通过设置的，月一共12个月，最麻烦的是日，28天也有31天也有
    private void initDay() {
        //年或月变化都先清空掉日吧
        options3.clear();
        //解析出年的数值和月的数值，因为之前ui要求加了年，现在减回去先，换成int等会要计算
        int year = Integer.parseInt(options.get(mWheelView1.getCurrentItem()).substring(0, options.get(mWheelView1.getCurrentItem()).indexOf("年")));
        int month = Integer.parseInt(options2.get(mWheelView2.getCurrentItem()).substring(0, options2.get(mWheelView2.getCurrentItem()).indexOf("月")));
        if(month == 2) {
            //2月份需要计算闰年
            if(year % 4 == 0) {
                mMaxDay = 29;
            } else {
                mMaxDay = 28;
            }
            //刚已经计算好2月份了，其他月份都是固定的
        } else if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            mMaxDay = 31;
        } else {
            mMaxDay = 30;
        }
        for(int i = 1; i <= mMaxDay; i++) {
            options3.add(i + "日");
        }

    }

    private void changeDayData() {
        initDay();
        mWheelView3.setCurrentItem(mWheelView3.getCurrentItem());
    }

    public interface OnCommitClickListener{
        void onCommitClick(long time);
    }
    public void setOnCommitClickListener(OnCommitClickListener listener) {
        mListener = listener;
    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        //0包括 年不包括，解析年月日数值
        int year = Integer.parseInt(options.get(mWheelView1.getCurrentItem()).substring(0, options.get(mWheelView1.getCurrentItem()).indexOf("年")));
        int month = Integer.parseInt(options2.get(mWheelView2.getCurrentItem()).substring(0, options2.get(mWheelView2.getCurrentItem()).indexOf("月")));
        int day = Integer.parseInt(options3.get(mWheelView3.getCurrentItem()).substring(0, options3.get(mWheelView3.getCurrentItem()).indexOf("日")));
        Calendar date = Calendar.getInstance();
        date.set(year, month - 1, day, 0, 0 , 0);
        long time = date.getTimeInMillis();
        mListener.onCommitClick(time);
        dismiss();
    }
}
