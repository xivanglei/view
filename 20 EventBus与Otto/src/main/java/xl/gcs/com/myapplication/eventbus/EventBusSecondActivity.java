package xl.gcs.com.myapplication.eventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import xl.gcs.com.myapplication.R;


public class EventBusSecondActivity extends AppCompatActivity {
    private Button bt_message;
    private TextView tv_message;
    private Button bt_subscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus);
        tv_message=(TextView)this.findViewById(R.id.tv_message);
        tv_message.setText("EventBusSecondActivity");
        bt_subscription=(Button)this.findViewById(R.id.bt_subscription);
        bt_subscription.setText("发送粘性事件");
        bt_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送黏性时间，就是在发送之后再订阅，也能收到，什么时候注册，什么时候收到消息
                EventBus.getDefault().postSticky(new MessageEvent("粘性事件", new Message("祥雷", "男", 31)));
                finish();
            }
        });
        bt_message=(Button)this.findViewById(R.id.bt_message);
        bt_message.setText("发送事件");
        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这是普通事件，已经注册订阅方才能收到消息，发送方不需要注册就能发送
                EventBus.getDefault().post(new MessageEvent<>("欢迎关注刘望舒的博客", new Message("小红", "女", 31)));
                finish();
            }
        });

    }
}
