package xl.gcs.com.decorate;

/**
 * Created by xianglei on 2018/5/13.
 * 抽象装饰者
 */

public abstract class Master extends Swordsman{
    private Swordsman mSwordsman;

    public Master(Swordsman swordsman) {
        mSwordsman = swordsman;
    }

    @Override
    public void attackMagic() {
        //先用自己的武功，后面被复写会增加些新学的武功
        mSwordsman.attackMagic();
    }
}
