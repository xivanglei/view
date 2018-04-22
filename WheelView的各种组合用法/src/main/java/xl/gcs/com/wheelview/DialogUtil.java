package xl.gcs.com.wheelview;

import android.app.Dialog;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Created by ASUS on 2017/9/1.
=======
/**
 * Created by xianglei on 2018/3/19.
>>>>>>> origin/master
 */

public class DialogUtil {
    public static void showDialogBottom(Dialog dialog) {
        if (dialog != null && !dialog.isShowing()) {
            int statusBarHeight1 = -1;
            //获取status_bar_height资源的ID
            int resourceId = App.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight1 = App.getInstance().getResources().getDimensionPixelSize(resourceId);
            }
            dialog.show();
            WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
            attributes.width = App.SCREEN_WIDTH;
//            attributes.height = MyApplication.SCREEN_HEIGHT-statusBarHeight1;
            attributes.gravity = Gravity.BOTTOM;
            dialog.getWindow().setAttributes(attributes);
        }
    }
//    public static void showDialogBottom2(Dialog dialog) {
//        if (dialog != null && !dialog.isShowing()) {
//            dialog.show();
//            WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
//            attributes.width = App.SCREEN_WIDTH;
//            attributes.height = App.SCREEN_HEIGHT-180;
//            attributes.gravity = Gravity.BOTTOM;
//            dialog.getWindow().setAttributes(attributes);
//        }
//    }
//    public static void showDialogCenter(Dialog dialog) {
//        dialog.show();
//        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
//        attributes.width = App.SCREEN_WIDTH;
//        attributes.height = App.SCREEN_HEIGHT;
//        attributes.gravity = Gravity.CENTER;
//        dialog.getWindow().setAttributes(attributes);
//    }
}
