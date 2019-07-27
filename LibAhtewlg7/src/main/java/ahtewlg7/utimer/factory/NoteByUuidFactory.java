package ahtewlg7.utimer.factory;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.enumtype.DateLife;
import io.reactivex.Flowable;
import io.reactivex.functions.Predicate;

/**
 * Created by lw on 2019/3/13.
 */
public class NoteByUuidFactory extends ABaseLruCacheFactory<String, NoteEntity> {
    private static NoteByUuidFactory instance;

    private BiMap<String, String> pathUuidMap;

    protected NoteByUuidFactory(){
        super();
        pathUuidMap  = HashBiMap.create();
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
    public Flowable<NoteEntity> getEntityByProject(final GtdProjectEntity projectEntity){
        return getAllLifeEntity().filter(new Predicate<NoteEntity>() {
            @Override
            public boolean test(NoteEntity entity) throws Exception {
                return entity.getAttachFileRPath().isPresent() && projectEntity.getAttachFileRPath().isPresent()
                        && entity.getAttachFileRPath().get().startsWith(projectEntity.getAttachFileRPath().get());
            }
        });
    }
    public Flowable<NoteEntity> getAllLifeEntity(){
        return Flowable.fromIterable(getAll());
    }
    public Flowable<NoteEntity> getEntityByLife(@NonNull final DateLife actLife){
        return getAllLifeEntity().filter(new Predicate<NoteEntity>() {
                    @Override
                    public boolean test(NoteEntity entity) throws Exception {
                        return entity.getCreateDateLife() == actLife;
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
        return entityList;
    }
}
