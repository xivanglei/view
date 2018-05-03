package xl.gcs.com.permissionsdispatcher;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by xianglei on 2018/4/15.
 * 第三方开源库 implementation 'com.github.hotchemi:permissionsdispatcher:2.3.1'
 annotationProcessor 'com.github.hotchemi:permissionsdispatcher-processor:2.3.1'

 还要添加权限<uses-permission android:name="android.permission.CALL_PHONE" />
 */

//第一步关键注释，必须注释。注册这个activity或fragment，不然用不了
@RuntimePermissions
public class ThirdPartyActivity extends AppCompatActivity {
    private Button bt_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party);
        bt_call = (Button) this.findViewById(R.id.bt_call);
        bt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //前面注册过了，点Build的makeProject就会生成类（注册类名 + PermissionsDispatcher,看下面的类）
                //调用方法在下面 格式是 （方法名（随意） + WithCheck）括号里参数是注册的类，也就是弹出权限处理的类
                ThirdPartyActivityPermissionsDispatcher.callWithCheck(ThirdPartyActivity.this);
            }
        });
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
//在需要获取权限的地方注释，方法名随意
    void call() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10086");
        intent.setData(data);
        try {
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @OnShowRationale(Manifest.permission.CALL_PHONE)
            //方法名随意
//提示用户为何要开启此权限，第二次权限处理，这时已经带有不再询问对话框了，所以提示对方，这权限意味什么，别轻易取消
    void showWhy(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("提示用户为何要开启此权限")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //再次执行权限请求，一定要放，否则对方点知道了，就是不再弹出同意权限对话框，
                        // 要么就不要这个注解，不要这个方法，也会每次提醒的。但不能点不再询问后拒绝
                        request.proceed();
                    }
                })
                .show();
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
//用户选择拒绝时的提示，方法名随意
    void showDenied() {
        Toast.makeText(this, "用户选择拒绝时的提示", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CALL_PHONE)
//用户选择不再询问后的提示，点了不再询问后，提醒对方拒绝的严重性。方法名随意
    void showNotAsk() {
        new AlertDialog.Builder(this)
                .setMessage("该功能需要访问电话的权限，不开启将无法正常工作！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    //这里是标准操作，下面类名格式，上面有介绍
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ThirdPartyActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}

