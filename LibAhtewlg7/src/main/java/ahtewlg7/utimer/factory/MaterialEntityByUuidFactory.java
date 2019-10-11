package ahtewlg7.utimer.factory;

import android.text.TextUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import ahtewlg7.utimer.entity.gtd.MaterialEntity;

public class MaterialEntityByUuidFactory extends ABaseLruCacheFactory<String, MaterialEntity>{
    private static MaterialEntityByUuidFactory instance;

    private BiMap<String, String> absPathUuidMap;

    private MaterialEntityByUuidFactory(){
        absPathUuidMap = HashBiMap.create();
    }

    public static MaterialEntityByUuidFactory getInstance(){
        if(instance == null)
            instance = new MaterialEntityByUuidFactory();
        return instance;
    }

    @Override
    protected boolean ifKeyValid(String uuid) {
        return !TextUtils.isEmpty(uuid);
    }

    @Override
    protected boolean ifValueValid(MaterialEntity entity) {
        return entity != null && entity.ifValid();
    }

    @Override
    public boolean add(String s, MaterialEntity entity) {
        boolean result = false;
        if(entity != null && entity.ifValid()
            && entity.getAttachFileRPath().isPresent()
            && !absPathUuidMap.containsKey(entity.getAttachFileRPath().get())) {
            result = super.add(s, entity);
            if (result)
                absPathUuidMap.put(entity.getAbsPath(), entity.getUuid());
        }
        return result;
    }

    @Override
    public MaterialEntity remove(String s) {
        MaterialEntity entity = super.remove(s);
        if(entity != null)
            absPathUuidMap.inverse().remove(entity.getUuid());
        return entity;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        absPathUuidMap.clear();
    }
}
