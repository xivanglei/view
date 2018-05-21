package xl.gcs.com.strategy;

/**
 * Created by xianglei on 2018/5/13.
 * 构造方法包含了策略类，通过传进来不同的具体策略来调用不同策略的fighting方法
 */

public class Context {
    private FightingStrategy mFightingStrategy;
    public Context(FightingStrategy fightingStrategy) {
        mFightingStrategy = fightingStrategy;
    }
    public void fighting() {
        mFightingStrategy.fighting();
    }
}
