package xl.gcs.com.popupwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtn_left_add;
    private Button mBtn_right_add;
    private Button mBtn_custom;
    private PopupWindow mPopWindow;
    private PopupList mPopupList;
    private View mContentView;
    private MyPopupWindow mMyPopupWindow;
    private WheelViewPopupWindow mWheelViewPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        mBtn_left_add = (Button) findViewById(R.id.btn_left_add);
        mBtn_right_add = (Button) findViewById(R.id.btn_right_add);
        mBtn_custom = (Button) findViewById(R.id.btn_custom);
        initEvent();
    }

    private void initEvent() {
        mBtn_left_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移,试过，参1可以选一个父控件里的任意一个子控件，不过正常应该是下面这种
//                mPopWindow.showAtLocation(mBtn_right_add, Gravity.BOTTOM, 0, 0);

                //显示PopupWindow的控件
                View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
                mPopupList.showAtDropDownCenter(mBtn_left_add);
            }
        });


        mBtn_right_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示在某一控件的下方,窗口的左上对标记控件的左下，位置不够会自动调整
//                mPopWindow.showAsDropDown(mBtn_right_add);
                //显示在某异空间下方并偏移,x方向，和y方向，可以正负，窗口的左上对标记控件的左下，为原点，所以有时偏了很多都没动，以为之前位置不够已经调整过了，所以要偏更多才有效果
                mPopWindow.showAsDropDown(mBtn_right_add, 100, 180);
            }
        });

        mBtn_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mChartInfoPopupWindow.showAtDropDownCenter(mBtn_custom, 1, 3);
                mMyPopupWindow.showAtDropDownCenter(mBtn_custom);
//                mMyPopupWindow.showAtDropDownLeft(mBtn_custom);
//                mWheelViewPopupWindow.showAtLocation(mBtn_custom, Gravity.BOTTOM, 0, 0);
            }
        });
        mContentView.findViewById(R.id.fl_add_sell_house).setOnClickListener(this);
        mContentView.findViewById(R.id.fl_add_buyer).setOnClickListener(this);
        mContentView.findViewById(R.id.fl_add_rent_house).setOnClickListener(this);
        mContentView.findViewById(R.id.fl_add_seller).setOnClickListener(this);
        mPopupList.setOnItemClickListener(new PopupList.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                switch (item) {
                    case "第一":
                        Toast.makeText(MainActivity.this, "选了第一", Toast.LENGTH_SHORT).show();
                        break;
                    case "第二":
                        Toast.makeText(MainActivity.this, "选了第二", Toast.LENGTH_SHORT).show();
                        break;
                    case "第三":
                        Toast.makeText(MainActivity.this, "选了第三", Toast.LENGTH_SHORT).show();
                        break;
                    case "第四":
                        Toast.makeText(MainActivity.this, "选了第四", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initData() {
        showViewPopupWindow();
        mPopupList = new PopupList(this, new String[] {"第一", "第二", "第三", "第四"});
        mMyPopupWindow = new MyPopupWindow(this);
        mWheelViewPopupWindow = new WheelViewPopupWindow(this);

    }

    private void showPopupWindow() {
        //设置contentView，获得弹窗布局
        mContentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.window_add, null);
        //设置布局，宽高，是否可获得焦点
        mPopWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
    }

    private void showViewPopupWindow() {
        //获得弹窗布局
        mContentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.window_add, null);
        //设置布局
        mPopWindow = new PopupWindow();
        mPopWindow.setContentView(mContentView);
        //设置宽高，这里设置准确数值就会以这里为准，一半就是WRAP_CONTENT
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置动画
        mPopWindow.setAnimationStyle(R.style.AnimBottom);
        //另外，外面变暗的方法很简单，布局一个整体页面，背景颜色半透明，直接摆放好PopWindow位置，代码里宽高都是MATCH_PARENT，再在父布局点击事项设为dismiss就好了


        //设置PopupWindow是否响应touch事件，默认是true，如果设置为false，即会是下面这个结果：(所有touch事件无响应，包括点击事件)
        mPopWindow.setTouchable(true);
        //PopupWindow是否具有获取焦点的能力，默认为False。如果需要EditText就需要获取焦点能力
        mPopWindow.setFocusable(true);
        // 这个函数的意义，就是指，PopupWindow以外的区域是否可点击,设为true就是点外面部分就消失
        mPopWindow.setOutsideTouchable(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_add_buyer:
                Toast.makeText(this, "买家", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                break;
            case R.id.fl_add_rent_house:
                Toast.makeText(this, "租房", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                break;
            case R.id.fl_add_sell_house:
                Toast.makeText(this, "卖房", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                break;
            case R.id.fl_add_seller:
                Toast.makeText(this, "卖家", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                break;
            default:
                break;
        }
    }
}
