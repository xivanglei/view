package xl.gcs.com.cardview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.SeekBar;
import android.widget.Toast;


/*
卡片布局需要依赖 implementation 'com.android.support:cardview-v7:26.1.0'
 */
public class MainActivity extends AppCompatActivity {

    private CardView mCardView;
    private SeekBar sb1, sb2, sb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        initEvent();
    }

    private void bindView() {
        mCardView = (CardView) findViewById(R.id.tv_item);
        sb1 = (SeekBar) findViewById(R.id.sb_1);
        sb2 = (SeekBar) findViewById(R.id.sb_2);
        sb3 = (SeekBar) findViewById(R.id.sb_3);
    }

    private void initEvent() {
        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {      //进度默认是0-100，这里不是讲解进度条，顺便提一提
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCardView.setRadius(progress);          //圆角大小
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCardView.setCardElevation(progress);   //阴影大小
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        sb3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCardView.setContentPadding(progress, progress, progress, progress);  //内间距
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}
