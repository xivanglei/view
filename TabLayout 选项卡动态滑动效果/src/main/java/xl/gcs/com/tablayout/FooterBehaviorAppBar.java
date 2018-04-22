package xl.gcs.com.tablayout;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/9/26 0026.
 *继承CoordinatorLayout.Behavior<View>
 *
 */

public class FooterBehaviorAppBar extends CoordinatorLayout.Behavior<View> {

    public FooterBehaviorAppBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //得到一共移动了多少
        float translationY = Math.abs(dependency.getY());
        Log.i("wangshu",translationY+"");
        //把移动的举例给要滑进滑出的控件，超过也不会清零累计计算，直到重新划回来到原位，才会变回0，相当于滑动的控件就回来了
        child.setTranslationY(translationY);
        return true;
    }
}