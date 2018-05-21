package xl.gcs.com.observer;

/**
 * Created by xianglei on 2018/5/13.
 * 抽象被观察者
 */

public interface Subject {
    //增加订阅者
    public void attach(Observer observer);
    //删除订阅者
    public void detach(Observer observer);
    //通知订阅者更新消息
    public void notify(String message);
}
