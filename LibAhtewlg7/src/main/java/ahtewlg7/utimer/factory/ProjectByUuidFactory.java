package ahtewlg7.utimer.factory;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.comparator.LastAccessTimeComparator;
import ahtewlg7.utimer.entity.gtd.GtdProjectBuilder;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.material.DirAttachFile;
import ahtewlg7.utimer.enumtype.DateLife;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2019/3/13.
 * @key is uuid
 */
public class ProjectByUuidFactory extends ABaseLruCacheFactory<String, GtdProjectEntity> {
    private static ProjectByUuidFactory instance;

    private BiMap<String, String> absFilePathUuidMap;

    protected ProjectByUuidFactory(){
        super();
        absFilePathUuidMap = HashBiMap.create();
    }

    public static ProjectByUuidFactory getInstance() {
        if(instance == null)
            instance = new ProjectByUuidFactory();
        return instance;
    }

    @Override
    protected boolean ifKeyValid(String uuid) {
        return !TextUtils.isEmpty(uuid);
    }

    @Override
    protected boolean ifValueValid(GtdProjectEntity projectEntity) {
        return projectEntity != null && projectEntity.ifValid();
    }

    @Override
    public boolean add(String key, GtdProjectEntity projectEntity) {
        if(absFilePathUuidMap.containsKey(key))
            return false;
        boolean result = super.add(key, projectEntity);
        if(result && projectEntity.getAttachFileAbsPath().isPresent())
            absFilePathUuidMap.put(projectEntity.getAttachFileAbsPath().get(), projectEntity.getUuid());
        return result;
    }

    @Override
    public GtdProjectEntity remove(String key) {
        GtdProjectEntity projectEntity = super.remove(key);
        if(projectEntity != null && absFilePathUuidMap.containsValue(projectEntity.getUuid()))
            absFilePathUuidMap.inverse().remove(projectEntity.getUuid());
        return projectEntity;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        absFilePathUuidMap.clear();
    }
    public boolean ifValid(GtdProjectEntity projectEntity){
        return projectEntity != null && projectEntity.ifValid() && get(projectEntity.getUuid()) != null;
    }
    public Optional<GtdProjectEntity> create(String name){
        String defaultRootPath = new FileSystemAction().getDefaultProjectDocAbsPath();
        return create(defaultRootPath, name);
    }
    public Optional<GtdProjectEntity> create(String parentPath, String name){
        if(TextUtils.isEmpty(parentPath) || TextUtils.isEmpty(name))
            return Optional.absent();

        GtdProjectEntity projectEntity = get(parentPath + name);
        if(projectEntity != null)
            return Optional.of(projectEntity);
        DirAttachFile attachFile = new DirAttachFile(new File(parentPath,name));
        projectEntity = new GtdProjectBuilder().setAttachFile(attachFile).build();
        boolean result = projectEntity.ensureAttachFileExist();
        return result ? Optional.of(projectEntity) : Optional.absent();
    }

    public Flowable<GtdProjectEntity> getEntityByLife(){
         return Flowable.fromIterable(getAll()).sorted(new LastAccessTimeComparator<>().getDescOrder())
                 .subscribeOn(Schedulers.computation());
         }

     public Flowable<GtdProjectEntity> getEntityByLastModifyLife(@NonNull final DateLife actLife){
         return getEntityByLife().filter(new io.reactivex.functions.Predicate<GtdProjectEntity>() {
             @Override
             public boolean test(GtdProjectEntity entity) throws Exception {
                 return entity.getLastModifyDateLife() == actLife;
             }
         });
     }
    private List<GtdProjectEntity> getEntityByUuid(@NonNull List<String> uuidList){
        List<GtdProjectEntity> entityList = Lists.newArrayList();
        for(String uuid : uuidList){
            if(TextUtils.isEmpty(uuid))
                continue;
            GtdProjectEntity entity = get(uuid);
            if(entity != null)
                entityList.add(entity);
        }
        return entityList;
    }
}
