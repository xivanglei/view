package xl.gcs.com.myapplication.otto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.otto.Produce;

import xl.gcs.com.myapplication.R;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class OttoSecondActivity extends AppCompatActivity {
    private Button bt_jump;
    private OttoBus bus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otto);
        bt_jump= (Button) this.findViewById(R.id.bt_jump);
        bt_jump.setText("发送事件");
        bt_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用post来发送事件，发送方不需要注册就能发送
                OttoBus.getInstance().post(new BusData("刘望舒的博客更新了"));
                finish();
            }
        });
        bus=OttoBus.getInstance();
        bus.register(this);
    }

    //用Produce来发布事件，本来发送方不需要注册的，但使用这种方法需要注册和取消注册，并且在注册的同时就已经发送出消息了，
    // 这里都不需要调用setInitialContent方法，而一发出消息那边就已经订阅事件并处理了
    @Produce
    public BusData setInitialContent() {
        Log.i("wangshu","Produce");
        return new BusData("刘望舒的博客更新了");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
