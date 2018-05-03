package xl.gcs.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ACache mACache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取实例
        mACache = ACache.get(this);
        //放入数据 key+value+缓存时间
        mACache.put("dddsssss", "星期5", 60 * 60 * 24 * 3);
        //通过key读取字符串
        String aaa = mACache.getAsString("ddd");
        TextView textView = ((TextView) findViewById(R.id.hello_world));
        textView.setText("_" + ACache.TIME_HOUR);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                finish();
            }
        });

    }
}
