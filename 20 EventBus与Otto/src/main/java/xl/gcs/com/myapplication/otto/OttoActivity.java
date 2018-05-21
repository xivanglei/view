package xl.gcs.com.myapplication.otto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import xl.gcs.com.myapplication.R;

public class OttoActivity extends AppCompatActivity {
    private Button bt_jump;
    private TextView tv_message;
    private Bus bus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otto);
        tv_message= (TextView) this.findViewById(R.id.tv_message);
        bt_jump= (Button) this.findViewById(R.id.bt_jump);
        bt_jump.setText("跳转到SecondActivity");
        bt_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OttoActivity.this,OttoSecondActivity.class));
            }
        });
        //获取Bus类，OttoBus是继承Bus类，没改什么东西，只是封装成单例类，免得每次都要new出来
        bus=OttoBus.getInstance();
        //注册
        bus.register(this);
    }
    //用@Subscribe注解来订阅事件，这里不需要指定Main，也能更新UI,可能内部有判断
    @Subscribe
    public void setContent(BusData data) {
        Log.i("wangshu","Subscribe");
        tv_message.setText(data.getMessage());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //同样要取消注册
        bus.unregister(this);
    }
}
