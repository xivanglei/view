package xl.gcs.com;

import java.lang.reflect.Proxy;

import xl.gcs.com.agent.DynamicPurchasing;
import xl.gcs.com.agent.IShop;
import xl.gcs.com.agent.LiuWangShu;
import xl.gcs.com.agent.Purchasing;
import xl.gcs.com.builder.Builder;
import xl.gcs.com.builder.Computer;
import xl.gcs.com.builder.Director;
import xl.gcs.com.builder.MoonComputerBuilder;
import xl.gcs.com.decorate.HongQiGong;
import xl.gcs.com.decorate.OuYangFeng;
import xl.gcs.com.decorate.YangGuo;
import xl.gcs.com.facade.ZhangWuJi;
import xl.gcs.com.factory.AsusComputer;
import xl.gcs.com.factory.ComputerFactory;
import xl.gcs.com.factory.GDComputerFactory;
import xl.gcs.com.factory.LenovoComputer;
import xl.gcs.com.flyweight.Goods;
import xl.gcs.com.flyweight.GoodsFactory;
import xl.gcs.com.observer.SubscriptionSubject;
import xl.gcs.com.observer.WeixinUser;
import xl.gcs.com.singleton.Singleton;
import xl.gcs.com.strategy.CommonRivalStrategy;
import xl.gcs.com.strategy.Context;
import xl.gcs.com.strategy.StrongRivalStrategy;
import xl.gcs.com.strategy.WeakRivalStrategy;
import xl.gcs.com.template.ZhangSanFeng;

/**
 * Created by xianglei on 2018/5/13.
 */

public class Client {
    public static void main(String[] args) {
        //调用单例模式类
        Singleton singleton = Singleton.getInstance();

        //调用工厂方法模式，不需要具体实现逻辑，只要是同类型的，传类名进去就可以用了
        ComputerFactory computerFactory = new GDComputerFactory();
        LenovoComputer lenovoComputer = computerFactory.createComputer(LenovoComputer.class);
        lenovoComputer.start();
        AsusComputer asusComputer = computerFactory.createComputer(AsusComputer.class);
        asusComputer.start();

        //调用建造者模式导演类
        Builder builder = new MoonComputerBuilder();
        Director director = new Director(builder);
        //组装计算机
        Computer computer = director.createComputer("i7-6700", "华擎玩家至尊", "三星DDR4");

        //代理模式的简单实现(静态代理)
        IShop liuwangshu = new LiuWangShu();
        IShop purchasing = new Purchasing(liuwangshu);
        purchasing.buy();

        //调用动态代理模式
        IShop liuwangshu2 = new LiuWangShu();
        //创建动态代理
        DynamicPurchasing dynamicPurchasing = new DynamicPurchasing(liuwangshu2);
        //创建LiuWangShu的ClassLoader
        ClassLoader loader = liuwangshu2.getClass().getClassLoader();
        //动态创建代理类
        IShop purchasing2 = (IShop) Proxy.newProxyInstance(loader, new Class[] {IShop.class}, dynamicPurchasing);
        purchasing.buy();

        //调用装饰模式
        YangGuo yangGuo = new YangGuo();
        //洪七公向杨过传授打狗棒法，杨过学会了打狗棒法
        HongQiGong hongQiGong = new HongQiGong(yangGuo);
        hongQiGong.attackMagic();
        //欧阳锋向杨过传授蛤蟆功，杨过学会了蛤蟆功
        OuYangFeng ouYangFeng = new OuYangFeng(yangGuo);
        ouYangFeng.attackMagic();

        //外观模式的简单实现
        ZhangWuJi zhangWuJi = new ZhangWuJi();
        zhangWuJi.QianKun();
        zhangWuJi.QiShang();

        //享元模式的简单实现
        Goods goods1 = GoodsFactory.getGoods("iphone7");
        goods1.showGoodsPrice("32G");
        Goods goods2 = GoodsFactory.getGoods("iphone7");
        goods2.showGoodsPrice("32G");
        Goods goods3 = GoodsFactory.getGoods("iphone7");
        goods3.showGoodsPrice("128G");

        //策略模式的简单实现,只是简单剧烈，实际情况会很多，比如遇到周芷若和赵敏就需要手下留情，所以还需要很多的条件语句来判断
        Context context;
        //张无忌遇到对手宋青书，采用对较弱对手的策略
        context = new Context(new WeakRivalStrategy());
        context.fighting();
        //张无忌遇到对手灭绝师太，采用对普通对手的策略
        context = new Context(new CommonRivalStrategy());
        context.fighting();
        //张无忌遇到对手成昆，采用对强大对手的策略
        context = new Context(new StrongRivalStrategy());
        context.fighting();

        //模板方法模式
        xl.gcs.com.template.ZhangWuJi zhangWuJi1 = new xl.gcs.com.template.ZhangWuJi();
        zhangWuJi1.fighting();
        ZhangSanFeng zhangSanFeng = new ZhangSanFeng();
        zhangSanFeng.fighting();

        //观察者模式
        SubscriptionSubject subscriptionSubject = new SubscriptionSubject();
        //创建微信用户
        WeixinUser user1 = new WeixinUser("杨影枫");
        WeixinUser user2 = new WeixinUser("月眉儿");
        WeixinUser user3 = new WeixinUser("紫轩");
        //订阅公众号
        subscriptionSubject.attach(user1);
        subscriptionSubject.attach(user2);
        subscriptionSubject.attach(user3);
        //公众号更新发出消息给订阅的微信用户
        subscriptionSubject.notify("刘望舒的专栏更新了");
    }
}
