package xl.gcs.com.view.chart;

/**
 * Created by xianglei on 2018/5/14.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;


public class ScreenUtils {
    private static int screenW;
    private static int screenH;
    private static float screenDensity;
    private static float screenDensityDpi;

    public static float getScreenDensityDpi(Context context) {
        if(screenDensityDpi == 0) {
            initScreen(context);
        }
        return screenDensityDpi;
    }

    public static int getScreenW(Context context) {
        if (screenW == 0) {
            initScreen(context);
        }
        return screenW;
    }

    public static int getScreenH(Context context) {
        if (screenH == 0) {
            initScreen(context);
        }
        return screenH;
    }

    public static float getScreenDensity(Context context) {
        if (screenDensity == 0) {
            initScreen(context);
        }
        return screenDensity;
    }

    private static void initScreen(Context context) {
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        if (screenW > screenH) {
            int t = screenH;
            screenH = screenW;
            screenW = t;
        }
        //一般的屏幕分辨率为1024x600，density为1.5，densityDpi为240
        screenDensity = metric.density;         //屏幕密度，和像素px无关
        screenDensityDpi = metric.densityDpi;   //没英寸距离具有多少个像素点
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) (dpValue * getScreenDensity(context) + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / getScreenDensity(context) + 0.5f);
    }

    /**
     * 计算状态栏高度
     */
    public static int getStatusBarHeight(Activity ac) {
        Rect frame = new Rect();
        ac.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }
}

