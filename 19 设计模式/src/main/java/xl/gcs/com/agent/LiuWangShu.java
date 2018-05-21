package xl.gcs.com.agent;

/**
 * Created by xianglei on 2018/5/13.
 * 真实主题类，实现buy方法，要买东西
 */

public class LiuWangShu implements IShop {
    @Override
    public void buy() {
        System.out.println("购买");
    }
}
