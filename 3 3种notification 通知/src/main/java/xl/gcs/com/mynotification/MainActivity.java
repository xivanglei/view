package xl.gcs.com.mynotification;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;


/*
需要把最小api 设为21

PendingIntent最后一个参数
FLAG_CANCEL_CURRENT:如果当前系统中已经存在一个相同的 PendingIntent 对象，那么就将先将已有的 PendingIntent 取消，然后重新生成一个 PendingIntent 对象。
FLAG_IMMUTABLE:创建的PendingIntent不可变，API23加入。
FLAG_NO_CREATE:如果当前系统中不存在相同的 PendingIntent 对象，系统将不会创建该 PendingIntent 对象而是直接返回 null 。
FLAG_ONE_SHOT:该 PendingIntent 只作用一次。
FLAG_UPDATE_CURRENT:如果系统中已存在该 PendingIntent 对象，那么系统将保留该 PendingIntent 对象，但是会使用新的 Intent 来更新之前 PendingIntent 中的 Intent 对象数据，例如更新 Intent 中的 Extras。

 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_nomal;
    private TextView tv_fold;
    private TextView tv_hang;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private MyRadioGroup radioGroup;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_nomal = (TextView) findViewById(R.id.tv_nomal);
        tv_fold = (TextView) findViewById(R.id.tv_fold);
        tv_hang = (TextView) findViewById(R.id.tv_hang);
        radioButton1 = (RadioButton) findViewById(R.id.rb_public);
        radioButton2 = (RadioButton) findViewById(R.id.rb_private);
        radioButton2 = (RadioButton) findViewById(R.id.rb_secret);
        radioGroup = (MyRadioGroup) findViewById(R.id.rg_all);
        //设置选择的监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_public:
                        Toast.makeText(MainActivity.this, "public", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_private:
                        Toast.makeText(MainActivity.this, "private", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_secret:
                        Toast.makeText(MainActivity.this, "secret", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

        tv_nomal.setOnClickListener(this);
        tv_fold.setOnClickListener(this);
        tv_hang.setOnClickListener(this);

        //通知管理器
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

//    VISIBILITY_PUBLIC：任何情况都会显示通知
//    VISIBILITY_PRIVATE：只有在没有锁屏时会显示通知
//    VISIBILITY_SECRET：在pin、password等安全锁和没有锁屏的情况下才能够显示
    private void selectNotificationLevel(Notification.Builder builder) {
        //检查radioGroup选中的id是什么
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_public:
                builder.setVisibility(Notification.VISIBILITY_PUBLIC);
                builder.setContentText("public");
                break;
            case R.id.rb_private:
                builder.setVisibility(Notification.VISIBILITY_PRIVATE);
                builder.setContentText("private");
                break;
            case R.id.rb_secret:
                builder.setVisibility(Notification.VISIBILITY_SECRET);
                builder.setContentText("secret");
                break;
            default:
                builder.setVisibility(Notification.VISIBILITY_PUBLIC);
                builder.setContentText("public");
                break;

        }
    }

    //普通通知
    private void sendNormalNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net/itachi85/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);
        builder.setContentIntent(pendingIntent);
        //小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //大图标，有些情况大图标不能不设置否则出错，但有些手机又不能显示大图标
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        //是否能取消
        builder.setAutoCancel(true);
        //设置标题
        builder.setContentTitle("普通通知");
        //设置显示等级
        selectNotificationLevel(builder);
        notificationManager.notify(0, builder.build());

    }

    //折叠式通知
    private void sendFoldNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net/itachi85/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.lanucher));
        builder.setAutoCancel(true);
        builder.setContentTitle("折叠式通知");
        selectNotificationLevel(builder);
        //比普通通知就多了两步，第一步，用RemoteViews来创建自定义Notification视图，视图只要普通写法写在UI布局里就行了。
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.view_fold);
        Notification notification = builder.build();
        //第二步，指定展开时的视图
        notification.bigContentView = remoteViews;
        //发送时是普通视图，可以往下拉，变成自定义的展开视图
        //也可以把普通状态视图也设为自定义视图 notification.contentView = remoteViews; 一发送就显示出自定义视图了
        notificationManager.notify(1, notification);
    }

    private void sendHangNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net/itachi85/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.foldleft);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.lanucher));
        builder.setAutoCancel(true);
        builder.setContentTitle("悬挂式通知");
        selectNotificationLevel(builder);
        //设置点击跳转
        Intent hangIntent = new Intent();
        /*
        一. intent.setFlags()方法中的参数值含义：
        1.FLAG_ACTIVITY_CLEAR_TOP：例如现在的栈情况为：A B C D 。D此时通过intent跳转到B，
        如果这个intent添加FLAG_ACTIVITY_CLEAR_TOP标记，则栈情况变为：A B。如果没有添加这个标记，
        则栈情况将会变成：A B C D B。也就是说，如果添加了FLAG_ACTIVITY_CLEAR_TOP标记，并且目标Activity在栈中已经存在，
        则将会把位于该目标activity之上的activity从栈中弹出销毁。这跟上面把B的Launch mode设置成singleTask类似。简而言之，
        跳转到的activity若已在栈中存在，则将其上的activity都销掉。
        2.FLAG_ACTIVITY_NEW_TASK：例如现在栈1的情况是：A B C。C通过intent跳转到D，并且这个intent添加了FLAG_ACTIVITY_NEW_TASK标记，
        如果D这个Activity在Manifest.xml中的声明中添加了Task affinity，系统首先会查找有没有和D的Task affinity相同的task栈存在，
        如果有存在，将D压入那个栈，如果不存在则会新建一个D的affinity的栈将其压入。如果D的Task affinity默认没有设置，则会把其压入栈1，
        变成：A B C D，这样就和不加FLAG_ACTIVITY_NEW_TASK标记效果是一样的了。注意如果试图从非activity的非正常途径启动一个
        activity（例见下文“intent.setFlags()方法中参数的用例”），比如从一个service中启动一个activity，
        则intent比如要添加FLAG_ACTIVITY_NEW_TASK标记（编者按：activity要存在于activity的栈中，
        而非activity的途径启动activity时必然不存在一个activity的栈，所以要新起一个栈装入启动的activity）。
        简而言之，跳转到的activity根据情况，可能压在一个新建的栈中。
        3.FLAG_ACTIVITY_NO_HISTORY：例如现在栈情况为：A B C。C通过intent跳转到D，这个intent添加FLAG_ACTIVITY_NO_HISTORY标志，
        则此时界面显示D的内容，但是它并不会压入栈中。如果按返回键，返回到C，栈的情况还是：A B C。如果此时D中又跳转到E，
        栈的情况变为：A B C E，此时按返回键会回到C，因为D根本就没有被压入栈中。简而言之，跳转到的activity不压在栈中。
        4.FLAG_ACTIVITY_SINGLE_TOP：和Activity的Launch mode的singleTop类似。如果某个intent添加了这个标志，
        并且这个intent的目标activity就是栈顶的activity，那么将不会新建一个实例压入栈中。简而言之，目标activity已在栈顶则跳转过去，
        不在栈顶则在栈顶新建activity。
        二.intent.setFlags()方法中参数的用例：
        很多人使用startActivity时候，会碰到如下的异常：
        Caused by: android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
        都知道，Context中有一个startActivity方法，Activity继承自Context，重载了startActivity方法。如果使用Activity的startActivity方法，不会有任何限制，而如果使用Context的startActivity方法的话，就需要开启一个新的task（编者按：参见一.2.的编者按），遇到上面那个异常的，都是因为使用了Context的startActivity方法。解决办法是：Java代码中加一个flag，即intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)。这样就可以在新的task里面启动这个Activity了。
         */
        hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        hangIntent.setClass(this, MainActivity.class);
        //如果描述的PendingIntent已经存在，则在产生新的Intent之前会先取消掉当前的。
        // 测试过，上面设置过builder.setContentIntent(pendingIntent);就用上面的，如果没有设置过，就用下面这个。我的理解，这里的 pendingIntent 跟上面的pendingIntent不冲突，
        // 书上说的设置过或没设置过相同的，指的是同时用一种方法设置两次,比如再多设置一次builder.setFullScreenIntent(hangPendingIntent, true);
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setFullScreenIntent(hangPendingIntent, true);

        notificationManager.notify(2, builder.build());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_nomal:
                sendNormalNotification();
                break;
            case R.id.tv_fold:
                sendFoldNotification();
                break;

            case R.id.tv_hang:
                sendHangNotification();
                break;
        }
    }
}

