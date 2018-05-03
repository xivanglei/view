package xl.gcs.com.viewpager.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import butterknife.ButterKnife;

/**
 * Created by xianglei on 2018/3/27.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutById());
        ButterKnife.bind(this);
        initView();
        initData(savedInstanceState);
        initEvent();
    }

    protected void initView(){}



    protected abstract int getLayoutById();

    protected abstract void initData(Bundle savedInstanceState);
    protected void startActivity(Class<? extends Activity> activity) {
        startActivity(new Intent(this, activity));
    }

    protected void initEvent() {
    }


}

