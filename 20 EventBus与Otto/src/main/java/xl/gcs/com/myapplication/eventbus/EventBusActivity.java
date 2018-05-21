package xl.gcs.com.myapplication.eventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import xl.gcs.com.myapplication.R;


public class EventBusActivity extends AppCompatActivity {
    private TextView tv_message;
    private Button bt_message;
    private Button bt_subscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus);
        tv_message=(TextView)this.findViewById(R.id.tv_message);
        tv_message.setText("EventBusActivity");
        bt_subscription=(Button)this.findViewById(R.id.bt_subscription);
        bt_subscription.setText("注册事件");
        bt_message=(Button)this.findViewById(R.id.bt_message);
        bt_message.setText("跳转到SecondActivity");
        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(EventBusActivity.this,EventBusSecondActivity.class));
            }
        });
        bt_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!EventBus.getDefault().isRegistered(EventBusActivity.this)) {
                    //注册事件，可以使用new，但一般使用getDefault就可以了
                    EventBus.getDefault().register(EventBusActivity.this);
                }else{
                    Toast.makeText(EventBusActivity.this,"请勿重复注册事件",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }

//    1 POSTING，默认就是这项，事件在哪个线程发布出来的，onEvent就会在这个线程中运行
//    2 MAIN，不论事件是在哪个线程中发布出来的都会在UI线程中执行，接收事件就会在UI线程中运行
//    3 BACKGROUND，那么如果事件是在UI线程中发布出来的，那么就会在子线程中运行，如果事件本来就是子线程中发布出来的，那么函数直接在该子线程中执行
//    4 ASYNC使用这个函数作为订阅函数，那么无论事件在哪个线程发布，都会创建新的子线程在执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent<Message> messageEvent){
        tv_message.setText(messageEvent.getMessage() + messageEvent.getT().getName());
    }
    //sticky=true, true表示粘性事件，可以接收postSticky事件,先发送消息，什么时候注册什么时候接收
//    我们如果不再需要该粘性事件我们可以移除,EventBus.getDefault().removeStickyEvent(new DataSynEvent());
//    或者调用移除所有粘性事件,EventBus.getDefault().removeAllStickyEvents();
//    priority是优先级，越高的越优先，像广播一样，高的也能截断低的，EventBus.getDefault().cancelEventDelivery(event)
    @Subscribe(sticky = true, priority = 100)
    public void ononMoonStickyEvent(MessageEvent<Message> messageEvent){
        tv_message.setText(messageEvent.getMessage() + messageEvent.getT().getName());
    }
}
