package ahtewlg7.utimer.factory;

import android.text.TextUtils;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.gtd.ShortHandEntity;


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
        addValue(entity.getCreateTime(), entity.getTitle(), entity);
    }

    public void newAndAddVaule(){
        addValue(newValue());
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
    public ShortHandEntity newValue() {
        return new ShortHandEntity.Builder().build();
    }
}
