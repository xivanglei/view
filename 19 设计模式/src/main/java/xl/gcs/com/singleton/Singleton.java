package xl.gcs.com.singleton;

/**
 * Created by xianglei on 2018/5/12.
 * 单例模式有6种
 */

public class Singleton {
    //饿汉模式，加载时就完成了初始化，加载慢，但回去会快，避免了多线程的同步问题。
//    private static Singleton instance = new Singleton();
    //把无参构造private掉，也不提供有参构造，就不能使用new来实例化了，所以每次调用的都是instance，就是单例
//    private Singleton() {
//    }
//    public static Singleton getInstance() {
//        return instance;
//    }


//    //懒汉模式（线程不安全），第一次调用时初始化
//    private static Singleton instance;
//    private Singleton() {
//    }
//    public static Singleton getInstance() {
//        if(instance == null) {
//            instance = new Singleton();
//        }
//        return instance;
//    }

//    //懒汉模式（线程安全），这写法在多线程中很好的工作，但每次调用getInstance时都需要进行同步，会造成不必要的同步开销，大部分时间用不到同步，所以，不建议用这种模式。
//    private static Singleton instance;
//    private Singleton() {
//    }
//    public static synchronized Singleton getInstance() {
//        if(instance == null) {
//            instance = new Singleton();
//        }
//        return instance;
//    }

    //双重检查模式（DCL），某些情况下还是会出现失效的问题，也就是DCL失效，建议用静态内部类单例模式来代替DCL
    //只要声明了volatile, 编译器和虚拟机就知道该域是可能被另一个线程并发更新的
//    private static volatile Singleton instance;
//    private Singleton() {
//    }
//    public static Singleton getInstance() {
//        if(instance == null) {
//            //先检查下有没有被另一个线程更新
//            synchronized (Singleton.class) {
//                //如果还是空，就new一个
//                if(instance == null) {
//                    instance = new Singleton();
//                }
//            }
//        }
//        return instance;
//    }

    //静态内部类单例模式,第一次加载不会初始化，第一次调用getInstance时虚拟机加载SingletonHolder并初始化sInstance,这样能保证唯一性和线程安全，所以推荐这种方式
    private Singleton() {
    }
    public static Singleton getInstance() {
        return SingletonHolder.sInstance;
    }
    private static class SingletonHolder {
        //这里用final定义sInstance不可变，所以就只会用一次
        private static final Singleton sInstance = new Singleton();
    }

    //枚举单例,简单，但可读性不高，大部分应用很少用枚举法
//    public enum Singleton {
//        INSTANCE;
//        public void doSomeThing() {}
//    }


}
