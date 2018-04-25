package xl.gcs.com.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by xianglei on 2018/4/24.
 */

public class MyPopupWindow extends PopupWindow {
    private View conentView;
    private Activity context;


    public MyPopupWindow(final Activity context) {
        this(context, null);
        this.context = context;
        this.initPopupWindow();

    }

    public MyPopupWindow(Activity context, AttributeSet attrs) {
        this(context, attrs, R.attr.popupWindowStyle);
    }
    public MyPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    private void initPopupWindow() {
        //使用view来引入布局
        LayoutInflater inflater = LayoutInflater.from(context);
        conentView = inflater.inflate(R.layout.popuo_dialog, null);
        //获取手机屏幕的高度与宽度
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        Log.d("1234", "initPopupWindow: " + h + "宽:" + w);
//         设置显示的弹窗
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽,这里设置半个屏幕的宽度+50像素
        this.setWidth(w / 6 + 50);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置点击外部就会消失
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作，试过注解掉，没关系，可能改版了
//        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果，设置动画
        this.setAnimationStyle(R.style.AnimBottom);
        //布局控件初始化与监听设置
        LinearLayout llayout_remind = (LinearLayout) conentView
                .findViewById(R.id.llayout_remind);
        LinearLayout llayout_history = (LinearLayout) conentView
                .findViewById(R.id.llayout_history);
        llayout_remind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

            }
        });

        llayout_history.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 显示popupWindow的方式设置，当然可以有别的方式。
     *一会会列出其他方法
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
        } else {
            this.dismiss();
        }
    }

    /**
     * 第一种
     * 显示在控件的下右方
     *
     * @param parent parent
     */
    public void showAtDropDownRight(View parent) {
        if (parent.getVisibility() == View.GONE) {
            this.showAtLocation(parent, 0, 0, 0);
        } else {
            // x y
            int[] location = new int[2];
            //获取控件(左上角位置)在整个屏幕内的绝对坐标
            parent.getLocationOnScreen(location);
            Log.d("1234", "showAtDropDownRight: " + location[0] + "y" + location[1]);
            //在整个布局的位置，不指定gravity,偏移自己设，location[0] + parent.getWidth()表示下边右边，减一个自身宽度，就是下右边对齐的下方
            this.showAtLocation(parent, 0, location[0] + parent.getWidth() - this.getWidth(), location[1] + parent.getHeight());
        }
    }


    /**  第二种
     * 显示在控件的下中方
     *
     * @param parent parent
     */
    public void showAtDropDownCenter(View parent) {
        if (parent.getVisibility() == View.GONE) {
            this.showAtLocation(parent, 0, 0, 0);
        } else {
            // x y
            int[] location = new int[2];
            //获取控件(左上角位置)在整个屏幕内的绝对坐标
            parent.getLocationOnScreen(location);
            this.showAtLocation(parent, 0, location[0] + parent.getWidth() / 2 - this.getWidth() / 2, location[1] + parent.getHeight());
        }
    }



    /**   第三种
     * 显示在控件的下左方
     *
     * @param parent parent
     */
    public void showAtDropDownLeft(View parent) {
        if (parent.getVisibility() == View.GONE) {
            this.showAtLocation(parent, 0, 0, 0);
        } else {
            // x y
            int[] location = new int[2];
            //获取控件(左上角位置)在整个屏幕内的绝对坐标
            parent.getLocationOnScreen(location);
            this.showAtLocation(parent, 0, location[0], location[1] + parent.getHeight());
        }
    }


}
