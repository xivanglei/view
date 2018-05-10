package xl.gcs.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xl.gcs.com.httpclientandhttpurlconnection.HttpClientAndHttpURLConnectionActivity;
import xl.gcs.com.okhttp.OkHttpActivity;
import xl.gcs.com.retrofit.RetrofitActivity;
import xl.gcs.com.volley.VolleyActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bt_client_urlconnection)
    Button bt_client_urlconnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_client_urlconnection, R.id.bt_volley, R.id.bt_okhttp, R.id.bt_retrofit})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_client_urlconnection:
                startActivity(new Intent(this, HttpClientAndHttpURLConnectionActivity.class));
                break;
            case R.id.bt_volley:
                startActivity(new Intent(this, VolleyActivity.class));
                break;
            case R.id.bt_okhttp:
                startActivity(new Intent(this, OkHttpActivity.class));
                break;
            case R.id.bt_retrofit:
                startActivity(new Intent(this, RetrofitActivity.class));
                break;
            default:
                break;
        }
    }
}
