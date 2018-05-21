package xl.gcs.com.agent;

/**
 * Created by xianglei on 2018/5/13.
 * 代理类，必须要有被代理者，在buy方法中调用了被代理者的buy方法
 */

public class Purchasing implements IShop {
    private IShop mShop;
    public Purchasing(IShop shop) {
        mShop = shop;
    }
    @Override
    public void buy() {
        mShop.buy();
    }
}
