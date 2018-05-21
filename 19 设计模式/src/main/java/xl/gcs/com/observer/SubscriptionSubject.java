package xl.gcs.com.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianglei on 2018/5/13.
 */

public class SubscriptionSubject implements Subject {

    //储存订阅公众号的微信用户
    private List<Observer> weixinUserlist = new ArrayList<>();
    @Override
    public void attach(Observer observer) {
        weixinUserlist.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        weixinUserlist.remove(observer);
    }

    @Override
    public void notify(String message) {
        for(Observer observer : weixinUserlist) {
            observer.update(message);
        }
    }
}
