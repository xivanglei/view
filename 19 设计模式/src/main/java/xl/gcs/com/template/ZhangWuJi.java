package xl.gcs.com.template;

/**
 * Created by xianglei on 2018/5/13.
 */

public class ZhangWuJi extends AbstractSwordsman {
    @Override
    protected void neigong() {
        System.out.println("运行九阳神功");
    }

    //武器不准备用，所以方法里也不写了
    @Override
    protected void weapons() {
    }

    @Override
    protected void moves() {
        System.out.println("使用招式乾坤大挪移");
    }

    //不用武器，返回false
    @Override
    protected boolean hasWeapons() {
        return false;
    }
}
