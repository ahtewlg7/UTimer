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
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.comparator.DeedEntityStateOrderComparator;
import ahtewlg7.utimer.comparator.DeedStateOrderComparator;
import ahtewlg7.utimer.entity.gtd.GtdDeedBuilder;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DateLife;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.nlp.NlpAction;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2019/3/13.
 * @key is uuid
 */
public class GtdDeedByUuidFactory extends ABaseLruCacheFactory<String, GtdDeedEntity> {
    private static GtdDeedByUuidFactory instance;

    private BiMap<String, String> titleUuidMap;
    private Multimap<LocalDate, String> dateUuidMultimap;
    private Multimap<DeedState, String> stateUuidMultiMap;

    protected GtdDeedByUuidFactory(){
        super();
        titleUuidMap              = HashBiMap.create();
        dateUuidMultimap          = HashMultimap.create();
        stateUuidMultiMap         = HashMultimap.create();
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
    protected boolean ifValueValid(GtdDeedEntity deedEntity) {
        return deedEntity != null && deedEntity.ifValid();
    }

    @Override
    public boolean add(String key, GtdDeedEntity deedEntity) {
        if(titleUuidMap.containsKey(key))
            return false;
        boolean result = super.add(key, deedEntity);
        if(result && deedEntity.getDetail().isPresent())
            titleUuidMap.put(deedEntity.getDetail().get(), deedEntity.getUuid());
        if(result && deedEntity.getDeedState() != null)
            stateUuidMultiMap.put(deedEntity.getDeedState(), deedEntity.getUuid());
        if(result)
            updateDeedWorkCalendar(deedEntity,false);
        if(result && deedEntity.getDeedState() != DeedState.TRASH)
            updateDeedWarningCalendar(deedEntity,false);
        DeedSchemeEntityFactory.getInstacne().toParseScheme(deedEntity);
        return result;
    }

    @Override
    public GtdDeedEntity remove(String key) {
        GtdDeedEntity deedEntity = super.remove(key);
        if(deedEntity != null && titleUuidMap.containsValue(deedEntity.getUuid()))
            titleUuidMap.inverse().remove(deedEntity.getUuid());
        if(deedEntity != null && deedEntity.getDeedState() != null)
            stateUuidMultiMap.remove(deedEntity.getDeedState(), deedEntity.getUuid());
        if(deedEntity != null)
            updateDeedWorkCalendar(deedEntity,true);
        if(deedEntity != null)
            updateDeedWarningCalendar(deedEntity,true);
        DeedSchemeEntityFactory.getInstacne().toRemoveSheme(deedEntity);
        return deedEntity;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        titleUuidMap.clear();
        dateUuidMultimap.clear();
        stateUuidMultiMap.clear();
        DeedSchemeEntityFactory.getInstacne().clearAll();
    }
    public boolean ifExist(GtdDeedEntity deedEntity){
        return deedEntity != null && deedEntity.ifValid() && get(deedEntity.getUuid()) != null;
    }
    public boolean ifInCalendar(GtdDeedEntity deedEntity){
        return deedEntity != null && deedEntity.ifValid() && dateUuidMultimap.containsValue(deedEntity.getUuid());
    }
    public int getCalendarDeedNum(LocalDate localDate){
        return localDate == null ? 0 : dateUuidMultimap.get(localDate).size();
    }
    public Optional<GtdDeedEntity> create(String msg){
        return create(msg, msg, null,false);
    }
    public Optional<GtdDeedEntity> create(String title, String detail){
        return create(title, detail, null,false);
    }
    public Optional<GtdDeedEntity> create(String title, String detail, DeedState state){
        return create(title, detail, state,false);
    }
    public Optional<GtdDeedEntity> create(String title, String detail, DeedState state, boolean isLink){
        //the title and detail of Deed must not be null, when save the bean to DB
        if(TextUtils.isEmpty(title) && TextUtils.isEmpty(detail))
            return Optional.absent();
        GtdDeedBuilder builder  = new GtdDeedBuilder()
                .setCreateTime(DateTime.now())
                .setLink(isLink)
                .setDeedState(state == null ? DeedState.MAYBE : state)
                .setDetail(!TextUtils.isEmpty(detail) ? detail : title)
                .setTitle(!TextUtils.isEmpty(title) ? title : detail)
                .setUuid(new IdAction().getUUId());
        GtdDeedEntity gtdActionEntity = builder.build();
        updateContent(gtdActionEntity, title, detail);
        return Optional.of(gtdActionEntity);
    }

    public Optional<Boolean> updateContent(GtdDeedEntity deedEntity, String title, String detail){
        //the title and detail of Deed must not be null, when save the bean to DB
        if(deedEntity != null && (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(detail))) {
            List<DateTime> dateTimeList = NlpAction.getInstance().toSegTimes(title);
            if(dateTimeList == null)
                dateTimeList = NlpAction.getInstance().toSegTimes(detail);
            if(dateTimeList == null)
                clearDeedWarningCalendar(deedEntity);
            deedEntity.setTitle(title);
            deedEntity.setDetail(detail);
            deedEntity.setWarningTimeList(dateTimeList);
            deedEntity.setLastModifyTime(DateTime.now());
            deedEntity.setLastAccessTime(DateTime.now());
            DeedSchemeEntityFactory.getInstacne().toParseScheme(deedEntity);
            return Optional.of(true);
        }
        return Optional.of(false);
    }
    public void updateState(DeedState preState, GtdDeedEntity deedEntity){
        if(preState != null && deedEntity != null && deedEntity.ifValid()) {
            stateUuidMultiMap.remove(preState, deedEntity.getUuid());
            stateUuidMultiMap.put(deedEntity.getDeedState(), deedEntity.getUuid());
            updateDeedWorkCalendar(deedEntity,false);
            updateDeedWarningCalendar(deedEntity, deedEntity.getDeedState() == DeedState.TRASH);
            DeedSchemeEntityFactory.getInstacne().toParseScheme(deedEntity);
        }
    }
    public Flowable<List<GtdDeedEntity>> getEntityByDate(final DeedState state, @NonNull LocalDate... localDates){
        return Flowable.fromArray(localDates).map(new Function<LocalDate, List<GtdDeedEntity>>() {
                    @Override
                    public List<GtdDeedEntity> apply(LocalDate localDate) throws Exception {
                        List<GtdDeedEntity> timeDeedMap = Lists.newArrayList();
                        Collection<String> dateUuidMap  = dateUuidMultimap.get(localDate);
                        for(String uuid : dateUuidMap){
                            GtdDeedEntity deedEntity = get(uuid);
                            if (deedEntity.getDeedState() == state && (state != DeedState.SCHEDULE || localDate.isBefore(deedEntity.getStartTime().plusDays(1).toLocalDate())))
                                timeDeedMap.add(deedEntity);
                        }
                        return timeDeedMap;
                    }
                })
                .subscribeOn(Schedulers.computation());
    }
    public Flowable<List<GtdDeedEntity>> getEntityByDate(@NonNull LocalDate... localDate){
        return Flowable.fromArray(localDate).map(new Function<LocalDate, List<GtdDeedEntity>>() {
                    @Override
                    public List<GtdDeedEntity> apply(LocalDate localDate) throws Exception {
                        List<GtdDeedEntity> timeDeedMap = Lists.newArrayList();
                        Collection<String> dateUuidMap  = dateUuidMultimap.get(localDate);
                        for(String uuid : dateUuidMap)
                            timeDeedMap.add(get(uuid));
                        return timeDeedMap;
                    }
                })
                .subscribeOn(Schedulers.computation());
    }

    public Flowable<List<GtdDeedEntity>> getEntityByState(@NonNull DeedState... actStates){
        return Flowable.fromArray(actStates).sorted(new DeedStateOrderComparator().getAscOrder()).map(new Function<DeedState, List<GtdDeedEntity>>() {
                    @Override
                    public List<GtdDeedEntity> apply(DeedState state) throws Exception {
                        return getEntityByUuid(new ArrayList<String>(stateUuidMultiMap.get(state)));
                    }
                })
                .subscribeOn(Schedulers.computation());
    }

    public Flowable<GtdDeedEntity> getEntityByLife(){
        return Flowable.fromIterable(getAll()).sorted(new DeedEntityStateOrderComparator().getAscOrder())
                .subscribeOn(Schedulers.computation());
        }

    public Flowable<GtdDeedEntity> getEntityByLife(@NonNull final DateLife actLife){
        return getEntityByLife().filter(new Predicate<GtdDeedEntity>() {
                    @Override
                    public boolean test(GtdDeedEntity entity) throws Exception {
                        return entity.getCreateDateLife() == actLife;
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
    private void clearDeedWarningCalendar(GtdDeedEntity deedEntity){
        for(DateTime date : deedEntity.getWarningTimeList())
            dateUuidMultimap.remove(date.toLocalDate(), deedEntity.getUuid());
    }
    private void updateDeedWarningCalendar(GtdDeedEntity deedEntity, boolean remove){
        if(deedEntity.getWarningTimeList() == null)
            return;
        for(DateTime date : deedEntity.getWarningTimeList()){
            if(remove)
                dateUuidMultimap.remove(date.toLocalDate(), deedEntity.getUuid());
            else
                dateUuidMultimap.put(date.toLocalDate(), deedEntity.getUuid());
        }
    }
    private void updateDeedWorkCalendar(GtdDeedEntity deedEntity, boolean remove) {
        if (deedEntity.getWorkTime() == null)
            return;
        if(remove)
            dateUuidMultimap.remove(deedEntity.getWorkTime().toLocalDate(), deedEntity.getUuid());
        else
            dateUuidMultimap.put(deedEntity.getWorkTime().toLocalDate(), deedEntity.getUuid());
    }
}
