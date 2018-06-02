package xl.gcs.com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xl.gcs.com.annotations.cls.BindView;
import xl.gcs.com.myapplication.butterknife.ButterKnifeActivity;
import xl.gcs.com.myapplication.dagger2.Dagger2Activity;

public class MainActivity extends AppCompatActivity {

    @BindView(value = R.id.tv_text)
    TextView tv_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_annotation, R.id.bt_butter_knife, R.id.bt_dagger2})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_annotation:
                Toast.makeText(this, "MainActivity中的xl.gcs.com.annotations.cls.BindView，与@BindView(value = R.id.tv_text)\n" +
                        "    TextView tv_text;就是使用编译时注解，具体还要看processor模块和annotations模块", Toast.LENGTH_SHORT).show();
                break;
                //ButterKnife从严格意义讲不算是依赖注入框架，它只是专注于Android系统的View注入框架，并不支持其他方面的注入
            case R.id.bt_butter_knife:
                startActivity(new Intent(this, ButterKnifeActivity.class));
                break;
            case R.id.bt_dagger2:
                startActivity(new Intent(this, Dagger2Activity.class));
                break;
        }
    }
}
