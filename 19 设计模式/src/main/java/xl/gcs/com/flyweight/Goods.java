package xl.gcs.com.flyweight;

/**
 * Created by xianglei on 2018/5/13.
 * 具体享元角色，实现了showGoodsPrice方法，来展示商品价格
 */

public class Goods implements IGoods {
    //名称，一会把键值存进去
    private String name;
    //版本，根据不同的版本显示不同的价格
    private String version;

    public Goods(String name) {
        this.name = name;
    }

    @Override
    public void showGoodsPrice(String version) {
        if(version.equals("32G")) {
            System.out.println("价格为5199元");
        } else if(version.equals("128G")) {
            System.out.println("价格为5999元");
        }
    }
}
