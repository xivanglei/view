package xl.gcs.com.myapplication.app;

import android.app.Application;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

/**
 * Created by xianglei on 2018/6/2.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化OkHttpFinal
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
    }
}
