package xl.gcs.com.myapplication.dagger2.module;

import dagger.Module;
import dagger.Provides;
import xl.gcs.com.myapplication.dagger2.model.Swordsman;

/**
 * Created by Administrator on 2016/12/21 0021.
 */
@Module
public class SwordsmanModule {
    @Provides
    public Swordsman provideSwordsman() {
        return new Swordsman();
    }
}

