package xl.gcs.com.mynotification;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by xianglei on 2018/4/20.
 * 自定义radioGroup 可以多行，只要里面有radioButton都能正常用
 * 只能是LinearLayout,而且不能多层，有需求自己另外做一套，逻辑不难
 */

public class MyRadioGroup extends RadioGroup {

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener;

    public MyRadioGroup(Context context) {
        super(context);
    }

    public MyRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    @Override
    public void addView(final View child, int index, ViewGroup.LayoutParams params) {
        //如果子控件是LinearLayout
        if (child instanceof LinearLayout) {
            int childCount = ((LinearLayout) child).getChildCount();
            for (int i = 0; i < childCount; i++) {
                //获得LinearLayout的子控件
                View view = ((LinearLayout) child).getChildAt(i);
                //如果是RadioButton
                if (view instanceof RadioButton) {
                    final RadioButton button = (RadioButton) view;
                    ((RadioButton) button).setOnTouchListener(new OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            ((RadioButton) button).setChecked(true);
                            //检查一遍主要是把其他radioButton项设为false
                            checkRadioButton((RadioButton) button);
                            if (mOnCheckedChangeListener != null) {
                                //然后运行RadioGroup里面的回调方法，传值
                                mOnCheckedChangeListener.onCheckedChanged(MyRadioGroup.this, button.getId());
                            }
                            return true;
                        }
                    });
                }
            }
        }

        super.addView(child, index, params);
    }

    private void checkRadioButton(RadioButton radioButton) {
        View child;
        //radioGroup 有多少子控件
        int radioCount = getChildCount();
        //遍历子控件
        for (int i = 0; i < radioCount; i++) {
            child = getChildAt(i);
            //如果这控件是radioButton
            if (child instanceof RadioButton) {
                //就检查下是不是刚刚点上去的那个
                if (child == radioButton) {
                    // do nothing 如果是，就什么都不做，因为刚刚已经设为checked 为true了
                } else {
                    // 如果不是刚刚点上去那个radioButton 那就要设为false了
                    ((RadioButton) child).setChecked(false);
                }
                //如果不是RadioButton 但是LinearLayout
            } else if (child instanceof LinearLayout) {
                //就让里面的子控件遍历
                int childCount = ((LinearLayout) child).getChildCount();
                for (int j = 0; j < childCount; j++) {
                    View view = ((LinearLayout) child).getChildAt(j);
                    if (view instanceof RadioButton) {
                        //同样，不是RadioButton不管他，是的话就跟传进来那个刚刚触摸点击的比较，如果是刚刚那个就不动，不是就设为false
                        final RadioButton button = (RadioButton) view;
                        if (button == radioButton) {
                            // do nothing
                        } else {
                            ((RadioButton) button).setChecked(false);
                        }
                    }
                }
            }
        }
    }
}