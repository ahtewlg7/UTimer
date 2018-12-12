package ahtewlg7.utimer.factory;

import android.text.TextUtils;

import org.joda.time.DateTime;

import ahtewlg7.utimer.db.entity.ShortHandEntityGdBean;
import ahtewlg7.utimer.entity.gtd.ShortHandBuilder;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.material.AAttachFile;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;


//todo：auto check if long time no update, when next get

public class ShortHandFactory extends ABaseTableFactory<DateTime, String, ShortHandEntity> {
    public static final String TAG = ShortHandFactory.class.getSimpleName();

    private static ShortHandFactory instance;

    protected ShortHandFactory() {
        super();
    }

    public static ShortHandFactory getInstance(){
        if(instance == null)
            instance = new ShortHandFactory();
        return instance;
    }

    public void addValue(ShortHandEntity entity){
        Logcat.i(TAG,"add " + entity.toString());
        addValue(entity.getCreateTime(), entity.getTitle(), entity);
    }

    public void newAndAddVaule(){
        addValue(createBean());
    }

    @Override
    public boolean ifPriKeyValid(DateTime dateTime) {
        return dateTime != null;
    }

    @Override
    public boolean ifSecKeyValid(String key) {
        return !TextUtils.isEmpty(key);
    }

    @Override
    public boolean ifValueValid(ShortHandEntity shortHandEntity) {
        return shortHandEntity != null && shortHandEntity.ifValid();
    }

    @Override
    public ShortHandEntity createBean() {
        Logcat.i(TAG,"create ShortHandEntity ");
        String now = new DateTimeAction().toFormatNow().toString();
        ShortHandBuilder builder = (ShortHandBuilder)new ShortHandBuilder().setTitle(now);
        return builder.build();
    }

    @Override
    public ShortHandEntity createBean(AAttachFile attachFile) {
        Logcat.i(TAG,"create ShortHandEntity : " + attachFile.toString());
        ShortHandBuilder builder = (ShortHandBuilder)new ShortHandBuilder().setAttachFile(attachFile);
        return builder.build();
    }
    public ShortHandEntity createBean(ShortHandEntityGdBean dbBean) {
        Logcat.i(TAG,"create ShortHandEntity : " + dbBean.toString());
        ShortHandBuilder builder = (ShortHandBuilder)new ShortHandBuilder().setGbBean(dbBean);
        return builder.build();
    }
}
