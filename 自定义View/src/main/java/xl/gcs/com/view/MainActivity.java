package xl.gcs.com.view;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xl.gcs.com.view.chart.ChartInfoPopupWindow;
import xl.gcs.com.view.chart.CombineChart;
import xl.gcs.com.view.chart.OperatorChartBean;

/*
书上要点：
Activity.setContentView会调用 getWindow().setContentView(layoutResID); getWindow()会得到PhoneWindow
PhoneWindow会将DecorView（继承FrameLayout）作为整个窗口的根View,里面又会再加入子View TitleView与ContentView,简单说实现完了就是4层
Activity层最外，下面是PhoneWindow,DecorView,最里层是TitleView和ContentView，也是我们操作的层
分发机制，点击事件产生后，首先会传递给Activity,然后Activity会调用dispatchTouchEvent(),把事件交给PhoneWindow，接着又交给DecorView,再给根ViewGroup
顶层ViewGroup运行onInterceptTouchEvent()默认返回false不拦截，就会传下去，如果要拦截就要复写，返回true就好了，最后传到最底层View,他就算返回false也没用了
但是他可以不处理，用onTouchEvent()返回false,往上传，直到被某层ViewGroup消费，并返回true,就不传了，或者传到最上层都不处理

 */
public class MainActivity extends AppCompatActivity {
    private InvalidTextView iv_text;
    private  RectView rv_rect;
    private TitleBar mTitleBar;
    private List<OperatorChartBean> operatorChartBeans = new ArrayList<>();
    private ChartInfoPopupWindow mChartInfoPopupWindow;
    CombineChart combineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
        iv_text= (InvalidTextView) this.findViewById(R.id.iv_text);
        rv_rect= (RectView) this.findViewById(R.id.rv_rect);
        iv_text.setText("皇家马德里 C罗");

        mTitleBar= (TitleBar) this.findViewById(R.id.title);
        mTitleBar.setTitle("自定义组合控件");

        mTitleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击左键", Toast.LENGTH_SHORT).show();
            }
        });

        mTitleBar.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击右键", Toast.LENGTH_SHORT).show();
            }
        });

        operatorChartBeans.add(new OperatorChartBean(2017, 10, 10f, 10));
        operatorChartBeans.add(new OperatorChartBean(2017, 11, 20f, 30));
        operatorChartBeans.add(new OperatorChartBean(2017, 12, 30f, 50));
        operatorChartBeans.add(new OperatorChartBean(2018, 1, 40f, 70));
        operatorChartBeans.add(new OperatorChartBean(2018, 2, 50f, 90));
        operatorChartBeans.add(new OperatorChartBean(2018, 3, 60f, 120));
        mChartInfoPopupWindow = new ChartInfoPopupWindow(this);
        combineChart = (CombineChart) findViewById(R.id.chart);
        combineChart.setChartData(operatorChartBeans);
        combineChart.setOnTouchListener(new CombineChart.OnInsideTouchListener() {
            @Override
            public void show(int index, float x, float y) {
                mChartInfoPopupWindow.showAtDropDownCenter(combineChart, (int)x, (int)y, operatorChartBeans.get(index));
            }
            @Override
            public void dismiss() {
                mChartInfoPopupWindow.dismiss();
            }
        });
    }
}
