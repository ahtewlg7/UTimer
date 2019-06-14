package ahtewlg7.utimer.factory;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.enumtype.GtdLife;
import io.reactivex.Flowable;
import io.reactivex.functions.Predicate;

/**
 * Created by lw on 2019/3/13.
 */
public class ShortHandByUuidFactory extends ABaseLruCacheFactory<String, ShortHandEntity> {
    private static ShortHandByUuidFactory instance;

    private List<String> shorHandFileList;
    private BiMap<String, String> pathUuidMap;

    protected ShortHandByUuidFactory(){
        super();
        shorHandFileList    = Lists.newArrayList();
        pathUuidMap         = HashBiMap.create();

        toUpdatePathList();
    }

    public static ShortHandByUuidFactory getInstance() {
        if(instance == null)
            instance = new ShortHandByUuidFactory();
        return instance;
    }

    @Override
    protected boolean ifKeyValid(String uuid) {
        return !TextUtils.isEmpty(uuid);
    }

    @Override
    protected boolean ifValueValid(ShortHandEntity entity) {
        return entity != null && entity.ifValid();
    }

    @Override
    public boolean add(String s, ShortHandEntity shortHandEntity) {
        boolean result = false;
        if(shortHandEntity != null && shortHandEntity.ifValid()
                && shortHandEntity.getAttachFileRPath().isPresent()
                && !pathUuidMap.containsKey(shortHandEntity.getAttachFileRPath().get())) {
            result = super.add(s, shortHandEntity);
            if (result)
                pathUuidMap.put(shortHandEntity.getAttachFileRPath().get(), shortHandEntity.getUuid());
        }
        return result;
    }

    @Override
    public ShortHandEntity remove(String s) {
        ShortHandEntity entity = super.remove(s);
        if(entity != null)
            pathUuidMap.inverse().remove(entity.getUuid());
        return entity;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        pathUuidMap.clear();
    }
    public Flowable<ShortHandEntity> getAllLifeEntity(){
        return Flowable.fromIterable(getAll());
    }
    public Flowable<ShortHandEntity> getEntityByLife(@NonNull final GtdLife actLife){
        return getAllLifeEntity().filter(new Predicate<ShortHandEntity>() {
                    @Override
                    public boolean test(ShortHandEntity entity) throws Exception {
                        return entity.getGtdLife() == actLife && entity.getAttachFileRPath().isPresent()
                                && shorHandFileList.contains(entity.getAttachFileRPath().get());
                    }
                });
    }

    public List<ShortHandEntity> getEntitiesByUuid(@NonNull List<String> uuidList){
        List<ShortHandEntity> entityList = Lists.newArrayList();
        for(String uuid : uuidList){
            if(TextUtils.isEmpty(uuid))
                continue;
            ShortHandEntity entity = get(uuid);
            if(entity != null)
                entityList.add(entity);
        }
        return entityList;
    }

    public void toUpdatePathList(){
        try{
            FileSystemAction fileSystemAction = new FileSystemAction();
            String path         = fileSystemAction.getShorthandNoteAbsPath();
            File shortHandDir   = FileUtils.getFileByPath(path);
            if(!shortHandDir.exists())
                return;
            shorHandFileList.clear();
            for(File file : shortHandDir.listFiles()){
                String tmp = fileSystemAction.getRPath(file);
                if(!TextUtils.isEmpty(tmp))
                    shorHandFileList.add(tmp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
