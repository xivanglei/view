package xl.gcs.com.myapplication.butterknife;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnLongClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import xl.gcs.com.myapplication.R;
//这里详细使用ButterKnife
//ButterKnife从严格意义讲不算是依赖注入框架，它只是专注于Android系统的View注入框架，并不支持其他方面的注入

public class ButterKnifeActivity extends AppCompatActivity {
    //普通绑定，这里TextView的修饰符不能是private 或 static,绑定好了就可以不用findViewById(R.id.tv_text)了
    @BindView(R.id.tv_text)
    TextView tv_text;
    //下面的注解操作符都能用Nullable来变成可选绑定，如果不能找到目标资源也不会异常
    @Nullable
    @BindView(R.id.et_edit_text)
    EditText et_edit_text;
    //绑定多个控件id,可以使用buttonList.get(i).setText("button1),i来确定是哪一个
    @BindViews({R.id.bt_button1, R.id.bt_button2, R.id.bt_button3})
    List<Button> buttonList;
    //绑定资源String
    @BindString(R.string.app_name)
    String appName;
    //绑定资源数组
    @BindArray(R.array.swordsman)
    String[] swordsman;
    //绑定资源dimen
    @BindDimen(R.dimen.activity_horizontal_margin)
    float margin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife);
        //第一步就要绑定，不然不能用
        ButterKnife.bind(this);
//        editText软键盘遮挡问题，本来默认是智能判断不会让软键盘遮挡输入框的，但有时候状态栏的工具框架里面会改变输入法适应的方式，所以设置完框架使用后
    // 设置输入法适应的方式，这样就避免软键盘遮挡了
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //设置EditText最大字符长度
        InputFilter[] filters = {new InputFilter.LengthFilter(15)};
        et_edit_text.setFilters(filters);
        //更改右下角的显示字和更改默认行为模式
        et_edit_text.setImeOptions(EditorInfo.IME_ACTION_SEND);
        //为右下角的软键盘设置监听
        et_edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //如果只有一个设置监听的话，这句不用判断也没关系
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    return true;
                }
                return false;
            }
        });
        //输入模式为字符，可以数字可以英文可以符号
//        et_edit_text.setInputType(InputType.TYPE_CLASS_TEXT);
        //输入模式跟上面一样，但是设为密码模式，显示为******
        et_edit_text.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        tv_text.setText("BindView");
        buttonList.get(0).setText(appName);
        buttonList.get(1).setText("button2" + swordsman[0]);
        buttonList.get(2).setText("button3" + margin);
    }

    //绑定点击事件
    @OnClick(R.id.bt_button1)
    public void showToast() {
        Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
    }

    //绑定长按事件
    @OnLongClick({R.id.bt_button2, R.id.bt_button3})
    public boolean onViewLongClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_button2:
                Toast.makeText(this, "我长按了button2", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.bt_button3:
                Toast.makeText(this, "我长按了button3", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;

        }
    }

    //这里监听代替et_edit_text.addTextChangedListener(new TextWatcher())方法，而且可以只实现一种方法,显得更简洁
    //字改变之前
//    @OnTextChanged(value = R.id.et_edit_text, callback = OnTextChanged.Callback.BEFORE_TEXT_CHANGED)
//    void beforeTextChanged(CharSequence s, int start, int count, int after) {
//    }
    //字改变时
    @OnTextChanged(value = R.id.et_edit_text, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onTextChanged(CharSequence s, int start, int before, int count) {
        Toast.makeText(this, "字改变了", Toast.LENGTH_SHORT).show();
    }
    //字改变之后
//    @OnTextChanged(value = R.id.et_edit_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
//    void afterTextChanged(Editable s) {
//    }

    //监听某View的onTouch时间
    @OnTouch(R.id.bt_button3)
    public boolean onTouch(View view, MotionEvent event) {
        return true;
    }

    //可以监听某个列表的子项点击
    @OnItemClick(R.id.lv_list)
    void onItemClick(int position) {
        Toast.makeText(this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
    }
}
