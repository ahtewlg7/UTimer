package ahtewlg7.utimer.factory;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import org.joda.time.DateTime;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.entity.gtd.GtdDeedBuilder;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.DateLife;
import ahtewlg7.utimer.nlp.NlpAction;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by lw on 2019/3/13.
 */
public class GtdDeedByUuidFactory extends ABaseLruCacheFactory<String, GtdDeedEntity> {
    private static GtdDeedByUuidFactory instance;

    private BiMap<String, String> titleUuidMap;
    private Multimap<DeedState, String> stateUuidMultiMap;


    protected GtdDeedByUuidFactory(){
        super();
        titleUuidMap = HashBiMap.create();
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
        if(titleUuidMap.containsKey(key))
            return false;
        boolean result = super.add(key, actionEntity);
        if(result && actionEntity.getDetail().isPresent())
            titleUuidMap.put(actionEntity.getDetail().get(), actionEntity.getUuid());
        if(result && actionEntity.getDeedState() != null)
            stateUuidMultiMap.put(actionEntity.getDeedState(), actionEntity.getUuid());
        return result;
    }

    @Override
    public GtdDeedEntity remove(String key) {
        GtdDeedEntity actionEntity = super.remove(key);
        if(actionEntity != null && actionEntity.ifValid() && titleUuidMap.containsValue(actionEntity.getUuid()))
            titleUuidMap.inverse().remove(actionEntity.getUuid());
        if(actionEntity != null && actionEntity.ifValid())
            stateUuidMultiMap.remove(actionEntity.getDeedState(), actionEntity.getUuid());
        return actionEntity;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        titleUuidMap.clear();
        stateUuidMultiMap.clear();
    }
    public Optional<GtdDeedEntity> create(String msg){
        return create(msg, msg, null);
    }
    public Optional<GtdDeedEntity> create(String title, String detail){
        return create(title, detail, null);
    }
    public Optional<GtdDeedEntity> create(String title, String detail, DeedState state){
        if(TextUtils.isEmpty(title) && TextUtils.isEmpty(detail))
            return Optional.absent();
        GtdDeedBuilder builder  = new GtdDeedBuilder()
                .setCreateTime(DateTime.now())
                .setDeedState(state == null ? DeedState.MAYBE : state)
                .setDetail(!TextUtils.isEmpty(detail) ? detail : title)
                .setTitle(!TextUtils.isEmpty(title) ? title : detail)
                .setUuid(new IdAction().getUUId());
        GtdDeedEntity gtdActionEntity = builder.build();
        List<DateTime> dateTimeList = NlpAction.getInstance().toSegTimes(detail);
        if(dateTimeList == null)
            dateTimeList = NlpAction.getInstance().toSegTimes(title);
        gtdActionEntity.setWarningTimeList(dateTimeList);
        gtdActionEntity.setLastModifyTime(DateTime.now());
        gtdActionEntity.setLastAccessTime(DateTime.now());
        return Optional.of(gtdActionEntity);
    }
    public void updateState(DeedState preState, GtdDeedEntity actionEntity){
        if(preState != null && actionEntity != null && actionEntity.ifValid()) {
            stateUuidMultiMap.remove(preState, actionEntity.getUuid());
            stateUuidMultiMap.put(actionEntity.getDeedState(), actionEntity.getUuid());
        }
    }

    @Deprecated
    public Flowable<GtdDeedEntity> getEntityByState(){
        return Flowable.fromIterable(DeedState.getActiveAll()).flatMap(new Function<DeedState, Publisher<GtdDeedEntity>>() {
            @Override
            public Publisher<GtdDeedEntity> apply(DeedState actState) throws Exception {
                return getEntityByState(actState);
            }
        });
    }
    public Flowable<GtdDeedEntity> getEntityByState(@NonNull DeedState actState){
        return Flowable.fromIterable(getEntityByUuid(new ArrayList<String>(stateUuidMultiMap.get(actState))));
    }
    public Flowable<List<GtdDeedEntity>> getEntityByState(@NonNull DeedState... actStates){
        return Flowable.fromArray(actStates).map(new Function<DeedState, List<GtdDeedEntity>>() {
            @Override
            public List<GtdDeedEntity> apply(DeedState state) throws Exception {
                return getEntityByState(state).toList().blockingGet();
            }
        });
    }

    public Flowable<GtdDeedEntity> getEntityByLife(){
        return Flowable.fromIterable(getAll());
        }

    public Flowable<GtdDeedEntity> getEntityByLife(@NonNull final DateLife actLife){
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
        return entityList;
    }

    public Optional<GtdDeedEntity> getDeedByTitle(String detail){
        if(!TextUtils.isEmpty(detail) && titleUuidMap.containsKey(detail))
            return Optional.fromNullable(get(titleUuidMap.get(detail)));
        return Optional.absent();
    }
}
