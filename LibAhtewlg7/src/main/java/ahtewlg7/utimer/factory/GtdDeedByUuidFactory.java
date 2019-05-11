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
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.ActState;
import ahtewlg7.utimer.enumtype.GtdLife;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by lw on 2019/3/13.
 */
public class GtdDeedByUuidFactory extends ABaseLruCacheFactory<String, GtdDeedEntity> {
    private static GtdDeedByUuidFactory instance;

    private BiMap<String, String> detailUuidMap;
    private Multimap<ActState, String> stateUuidMultiMap;


    protected GtdDeedByUuidFactory(){
        super();
        detailUuidMap       = HashBiMap.create();
        stateUuidMultiMap   = HashMultimap.create();
    }

    public static GtdDeedByUuidFactory getInstance() {
        if(instance == null)
            instance = new GtdDeedByUuidFactory();
        return instance;
    }

    @Override
    protected boolean ifKeyValid(String uuid) {
        return !TextUtils.isEmpty(uuid);
    }

    @Override
    protected boolean ifValueValid(GtdDeedEntity actionEntity) {
        return actionEntity != null && actionEntity.ifValid();
    }

    @Override
    public boolean add(String key, GtdDeedEntity actionEntity) {
        if(detailUuidMap.containsKey(key))
            return false;
        boolean result = super.add(key, actionEntity);
        if(result && actionEntity.getDetail().isPresent())
            detailUuidMap.put(actionEntity.getDetail().get(), actionEntity.getUuid());
        if(result && actionEntity.getActionState() != null)
            stateUuidMultiMap.put(actionEntity.getActionState(), actionEntity.getUuid());
        return result;
    }

    @Override
    public GtdDeedEntity remove(String key) {
        GtdDeedEntity actionEntity = super.remove(key);
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
    public void updateState(ActState preState, GtdDeedEntity actionEntity){
        if(preState != null && actionEntity != null && actionEntity.ifValid()) {
            stateUuidMultiMap.remove(preState, actionEntity.getUuid());
            stateUuidMultiMap.put(actionEntity.getActionState(), actionEntity.getUuid());
        }
    }

    public Flowable<GtdDeedEntity> getEntityByState(@NonNull ActState actState){
        return Flowable.fromIterable(getEntityByUuid(new ArrayList<String>(stateUuidMultiMap.get(actState))));
    }
    public Flowable<GtdDeedEntity> getEntityByState(){
        return Flowable.fromIterable(ActState.getActiveAll()).flatMap(new Function<ActState, Publisher<GtdDeedEntity>>() {
            @Override
            public Publisher<GtdDeedEntity> apply(ActState actState) throws Exception {
                return getEntityByState(actState);
            }
        });
    }

    public Flowable<GtdDeedEntity> getEntityByLife(){
        return Flowable.fromIterable(getAll()).sorted(new GtdTimeComparator<GtdDeedEntity>());
        }

    public Flowable<GtdDeedEntity> getEntityByLife(@NonNull final GtdLife actLife){
        return getEntityByLife().filter(new Predicate<GtdDeedEntity>() {
                    @Override
                    public boolean test(GtdDeedEntity entity) throws Exception {
                        return entity.getGtdLife() == actLife;
                    }
                });
    }

    private List<GtdDeedEntity> getEntityByUuid(@NonNull List<String> uuidList){
        List<GtdDeedEntity> entityList = Lists.newArrayList();
        for(String uuid : uuidList){
            if(TextUtils.isEmpty(uuid))
                continue;
            GtdDeedEntity entity = get(uuid);
            if(entity != null)
                entityList.add(entity);
        }
        Collections.sort(entityList, new GtdTimeComparator<GtdDeedEntity>());
        return entityList;
    }

    public Optional<GtdDeedEntity> getActionByDetail(String detail){
        if(!TextUtils.isEmpty(detail) && detailUuidMap.containsKey(detail))
            return Optional.fromNullable(get(detailUuidMap.get(detail)));
        return Optional.absent();
    }
}
