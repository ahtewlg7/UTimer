package ahtewlg7.utimer.factory;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ahtewlg7.utimer.comparator.GtdTimeComparator;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.enumtype.ActState;
import ahtewlg7.utimer.enumtype.GtdLife;
import ahtewlg7.utimer.gtd.GtdLifeCycleAction;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by lw on 2019/3/13.
 */
public class GtdActionByUuidFactory extends ABaseLruCacheFactory<String, GtdActionEntity> {
    private static GtdActionByUuidFactory instance;

    private BiMap<String, String> detailUuidMap;
    private Multimap<ActState, String> stateUuidMultiMap;

    private GtdLifeCycleAction lifeCycleAction;

    protected GtdActionByUuidFactory(){
        super();
        detailUuidMap       = HashBiMap.create();
        stateUuidMultiMap   = HashMultimap.create();
        lifeCycleAction     = new GtdLifeCycleAction();
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
    public boolean add(String key, GtdActionEntity actionEntity) {
        boolean result = super.add(key, actionEntity);
        if(result && actionEntity.getDetail().isPresent())
            detailUuidMap.put(actionEntity.getDetail().get(), actionEntity.getUuid());
        if(result && actionEntity.getActionState() != null)
            stateUuidMultiMap.put(actionEntity.getActionState(), actionEntity.getUuid());
        return result;
    }

    @Override
    public GtdActionEntity remove(String key) {
        GtdActionEntity actionEntity = super.remove(key);
        if(actionEntity != null && actionEntity.ifValid() && detailUuidMap.containsValue(actionEntity.getUuid()))
            detailUuidMap.inverse().remove(actionEntity.getUuid());
        if(actionEntity != null && actionEntity.ifValid())
            stateUuidMultiMap.remove(actionEntity.getActionState(), actionEntity.getUuid());
        return actionEntity;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        detailUuidMap.clear();
        stateUuidMultiMap.clear();
    }

    public Flowable<GtdActionEntity> getEntityByState(@NonNull ActState actState){
        return Flowable.fromIterable(getEntityByUuid(new ArrayList<String>(stateUuidMultiMap.get(actState))));
    }
    public Flowable<GtdActionEntity> getEntityByState(){
        return Flowable.fromIterable(ActState.getActiveAll()).flatMap(new Function<ActState, Publisher<GtdActionEntity>>() {
            @Override
            public Publisher<GtdActionEntity> apply(ActState actState) throws Exception {
                return getEntityByState(actState);
            }
        });
    }

    public Flowable<GtdActionEntity> getAllLifeEntity(){
        return Flowable.fromIterable(getAll()).sorted(new GtdTimeComparator<GtdActionEntity>());
        }

    public Flowable<GtdActionEntity> getEntityByLife(@NonNull final GtdLife actLife){
        return getAllLifeEntity().filter(new Predicate<GtdActionEntity>() {
                    @Override
                    public boolean test(GtdActionEntity entity) throws Exception {
                        return entity.getGtdLife() == actLife;
                    }
                });
    }

    private List<GtdActionEntity> getEntityByUuid(@NonNull List<String> uuidList){
        List<GtdActionEntity> entityList = Lists.newArrayList();
        for(String uuid : uuidList){
            if(TextUtils.isEmpty(uuid))
                continue;
            GtdActionEntity entity = get(uuid);
            if(entity != null)
                entityList.add(entity);
        }
        Collections.sort(entityList, new GtdTimeComparator<GtdActionEntity>());
        return entityList;
    }

    public Optional<GtdActionEntity> getActionByDetail(String detail){
        if(!TextUtils.isEmpty(detail) && detailUuidMap.containsKey(detail))
            return Optional.fromNullable(get(detailUuidMap.get(detail)));
        return Optional.absent();
    }
}
