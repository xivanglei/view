package xl.gcs.com.agent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by xianglei on 2018/5/13.
 * 动态代理类
 */

//每一个动态代理类都必须要实现InvocationHandler这个接口，并且每个代理类的实例都关联到了一个handler，
// 当我们通过代理对象调用一个方法的时候，这个方法的调用就会被转发为由InvocationHandler这个接口的 invoke 方法来进行调用。
public class DynamicPurchasing implements InvocationHandler {

    private Object obj;

    public DynamicPurchasing(Object obj) {
        this.obj = obj;
    }

    //proxy:指代我们所代理的那个真实对象, method:指代的是我们所要调用真实对象的某个方法的Method对象, args:指代的是调用真实对象某个方法时接受的参数
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //反射用法，相当于对obj类实行method方法，args是参数
        Object result = method.invoke(obj, args);
        if(method.getName().equals("buy")) {
            System.out.println("Liuwangshu在买买买");
        }
        return result;
    }
}
