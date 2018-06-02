package xl.gcs.com.myapplication.dagger2.component;



import dagger.Component;
import dagger.Subcomponent;
import xl.gcs.com.myapplication.dagger2.model.Swordsman;
import xl.gcs.com.myapplication.dagger2.module.SwordsmanModule;

/**
 * Created by Administrator on 2016/12/21 0021.
 */
@Component(modules = SwordsmanModule.class)
public interface SwordsmanComponent {
    Swordsman getSwordsman();
}
