package ahtewlg7.utimer.factory;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.Collections;
import java.util.List;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.comparator.GtdTimeComparator;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.enumtype.GtdLife;
import ahtewlg7.utimer.gtd.GtdLifeCycleAction;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by lw on 2019/3/13.
 */
public class NoteByUuidFactory extends ABaseLruCacheFactory<String, NoteEntity> {
    private static NoteByUuidFactory instance;

    private List<String> shorHandFileList;
    private BiMap<String, String> pathUuidMap;

    private GtdLifeCycleAction lifeCycleAction;

    protected NoteByUuidFactory(){
        super();
        shorHandFileList    = Lists.newArrayList();
        pathUuidMap         = HashBiMap.create();
        lifeCycleAction     = new GtdLifeCycleAction();

        toUpdatePathList();
    }

    public static NoteByUuidFactory getInstance() {
        if(instance == null)
            instance = new NoteByUuidFactory();
        return instance;
    }

    @Override
    protected boolean ifKeyValid(String uuid) {
        return !TextUtils.isEmpty(uuid);
    }

    @Override
    protected boolean ifValueValid(NoteEntity entity) {
        return entity != null && entity.ifValid();
    }

    @Override
    public boolean add(String s, NoteEntity entity) {
        boolean result = false;
        if(entity != null && entity.ifValid()
                && entity.getAttachFileRPath().isPresent()
                && !pathUuidMap.containsKey(entity.getAttachFileRPath().get())) {
            result = super.add(s, entity);
            if (result)
                pathUuidMap.put(entity.getAttachFileRPath().get(), entity.getUuid());
        }
        return result;
    }

    @Override
    public NoteEntity remove(String s) {
        NoteEntity entity = super.remove(s);
        if(entity != null)
            pathUuidMap.inverse().remove(entity.getUuid());
        return entity;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        pathUuidMap.clear();
    }
    public Flowable<NoteEntity> getAllLifeEntity(){
        return Flowable.fromIterable(getAll()).doOnNext(new Consumer<NoteEntity>() {
            @Override
            public void accept(NoteEntity entity) throws Exception {
                GtdLife life = lifeCycleAction.getLife(entity.getCreateTime());
                entity.setGtdLife(life);
            }
        }).sorted(new GtdTimeComparator<NoteEntity>());
    }
    public Flowable<NoteEntity> getEntityByLife(@NonNull final GtdLife actLife){
        return getAllLifeEntity().filter(new Predicate<NoteEntity>() {
                    @Override
                    public boolean test(NoteEntity entity) throws Exception {
                        return entity.getGtdLife() == actLife && entity.getAttachFileRPath().isPresent()
                                && shorHandFileList.contains(entity.getAttachFileRPath().get());
                    }
                });
    }

    public List<NoteEntity> getEntityByUuid(@NonNull List<String> uuidList){
        List<NoteEntity> entityList = Lists.newArrayList();
        for(String uuid : uuidList){
            if(TextUtils.isEmpty(uuid))
                continue;
            NoteEntity entity = get(uuid);
            if(entity != null)
                entityList.add(entity);
        }
        Collections.sort(entityList, new GtdTimeComparator<NoteEntity>());
        return entityList;
    }

    public void toUpdatePathList(){
        try{
            FileSystemAction fileSystemAction = new FileSystemAction();
            String path         = fileSystemAction.getInboxGtdAbsPath();
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
