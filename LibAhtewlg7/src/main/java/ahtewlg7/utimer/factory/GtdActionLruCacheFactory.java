package ahtewlg7.utimer.factory;

import android.text.TextUtils;

import ahtewlg7.utimer.entity.gtd.GtdActionEntity;

/**
 * Created by lw on 2019/3/13.
 */
public class GtdActionLruCacheFactory extends ABaseLruCacheFactory<String, GtdActionEntity> {
    private static GtdActionLruCacheFactory instance;

    protected GtdActionLruCacheFactory(){
        super();
    }

    public static GtdActionLruCacheFactory getInstance() {
        if(instance == null)
            instance = new GtdActionLruCacheFactory();
        return instance;
    }

    @Override
    protected boolean ifKeyValid(String uuid) {
        return !TextUtils.isEmpty(uuid);
    }

    @Override
    protected boolean ifValueValid(GtdActionEntity actionEntity) {
        return actionEntity != null && actionEntity.ifValid();
    }
}
