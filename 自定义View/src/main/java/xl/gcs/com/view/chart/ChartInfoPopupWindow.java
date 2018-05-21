package xl.gcs.com.view.chart;

import android.app.Activity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import xl.gcs.com.view.R;

import static android.content.ContentValues.TAG;

/**
 * Created by xianglei on 2018/4/24.
 */

public class ChartInfoPopupWindow extends PopupWindow {
    private View contentView;
    private Activity context;
    @BindView(R.id.tv_telephone_charge)
    TextView tv_telephone_charge;
    @BindView(R.id.tv_telephone_count)
    TextView tv_telephone_count;

    public ChartInfoPopupWindow(Activity context) {
        this(context, null);
    }

    public ChartInfoPopupWindow(Activity context, AttributeSet attrs) {
        this(context, attrs, R.attr.popupWindowStyle);
    }
    public ChartInfoPopupWindow(Activity context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
        this.context = context;
        this.initPopupWindow();
    }

    private void initPopupWindow() {
        LayoutInflater inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.pop_win_chart_info, null);
        ButterKnife.bind(this, contentView);
        this.setContentView(contentView);
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        setHeight((int)(w * 0.1361));
        setWidth((int)(w * 0.2347));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
    }

    public void showAtDropDownCenter(View parent, int x, int y, OperatorChartBean data) {
        if (parent.getVisibility() == View.GONE) {
            this.showAtLocation(parent, 0, 0, 0);
        } else {
            int[] location = new int[2];
            //获取控件(左上角位置)在整个屏幕内的绝对坐标
            parent.getLocationOnScreen(location);
            this.showAtLocation(parent, 0, location[0] + x - this.getWidth() / 2, location[1] + y - this.getHeight());
            tv_telephone_charge.setText(data.getTelephone_charge() + "元话费");
            tv_telephone_count.setText(data.getTelephone_count() + "次通话");
        }
    }
}

