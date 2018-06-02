package xl.gcs.com.annotations.runtime;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/12/12 0012.
 * 注解处理器，这里是运行时注解处理器
 * 编译时注解处理器看processor(Java Library)里的ClassProcessor,Java Library模块创建方法就是上面菜单栏中File-New-New Module,之后选择Java Library,命名后出来和app文件夹同级的文件夹
 */

public class AnnotationProcessor {
    public static void main(String[] args) {
        //getDeclaredMethod() 获取的是类自身声明的所有方法，包含public、protected和private方法。
        //getMethod () 获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法。
        Method[] methods = AnnotationTest.class.getDeclaredMethods();
        for (Method m:methods){
            //返回指定类型的注解对象，这里是GET对象
            GET get= m.getAnnotation(GET.class);
            //这里打印GET的value值
            System.out.println(get.value());
        }
    }
}
