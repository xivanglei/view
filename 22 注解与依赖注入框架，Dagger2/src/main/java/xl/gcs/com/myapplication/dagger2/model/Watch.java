package xl.gcs.com.myapplication.dagger2.model;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by xianglei on 2018/6/1.
 */

public class Watch {
    //Inject 注解是JSR-330标准中的一部分，用于标记需要注入的依赖。再这里标记Watch构造方法，表明Dagger2可以用Watch构造方法构建对象。
    //下面需要定义一个接口，建议类名为 调用方目标类名+Component
    @Inject
    public Watch() {
    }
    public void work() {
        Log.d("Dagger2", "手表工作");
    }
}
