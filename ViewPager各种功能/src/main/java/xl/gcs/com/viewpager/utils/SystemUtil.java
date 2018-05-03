package xl.gcs.com.viewpager.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.widget.Toast;

import xl.gcs.com.viewpager.app.App;


/**
 * Created by ASUS on 2017/8/26.
 */
public class SystemUtil {
    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() == null) {
            Toast.makeText(App.getInstance().getApplicationContext(), "请检查网络状态", Toast.LENGTH_SHORT).show();
        }

        return connectivityManager.getActiveNetworkInfo() != null;
    }

    /**
     * 获取版本名
     */
    public static String getVersionName() {
        PackageManager packageManager = App.getInstance().getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(App.getInstance().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
    /**
     * 获取版本名
     */
    public static int getVersionCode() {
        PackageManager packageManager = App.getInstance().getPackageManager();
        PackageInfo packageInfo;
        int versionCode = 0;
        try {
            packageInfo = packageManager.getPackageInfo(App.getInstance().getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    /**
     * 验证手机号码
     *
     * @param cellphone
     * @return
     */
    public static boolean checkphone(String cellphone) {
        String regex = "\\d{11}$";
        return cellphone.matches(regex);
    }

}
