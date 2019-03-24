package ahtewlg7.utimer.factory;

import android.text.TextUtils;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import ahtewlg7.utimer.entity.gtd.GtdActionEntity;

/**
 * Created by lw on 2019/3/13.
 */
public class GtdActionByUuidFactory extends ABaseLruCacheFactory<String, GtdActionEntity> {
    private static GtdActionByUuidFactory instance;

    private BiMap<String, String> tipsUuidMap;
    private BiMap<String, String> detailUuidMap;

    protected GtdActionByUuidFactory(){
        super();
        tipsUuidMap   = HashBiMap.create();
        detailUuidMap = HashBiMap.create();
    }

    public static GtdActionByUuidFactory getInstance() {
        if(instance == null)
            instance = new GtdActionByUuidFactory();
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
    public boolean add(String s, GtdActionEntity actionEntity) {
        boolean result = super.add(s, actionEntity);
        if(result && actionEntity.toTips().isPresent())
            tipsUuidMap.put(actionEntity.toTips().get(), actionEntity.getUuid());
        if(result && actionEntity.getDetail().isPresent())
            detailUuidMap.put(actionEntity.getDetail().get(), actionEntity.getUuid());
        return result;
    }

    @Override
    public GtdActionEntity remove(String s) {
        GtdActionEntity actionEntity = super.remove(s);
        if(tipsUuidMap.containsValue(actionEntity.getUuid()))
            tipsUuidMap.inverse().remove(actionEntity.getUuid());
        if(detailUuidMap.containsValue(actionEntity.getUuid()))
            detailUuidMap.inverse().remove(actionEntity.getUuid());
        return actionEntity;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        tipsUuidMap.clear();
        detailUuidMap.clear();
    }

    public Optional<GtdActionEntity> getActionByTips(String tip){
        if(!TextUtils.isEmpty(tip) && tipsUuidMap.containsKey(tip))
            return Optional.fromNullable(get(tipsUuidMap.get(tip)));
        return Optional.absent();
    }

    public Optional<GtdActionEntity> getActionByDetail(String detail){
        if(!TextUtils.isEmpty(detail) && detailUuidMap.containsKey(detail))
            return Optional.fromNullable(get(detailUuidMap.get(detail)));
        return Optional.absent();
    }
}
