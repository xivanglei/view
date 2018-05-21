package xl.gcs.com.decorate;

/**
 * Created by xianglei on 2018/5/13.
 */

public class OuYangFeng extends Master {

    public OuYangFeng(Swordsman swordsman) {
        super(swordsman);
    }

    public void teachAttackMagic() {
        System.out.println("欧阳锋教授蛤蟆功");
        System.out.println("杨过使用蛤蟆功");
    }

    @Override
    public void attackMagic() {
        super.attackMagic();
        teachAttackMagic();
    }
}
