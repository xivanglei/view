package xl.gcs.com.rxjava.bus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import xl.gcs.com.rxjava.R;


public class RxBusActivity extends AppCompatActivity {
    private Button bt_post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_bus);
        bt_post= (Button) this.findViewById(R.id.bt_post);
        bt_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过RxBus里的subject发送事件,由于是单例，所以是同一个subject
                RxBus.getInstance().post(new MessageEvent("用RxJava实现RxBus"));
            }
        });
    }
}
