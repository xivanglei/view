package xl.gcs.com.strategy;

/**
 * Created by xianglei on 2018/5/13.
 * 不同策略来实现策略接口
 */

public class StrongRivalStrategy implements FightingStrategy {
    //这里只是简单举例，实际上可能还要加入不同的判断处理不同的情况
    @Override
    public void fighting() {
        System.out.println("遇到了强大的对手，张无忌使用乾坤大挪移");
    }
}
