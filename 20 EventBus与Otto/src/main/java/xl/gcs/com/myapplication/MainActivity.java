package xl.gcs.com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xl.gcs.com.myapplication.eventbus.EventBusActivity;
import xl.gcs.com.myapplication.otto.OttoActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_event_bus, R.id.bt_otto})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_event_bus:
                startActivity(new Intent(this, EventBusActivity.class));
                break;
            case R.id.bt_otto:
                startActivity(new Intent(this, OttoActivity.class));
                break;
        }
    }
}
