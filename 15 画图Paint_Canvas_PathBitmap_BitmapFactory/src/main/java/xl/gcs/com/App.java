package xl.gcs.com;

import android.app.Application;



/**
 * Created by admin on 2018/3/12.
 */

public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
