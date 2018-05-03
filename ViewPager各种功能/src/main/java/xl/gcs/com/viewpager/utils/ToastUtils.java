package xl.gcs.com.viewpager.utils;

import android.widget.Toast;

import xl.gcs.com.viewpager.app.App;


/**
 * Created by admin on 2018/3/12.
 */

public class ToastUtils {
    private static Toast mToast;

    public synchronized static void show(String content) {
        if (mToast == null) {
            mToast = Toast.makeText(App.getInstance(), content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }
}
