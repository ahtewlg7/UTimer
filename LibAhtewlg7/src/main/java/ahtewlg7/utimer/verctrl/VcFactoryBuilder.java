package ahtewlg7.utimer.verctrl;

import com.google.common.base.Optional;

/**
 * Created by lw on 2017/2/7.
 */
public class VcFactoryBuilder {
    private IBaseVersionControlFactory baseConfigFactory;

    private static VcFactoryBuilder instance;

    private VcFactoryBuilder(){
    }

    public static VcFactoryBuilder getInstance(){
        if(instance == null)
            instance = new VcFactoryBuilder();
        return instance;
    }
    public void setBaseConfigFactory(IBaseVersionControlFactory baseConfigFactory){
        this.baseConfigFactory = baseConfigFactory;
    }

    public IBaseVersionControlFactory getVersionControlFactory(){
        return baseConfigFactory;
    }

    public Optional<? extends BaseConfig> getVcConfig(){
        return baseConfigFactory != null ? Optional.fromNullable(baseConfigFactory.getBaseConfig()): Optional.<BaseConfig>absent();
    }
}
