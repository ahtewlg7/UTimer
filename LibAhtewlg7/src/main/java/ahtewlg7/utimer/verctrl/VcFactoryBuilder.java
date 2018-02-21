package ahtewlg7.utimer.verctrl;

/**
 * Created by lw on 2017/2/7.
 */
public class VcFactoryBuilder {
    public static final String TAG = VcFactoryBuilder.class.getSimpleName();

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
}
