package ahtewlg7.utimer.factory;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

import org.reactivestreams.Publisher;

import java.io.File;
import java.util.Collections;
import java.util.List;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.comparator.GtdTimeComparator;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.enumtype.GtdLife;
import ahtewlg7.utimer.gtd.GtdLifeCycleAction;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static ahtewlg7.utimer.enumtype.GtdLife.MONTH;
import static ahtewlg7.utimer.enumtype.GtdLife.QUARTER;
import static ahtewlg7.utimer.enumtype.GtdLife.TODAY;
import static ahtewlg7.utimer.enumtype.GtdLife.TOMORROW;
import static ahtewlg7.utimer.enumtype.GtdLife.WEEK;
import static ahtewlg7.utimer.enumtype.GtdLife.YEAR;

/**
 * Created by lw on 2019/3/13.
 */
public class ShortHandByUuidFactory extends ABaseLruCacheFactory<String, ShortHandEntity> {
    private static ShortHandByUuidFactory instance;

    private List<String> shorHandFileList;
    private BiMap<String, String> pathUuidMap;

    private GtdLifeCycleAction lifeCycleAction;

    protected ShortHandByUuidFactory(){
        super();
        shorHandFileList    = Lists.newArrayList();
        pathUuidMap         = HashBiMap.create();
        lifeCycleAction     = new GtdLifeCycleAction();

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

    public Flowable<ShortHandEntity> getEntityByLife(@NonNull final GtdLife actLife){
        return Flowable.fromIterable(getAll())
                .filter(new Predicate<ShortHandEntity>() {
                    @Override
                    public boolean test(ShortHandEntity entity) throws Exception {
                        GtdLife life = lifeCycleAction.getLife(entity.getCreateTime());
                        entity.setGtdLife(life);
                        return life == actLife && entity.getAttachFileRPath().isPresent()
                                && shorHandFileList.contains(entity.getAttachFileRPath().get());
                    }
                })
                .sorted(new GtdTimeComparator<ShortHandEntity>());
    }
    public Flowable<ShortHandEntity> getEntityByLife(){
        return Flowable.fromArray(TODAY,TOMORROW,WEEK,MONTH,QUARTER,YEAR).flatMap(new Function<GtdLife, Publisher<ShortHandEntity>>() {
            @Override
            public Publisher<ShortHandEntity> apply(GtdLife actLife) throws Exception {
                return getEntityByLife(actLife);
            }
        });
    }

    public List<ShortHandEntity> getEntityByUuid(@NonNull List<String> uuidList){
        List<ShortHandEntity> entityList = Lists.newArrayList();
        for(String uuid : uuidList){
            if(TextUtils.isEmpty(uuid))
                continue;
            ShortHandEntity entity = get(uuid);
            if(entity != null)
                entityList.add(entity);
        }
        Collections.sort(entityList, new GtdTimeComparator<ShortHandEntity>());
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
