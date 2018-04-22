package xl.gcs.com.designsupportlibrary;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


/*
implementation 'com.android.support:design:26.1.0'
        布局里面有FloatingActionButton的基本布局
        app:backgroundTint="#3F51B5"        //设置背景颜色，默认是主题的colorAccent颜色
        app:elevation="3dp"                 //正常状态的阴影大小
        app:pressedTranslationZ="6dp"       //点击状态的阴影大小
        android:clickable="true"            //默认是不可点击，所以要加上这句
 */
public class MainActivity extends AppCompatActivity {
    private Button bt_snackbar;
    private Button bt_textInputLayout;
    private RelativeLayout activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity_main = (RelativeLayout) this.findViewById(R.id.activity_main);
        bt_textInputLayout= (Button) this.findViewById(R.id.bt_textInputLayout);
        bt_snackbar = (Button) this.findViewById(R.id.bt_snackbar);
        bt_textInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TextInputLayoutActivity.class);
                startActivity(intent);
            }
        });

        bt_snackbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackbar();
            }
        });
    }

    private void showSnackbar() {
        //第一个参数必须是Snackbar的父控件，表示Snackbar将显示在哪个控件上，参数2是文字，参数3是显示时间
        //
        Snackbar.make(activity_main, "标题", Snackbar.LENGTH_LONG)
                //点击事件可以设置也可以不设
                .setAction("点击事件", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(
                                MainActivity.this,
                                "Toast",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                //setDuration有点重复了，因为上面第三个参数就是设置显示时间的，但这里设置了，就以这里为准，删掉就按上面的显示时间
                //也可以输入数字 比如 500  单位是毫秒
                .setDuration(Snackbar.LENGTH_SHORT).show();
    }
}
