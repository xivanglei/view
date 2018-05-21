package xl.gcs.com.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xianglei on 2018/5/13.
 */

public class GoodsFactory {
    private static Map<String,Goods> pool = new HashMap<>();
    public static Goods getGoods(String name) {
        //如果已经包含了这个产品名称
        if(pool.containsKey(name)) {
            System.out.println("使用缓存，key为:" + name);
            return pool.get(name);
        } else {
            Goods goods = new Goods(name);
            pool.put(name,goods);
            System.out.println("创建商品，key为:" + name);
            return goods;
        }
    }
}
