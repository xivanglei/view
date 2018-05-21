package xl.gcs.com.factory;

/**
 * Created by xianglei on 2018/5/12.
 * 工厂类
 */

//简单工厂模式，避免直接实例化类，降低了耦合性，但每次新增类，都需要修改工厂类。
//public class ComputerFactory {
//    public static Computer createComputer(String type) {
//        Computer mComputer = null;
//        switch(type) {
//            case "lenovo":
//                mComputer = new LenovoComputer();
//                break;
//            case "hp":
//                mComputer = new HpComputer();
//                break;
//            case "asus":
//                mComputer = new AsusComputer();
//                break;
//        }
//        return mComputer;
//    }
//}



//工厂方法模式,先要创建抽象工厂，一会由具体工厂来继承实现，想生产哪个品牌的计算机就生产哪个品牌的
public abstract class ComputerFactory {
    public abstract <T extends Computer> T createComputer(Class<T> clz);
}
