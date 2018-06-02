package xl.gcs.com.rxjava.bus;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;


public class RxBus {
    private static volatile RxBus rxBus;
    //Subject可以看成是一个桥梁或者代理，在某些ReactiveX实现中（如RxJava），它同时充当了Observer和Observable的角色。
    // 因为它是一个Observer，它可以订阅一个或多个Observable；又因为它是一个Observable，它可以转发它收到(Observe)的数据，也可以发射新的数据。

    //PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者。需要注意的是，PublishSubject可能会一创建完成
    // 就立刻开始发射数据（除非你可以阻止它发生），因此这里有一个风险：在Subject被创建后到有观察者订阅它之前这个时间段内，
    // 一个或多个数据可能会丢失。如果要确保来自原始Observable的所有数据都被分发，你需要这样做：或者使用Create创建那个Observable
    // 以便手动给它引入"冷"Observable的行为（当所有观察者都已经订阅时才开始发射数据），或者改用ReplaySubject。

    //如果你把 Subject 当作一个 Subscriber 使用，注意不要从多个线程中调用它的onNext方法（包括其它的on系列方法），
    // 这可能导致同时（非顺序）调用，这会违反Observable协议，给Subject的结果增加了不确定性,因为Subject是非线程安全的。
    // 要避免此类问题，你可以将 Subject 转换为一个 SerializedSubject
    private final Subject<Object, Object> subject = new SerializedSubject<>(PublishSubject.create());
    private RxBus() {
    }

    public static RxBus getInstance() {
        if (rxBus == null) {
            //这里用了双重检查模式
            synchronized (RxBus.class) {
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    public void post(Object ob) {
        subject.onNext(ob);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        //ofType的源码查看下，里面用filter过滤掉，结果必须是eventType类型，cast将多个Observable转换成指定类型的Observable
        return subject.ofType(eventType);
    }
}
