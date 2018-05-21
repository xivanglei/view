package xl.gcs.com.factory;

/**
 * Created by xianglei on 2018/5/13.
 */

public class GDComputerFactory extends ComputerFactory {
    @Override
    public <T extends Computer> T createComputer(Class<T> clz) {
        Computer computer = null;
        String classname = clz.getName();
        try {
            //通过反射来生产不同厂家的计算机
            computer = (Computer) Class.forName(classname).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) computer;
    }
}
