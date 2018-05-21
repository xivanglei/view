package xl.gcs.com.template;

/**
 * Created by xianglei on 2018/5/13.
 * 抽象模板类，固定个模板，所有子类基本按照这个模板来执行
 */

public abstract class AbstractSwordsman {
    public final void fighting() {
        //运行内功，抽象方法
        neigong();
        //调整经脉，具体方法
        meridian();
        //如果有武器，则准备武器
        if(hasWeapons()) {
            weapons();
        }
        //使用招式
        moves();
        //钩子方法
        hook();
    }

    protected void hook(){}
    protected abstract void neigong();
    protected abstract void weapons();
    protected abstract void moves();
    protected void meridian() {
        System.out.println("开启正经与奇经");
    }
    //是否有武器,默认是有的
    protected boolean hasWeapons() {
        return true;
    }
}
