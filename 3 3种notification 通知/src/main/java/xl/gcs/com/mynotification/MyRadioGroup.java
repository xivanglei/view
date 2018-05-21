package xl.gcs.com.mynotification;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by xianglei on 2018/4/20.
 * 自定义radioGroup 可以多行多层，只要里面有radioButton都能正常用
 */

public class MyRadioGroup extends RadioGroup {

    private OnCheckedChangeListener mOnCheckedChangeListener;
    private RadioButton mRadioButton;

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

        //如果子控件是ViewGroup或子类
        if (child instanceof ViewGroup) {
            //就遍历检查下所有的子控件
            checkChildView(child);
        }
        super.addView(child, index, params);
        //先记录下有没有选中的RadioButton，主要比较后面点的有没有改变，这方法要在super.addView之后，否则没加载还没子控件，没法判断
        checkRadioButtonChecked();
    }

    //检查所有的RadioButton，否掉没选中的
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
                //如果不是RadioButton 但是ViewGroup或者他的子类
            } else if (child instanceof ViewGroup) {
                //遍历检查一遍子类，看看有没有RadioButton
                checkChildRadioButton(child, radioButton);
            }
        }
    }

    //检查所有ViewGroup的子控件，看看有没有RadioButton
    private void checkChildView(View child) {
        int childCount = ((ViewGroup) child).getChildCount();
        for (int i = 0; i < childCount; i++) {
            //获得ViewGroup的子控件
            final View view = ((ViewGroup) child).getChildAt(i);
            //如果是RadioButton
            if (view instanceof RadioButton) {
                final RadioButton button = (RadioButton) view;
                //如果一个ViewGroup只有一个RadioButton。  这里如果要扩大点击范围，还需要增加让ViewGroup也同时监听，但注意，RadioGroup里面所有的子控件里就不能有直接RadioButton了，只能是ViewGroup内部包含RadioButton,而且任意一个ViewGroup里只能有一个或没有RadioButton，不能多个
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mRadioButton != button) {
                            mRadioButton = button;
                            //设置为选中
                            button.setChecked(true);
                            //检查一遍主要是把其他radioButton项的checked设为false
                            checkRadioButton(button);
                            if (mOnCheckedChangeListener != null) {
                                //然后运行RadioGroup里面的回调方法，传入id判断点击的是哪个
                                mOnCheckedChangeListener.onCheckedChanged(MyRadioGroup.this, button.getId());
                            }
                        }
                    }
                });
                //如果不是RadioButton但是ViewGroup的实例，就继续遍历，这样不管多少层，只要有RadioButton都能发现
            } else if(view instanceof ViewGroup) {
                checkChildView(view);
            }
        }
    }

    //检查子控件里的RadioButton，否掉没选中的
    private void checkChildRadioButton(View child, RadioButton radioButton) {
        //就让里面的子控件遍历
        int childCount = ((ViewGroup) child).getChildCount();
        for (int j = 0; j < childCount; j++) {
            View view = ((ViewGroup) child).getChildAt(j);
            if (view instanceof RadioButton) {
                //同样，不是RadioButton不管他，是的话就跟传进来那个刚刚触摸点击的比较，如果是刚刚那个就不动，不是就设为false
                final RadioButton button = (RadioButton) view;
                if (button == radioButton) {
                    // do nothing
                } else {
                    button.setChecked(false);
                }
                //如果不是RadioButton但是ViewGroup的实例，就继续遍历，这样不管多少层，只要有RadioButton都能发现
            } else if(view instanceof ViewGroup) {
                checkChildRadioButton(view, radioButton);
            }
        }
    }

    private void checkRadioButtonChecked() {
        View child;
        //radioGroup 有多少子控件
        int radioCount = getChildCount();
        //遍历子控件
        for (int i = 0; i < radioCount; i++) {
            child = getChildAt(i);
            //如果这控件是radioButton,同时选中了
            if (child instanceof RadioButton && ((RadioButton) child).isChecked()) {
                //记录下选中的,主要判断一会有没有改变，因为RadioGroup只能有一个被选中，所以选中了就可以断掉了
                mRadioButton = (RadioButton) child;
                break;
                //如果不是RadioButton 但是ViewGroup或者他的子类
            } else if (child instanceof ViewGroup) {
                //遍历检查一遍子类，看看有没有RadioButton
                checkChildRadioButtonChecked(child);
            }
        }
    }

    //检查子控件里的RadioButton，记录选中的
    private void checkChildRadioButtonChecked(View child) {
        //就让里面的子控件遍历
        int childCount = ((ViewGroup) child).getChildCount();
        for (int j = 0; j < childCount; j++) {
            View view = ((ViewGroup) child).getChildAt(j);
            //如果这控件是radioButton,同时选中了
            if (view instanceof RadioButton && ((RadioButton) view).isChecked()) {
                //记录下选中的,主要判断一会有没有改变，因为RadioGroup只能有一个被选中，所以选中了就可以断掉了
                mRadioButton = (RadioButton) view;
                break;
                //如果不是RadioButton但是ViewGroup的实例，就继续遍历，这样不管多少层，只要有RadioButton都能发现
            } else if(view instanceof ViewGroup) {
                checkChildRadioButtonChecked(view);
            }
        }
    }

    //查询现在选中的RadioButton id，复写方法，父类返回的id这里都没存入
    @Override
    public int getCheckedRadioButtonId() {
        //如果有选过或初始选中一项就会记录下来，没选过就是null
        if(mRadioButton != null) {
            //返回Id
            return mRadioButton.getId();
        } else {
            //没有就返回-1
            return -1;
        }
    }
}