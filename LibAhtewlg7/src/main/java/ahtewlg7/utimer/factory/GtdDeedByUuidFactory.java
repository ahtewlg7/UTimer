package ahtewlg7.utimer.factory;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
 */
public class GtdDeedByUuidFactory extends ABaseLruCacheFactory<String, GtdDeedEntity> {
    private static GtdDeedByUuidFactory instance;

    private BiMap<String, String> titleUuidMap;
    private Multimap<DeedState, String> stateUuidMultiMap;
    private Multimap<LocalDate, String> workDateUuidMultiMap;
    private Multimap<LocalDate, String> catalogueDateUuidMultiMap;

    protected GtdDeedByUuidFactory(){
        super();
        titleUuidMap              = HashBiMap.create();
        stateUuidMultiMap         = HashMultimap.create();
        workDateUuidMultiMap      = HashMultimap.create();
        catalogueDateUuidMultiMap = HashMultimap.create();
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
        if(result && deedEntity.getWorkTime() != null)
            workDateUuidMultiMap.put(deedEntity.getWorkTime().toLocalDate(), deedEntity.getUuid());
        if(result && deedEntity.getFirstWorkTime().isPresent() && deedEntity.getDeedState() != DeedState.TRASH)
            for(DateTime date : deedEntity.getWarningTimeList())
                catalogueDateUuidMultiMap.put(date.toLocalDate(), deedEntity.getUuid());
        return result;
    }

    @Override
    public GtdDeedEntity remove(String key) {
        GtdDeedEntity deedEntity = super.remove(key);
        if(deedEntity != null && titleUuidMap.containsValue(deedEntity.getUuid()))
            titleUuidMap.inverse().remove(deedEntity.getUuid());
        if(deedEntity != null && deedEntity.getDeedState() != null)
            stateUuidMultiMap.remove(deedEntity.getDeedState(), deedEntity.getUuid());
        if(deedEntity != null && deedEntity.getWorkTime() != null)
            workDateUuidMultiMap.remove(deedEntity.getWorkTime().toLocalDate(), deedEntity.getUuid());
        if(deedEntity != null && deedEntity.getWarningTimeList() != null) {
            for(DateTime date : deedEntity.getWarningTimeList())
                catalogueDateUuidMultiMap.remove(date.toLocalDate(), deedEntity.getUuid());
        }
        return deedEntity;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        titleUuidMap.clear();
        stateUuidMultiMap.clear();
        workDateUuidMultiMap.clear();
        catalogueDateUuidMultiMap.clear();
    }
    public boolean ifExist(GtdDeedEntity deedEntity){
        return deedEntity != null && deedEntity.ifValid() && get(deedEntity.getUuid()) != null;
    }
    public boolean ifInCalendar(GtdDeedEntity deedEntity){
        return deedEntity != null && deedEntity.ifValid() && catalogueDateUuidMultiMap.containsValue(deedEntity.getUuid());
    }
    public int getCatalogueDeedNum(LocalDate localDate){
        return localDate == null ? 0 : catalogueDateUuidMultiMap.get(localDate).size();
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
    public void updateState(DeedState preState, GtdDeedEntity deedEntity){
        if(preState != null && deedEntity != null && deedEntity.ifValid()) {
            stateUuidMultiMap.remove(preState, deedEntity.getUuid());
            stateUuidMultiMap.put(deedEntity.getDeedState(), deedEntity.getUuid());
            workDateUuidMultiMap.put(deedEntity.getWorkTime().toLocalDate(), deedEntity.getUuid());
            if(deedEntity.getDeedState() == DeedState.TRASH && deedEntity.getWarningTimeList() != null) {
                for(DateTime date : deedEntity.getWarningTimeList())
                    catalogueDateUuidMultiMap.remove(date.toLocalDate(), deedEntity.getUuid());
            }
        }
    }
    public Flowable<LocalDate> getCatalogueDate(){
        return Flowable.fromIterable(catalogueDateUuidMultiMap.keySet()).subscribeOn(Schedulers.computation());
    }
    public Flowable<List<GtdDeedEntity>> getEntityByCatalogueDate(@NonNull LocalDate... localDate){
        return Flowable.fromArray(localDate).map(new Function<LocalDate, List<GtdDeedEntity>>() {
                    @Override
                    public List<GtdDeedEntity> apply(LocalDate localDate) throws Exception {
                        List<GtdDeedEntity> deedList = getEntityByUuid(new ArrayList<String>(catalogueDateUuidMultiMap.get(localDate)));
                        Collections.sort(deedList, new DeedEntityStateOrderComparator().getAscOrder());
                        return deedList;
                    }
                })
                .subscribeOn(Schedulers.computation());
    }
    public Flowable<List<GtdDeedEntity>> getEntityByWorkDate(@NonNull LocalDate... localDate){
        return Flowable.fromArray(localDate).map(new Function<LocalDate, List<GtdDeedEntity>>() {
                    @Override
                    public List<GtdDeedEntity> apply(LocalDate localDate) throws Exception {
                        List<GtdDeedEntity> deedList = getEntityByUuid(new ArrayList<String>(workDateUuidMultiMap.get(localDate)));
                        Collections.sort(deedList, new DeedEntityStateOrderComparator().getAscOrder());
                        return deedList;
                    }
                })
                .subscribeOn(Schedulers.computation());
    }

    public Flowable<List<GtdDeedEntity>> getEntityByDate(@NonNull LocalDate... localDate){
        return Flowable.fromArray(localDate).sorted(DateTimeComparator.getDateOnlyInstance()).map(new Function<LocalDate, List<GtdDeedEntity>>() {
                    @Override
                    public List<GtdDeedEntity> apply(LocalDate localDate) throws Exception {
                        Set<GtdDeedEntity> dateDeedSet = Sets.newHashSet();
                        dateDeedSet.addAll(getEntityByUuid(new ArrayList<String>(workDateUuidMultiMap.get(localDate))));
                        dateDeedSet.addAll(getEntityByUuid(new ArrayList<String>(catalogueDateUuidMultiMap.get(localDate))));
                        if(localDate.equals(LocalDate.now())){
                            List<GtdDeedEntity> yesterdayScheduleDeed = getEntityByUuid(new ArrayList<String>(catalogueDateUuidMultiMap.get(localDate.minusDays(1))));
                            dateDeedSet.addAll(Collections2.filter(yesterdayScheduleDeed, new com.google.common.base.Predicate<GtdDeedEntity>() {
                                @Override
                                public boolean apply(@NullableDecl GtdDeedEntity input) {
                                    return input.getDeedState() == DeedState.SCHEDULE;
                                }
                            }));
                        }
                        List<GtdDeedEntity> deedList = Lists.newArrayList(dateDeedSet);
                        Collections.sort(deedList, new DeedEntityStateOrderComparator().getAscOrder());
                        return deedList;
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
}
