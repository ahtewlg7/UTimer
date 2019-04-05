package ahtewlg7.utimer.factory;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import org.reactivestreams.Publisher;

import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.enumtype.ActLife;
import ahtewlg7.utimer.enumtype.ActState;
import ahtewlg7.utimer.gtd.GtdActLifeCycleAction;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Created by lw on 2019/3/13.
 */
public class GtdActionByUuidFactory extends ABaseLruCacheFactory<String, GtdActionEntity> {
    private static GtdActionByUuidFactory instance;

    private BiMap<String, String> tipsUuidMap;
    private BiMap<String, String> detailUuidMap;
    private Multimap<ActLife, String> lifeUuidMultiMap;
    private Multimap<ActState, String> stateUuidMultiMap;

    private GtdActLifeCycleAction lifeCycleAction;

    protected GtdActionByUuidFactory(){
        super();
        tipsUuidMap         = HashBiMap.create();
        detailUuidMap       = HashBiMap.create();
        lifeUuidMultiMap    = HashMultimap.create();
        stateUuidMultiMap   = HashMultimap.create();
        lifeCycleAction     = new GtdActLifeCycleAction();
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
        if(result){
            lifeUuidMultiMap.put(lifeCycleAction.getLife(actionEntity), actionEntity.getUuid());
            stateUuidMultiMap.put(actionEntity.getActionState(), actionEntity.getUuid());
        }

        return result;
    }

    @Override
    public GtdActionEntity remove(String s) {
        GtdActionEntity actionEntity = super.remove(s);
        if(actionEntity != null && actionEntity.ifValid() && tipsUuidMap.containsValue(actionEntity.getUuid()))
            tipsUuidMap.inverse().remove(actionEntity.getUuid());
        if(actionEntity != null && actionEntity.ifValid() && detailUuidMap.containsValue(actionEntity.getUuid()))
            detailUuidMap.inverse().remove(actionEntity.getUuid());
        if(actionEntity != null && actionEntity.ifValid()){
            lifeUuidMultiMap.remove(lifeCycleAction.getLife(actionEntity), actionEntity.getUuid());
            stateUuidMultiMap.remove(actionEntity.getActionState(), actionEntity.getUuid());
        }

        return actionEntity;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        tipsUuidMap.clear();
        detailUuidMap.clear();
        lifeUuidMultiMap.clear();
        stateUuidMultiMap.clear();
    }

    public Flowable<GtdActionEntity> getEntityByState(@NonNull ActState actState){
        return getEntityByUuid(Flowable.fromIterable(stateUuidMultiMap.get(actState)));
    }
    public Flowable<GtdActionEntity> getEntityByState(){
        return Flowable.fromIterable(ActState.getActiveAll()).flatMap(new Function<ActState, Publisher<GtdActionEntity>>() {
            @Override
            public Publisher<GtdActionEntity> apply(ActState actState) throws Exception {
                return getEntityByState(actState);
            }
        });
    }

    public Flowable<GtdActionEntity> getEntityByLife(@NonNull ActLife actLife){
        return getEntityByUuid(Flowable.fromIterable(lifeUuidMultiMap.get(actLife)));
    }
    public Flowable<GtdActionEntity> getEntityByLife(){
        return Flowable.fromIterable(ActLife.getAll()).flatMap(new Function<ActLife, Publisher<GtdActionEntity>>() {
            @Override
            public Publisher<GtdActionEntity> apply(ActLife actLife) throws Exception {
                return getEntityByLife(actLife);
            }
        });
    }

    private Flowable<GtdActionEntity> getEntityByUuid(@NonNull Flowable<String> uuidRx){
        return uuidRx.map(new Function<String, GtdActionEntity>() {
            @Override
            public GtdActionEntity apply(String uuid) throws Exception {
                return get(uuid);
            }
        });
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
