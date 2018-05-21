package xl.gcs.com.decorate;

/**
 * Created by xianglei on 2018/5/13.
 * 装饰具体实现类
 */

public class HongQiGong extends Master {

    public HongQiGong(Swordsman swordsman) {
        super(swordsman);
    }

    public void teachAttackMagic() {
        System.out.println("洪七公教授打狗棒法");
        System.out.println("杨过使用打狗棒法");
    }

    @Override
    public void attackMagic() {
        super.attackMagic();
        teachAttackMagic();
    }
}
