package ahtewlg7.utimer.factory;

import android.text.TextUtils;

import com.google.common.collect.Lists;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdActionEntity;

/**
 * Created by lw on 2019/3/13.
 */
public class GtdActionCacheFactory extends ABaseLruCacheFactory<String, GtdActionEntity> {
    private static GtdActionCacheFactory instance;

    private List<GtdActionEntity> gtdActionEntityList;

    protected GtdActionCacheFactory(){
        super();
        gtdActionEntityList = Lists.newArrayList();
    }

    public static GtdActionCacheFactory getInstance() {
        if(instance == null)
            instance = new GtdActionCacheFactory();
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

    @Override
    public List<GtdActionEntity> getAll() {
        return gtdActionEntityList;
    }

    @Override
    public boolean add(String s, GtdActionEntity actionEntity) {
        boolean result = super.add(s, actionEntity);
        if(result)
            gtdActionEntityList.add(actionEntity);
        return result;
    }

    @Override
    public GtdActionEntity remove(String s) {
        GtdActionEntity entity = super.remove(s);
        if(entity != null)
            gtdActionEntityList.remove(entity);
        return entity;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        gtdActionEntityList.clear();
    }
}
