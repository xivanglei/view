package xl.gcs.com.designsupportlibrary;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xianglei on 2018/4/15.
 * TextInputLayout里面只能含一个子控件，这个控件必须是EditText
 * colorAccent 可以改变TextInputLayout上方的提示颜色更改,但错误提示还是红色
 */

public class TextInputLayoutActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private TextInputLayout tl_username;
    private TextInputLayout tl_password;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);  //创建正则
    private Matcher matcher;                                    //判断正则
    private Button bt_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_input_layout);
        tl_username= (TextInputLayout) this.findViewById(R.id.tl_username);
        tl_password= (TextInputLayout) this.findViewById(R.id.tl_password);
        bt_login= (Button) this.findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String username=tl_username.getEditText().getText().toString();
        String password=tl_password.getEditText().getText().toString();
        boolean boolUsername = validateUserName(username);
        boolean boolPassword = validatePassword(password);
        if(!boolUsername) {
            //这里测试，必须先设置错误信息，再设置是否显示，否则始终会显示，不管是不是false,书中有错误
            tl_username.setError("请输入正确的邮箱地址");
            //显示错误信息
            tl_username.setErrorEnabled(true);
        } else {
            //如果正确就不显示错误信息了
            tl_username.setErrorEnabled(false);
        }
        //这段同上
        if(!boolPassword) {
            tl_password.setError("密码字数过少");
            tl_password.setErrorEnabled(true);
        } else {
            tl_password.setErrorEnabled(false);
        }
        if(boolUsername && boolPassword){
            tl_username.setErrorEnabled(false);
            tl_password.setErrorEnabled(false);
            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
        }
    }

    //对代码进行字符数验证
    private boolean validatePassword(String password){
        return password.length() > 6;
    }

    //对用户名进行正则判断
    private boolean validateUserName(String username){
        matcher = pattern.matcher(username);
        return matcher.matches();
    }




}

