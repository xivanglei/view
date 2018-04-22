package xl.gcs.com.wheelview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.OnClick;

/*
implementation 'com.contrarywind:wheelview:4.0.5'
    implementation 'com.jakewharton:butterknife:8.5.1'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.5.1'
    implementation 'com.google.code.gson:gson:2.8.2'
 */

public class MainActivity extends AppCompatActivity {

    SelectCityDialog mSelectCityDialog;
    SelectTimeDialog mSelectTimeDialog;
    SelectNumberDialog mSelectNumberDialog;
    DateChooseDialog mDateChooseDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSelectCityDialog = new SelectCityDialog(MainActivity.this);
        mSelectCityDialog.setOnCommitClickListener(new SelectCityDialog.OnCommitClickListener() {
            @Override
            public void onCommitClick(String province, String city, String county) {
                Toast.makeText(MainActivity.this, province + city + county, Toast.LENGTH_SHORT).show();
            }
        });
        mSelectTimeDialog = new SelectTimeDialog(this);
        mSelectTimeDialog.setOnCommitClickListener(new SelectTimeDialog.OnCommitClickListener() {
            @Override
            public void onCommitClick(long time) {
                String strTime = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(time);
                Toast.makeText(MainActivity.this, strTime, Toast.LENGTH_SHORT).show();
            }
        });
        mSelectNumberDialog = new SelectNumberDialog(this, 3, 100);
        mSelectNumberDialog.setOnCommitClickListener(new SelectNumberDialog.OnCommitClickListener() {
            @Override
            public void onCommitClick(String number) {
                Toast.makeText(MainActivity.this, number, Toast.LENGTH_SHORT).show();
            }
        });
        mDateChooseDialog = new DateChooseDialog(this);
        mDateChooseDialog.setOnCommitClickListener(new DateChooseDialog.OnCommitClickListener() {
            @Override
            public void onCommitClick(long time) {
                String strTime = new SimpleDateFormat("yyyy年MM月dd日").format(time);
                Toast.makeText(MainActivity.this, strTime, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick({R.id.city, R.id.date, R.id.time, R.id.simple})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.city:
                DialogUtil.showDialogBottom(mSelectCityDialog);
                break;
            case R.id.date:
                DialogUtil.showDialogBottom(mDateChooseDialog);
                break;
            case R.id.time:
                DialogUtil.showDialogBottom(mSelectTimeDialog);
                break;
            case R.id.simple:
                DialogUtil.showDialogBottom(mSelectNumberDialog);
                break;
            default:
                break;

        }
    }
}
