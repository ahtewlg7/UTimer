package ahtewlg7.utimer.factory;


import androidx.annotation.NonNull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ahtewlg7.utimer.comparator.DeedSchemeProgressComparator;
import ahtewlg7.utimer.entity.gtd.DeedSchemeEntity;
import ahtewlg7.utimer.entity.gtd.DeedSchemeInfo;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static ahtewlg7.utimer.entity.gtd.DeedSchemeEntity.DEFAULT_SCHEME;
import static ahtewlg7.utimer.entity.gtd.DeedSchemeEntity.INVALID_PROGRESS;

public class DeedSchemeEntityFactory {
    private static DeedSchemeEntityFactory instacne;

    //this map is used to del the Schemes of dateSchemeMultimap by a Deed
    private Multimap<String, DeedSchemeEntity> deedSchemeMultimap;
    private Multimap<LocalDate, DeedSchemeEntity> dateSchemeMultimap;

    protected DeedSchemeEntityFactory(){
        deedSchemeMultimap  = HashMultimap.create();
        dateSchemeMultimap  = HashMultimap.create();
    }

    public static DeedSchemeEntityFactory getInstacne() {
        if(instacne == null)
            instacne = new DeedSchemeEntityFactory();
        return instacne;
    }

    public DeedSchemeInfo toLoadDateScheme(LocalDate localDate){
        List<DeedSchemeEntity> schemeEntityList = new ArrayList<DeedSchemeEntity>(dateSchemeMultimap.get(localDate));
        Collections.sort(schemeEntityList, new DeedSchemeProgressComparator().getDescOrder());
        return new DeedSchemeInfo(localDate, schemeEntityList);
    }
    public Flowable<DeedSchemeInfo> toLoadDateScheme(){
        return toLoadDateScheme(Flowable.fromIterable(dateSchemeMultimap.keySet()));
    }
    public Flowable<DeedSchemeInfo> toLoadDateScheme(@NonNull Flowable<LocalDate> localDateRx){
        return localDateRx.map(new Function<LocalDate, DeedSchemeInfo>() {
            @Override
            public DeedSchemeInfo apply(LocalDate localDate) throws Exception {
                return toLoadDateScheme(localDate);
            }
        }).subscribeOn(Schedulers.computation());
    }

    void toParseScheme(GtdDeedEntity deedEntity){
        toRemoveSheme(deedEntity);
        parseWarningScheme(deedEntity);
        parseScheduleScheme(deedEntity);
    }
    void toRemoveSheme(GtdDeedEntity deedEntity){
        if(deedEntity == null)
            return;
        Collection<DeedSchemeEntity> uuidSchemeList = deedSchemeMultimap.get(deedEntity.getUuid());
        for(DeedSchemeEntity deedSchemeEntity : uuidSchemeList)
            dateSchemeMultimap.remove(deedSchemeEntity.getDateTime().toLocalDate(), deedSchemeEntity);
        deedSchemeMultimap.removeAll(deedEntity.getUuid());
    }
    void clearAll() {
        deedSchemeMultimap.clear();
        dateSchemeMultimap.clear();
    }

    private void parseWarningScheme(GtdDeedEntity deedEntity){
        if(deedEntity == null || deedEntity.getWarningTimeList() == null || deedEntity.getWarningTimeList().isEmpty())
            return;
        for(DateTime dateTime : deedEntity.getWarningTimeList()) {
            DeedSchemeEntity deedSchemeEntity = new DeedSchemeEntity();
            deedSchemeEntity.setUuid(deedEntity.getUuid());
            deedSchemeEntity.setTip(DEFAULT_SCHEME);
            deedSchemeEntity.setDateTime(dateTime);
            deedSchemeEntity.setProgress(INVALID_PROGRESS);

            dateSchemeMultimap.put(dateTime.toLocalDate(), deedSchemeEntity);
            deedSchemeMultimap.put(deedEntity.getUuid(), deedSchemeEntity);
        }
    }
    private void parseScheduleScheme(GtdDeedEntity deedEntity) {
        if (deedEntity.getStartTime() == null)
            return;
        DeedSchemeEntity deedSchemeEntity = new DeedSchemeEntity();
        deedSchemeEntity.setUuid(deedEntity.getUuid());
        deedSchemeEntity.setTip(DEFAULT_SCHEME);
        if (deedEntity.getEndTime() != null || deedEntity.getDeedState() != DeedState.SCHEDULE){
            deedSchemeEntity.setProgress(INVALID_PROGRESS);
        }else{
            if(DateTime.now() .isAfter(deedEntity.getStartTime().plusHours(24)))
                deedSchemeEntity.setProgress(100);
            else if(DateTime.now().isBefore(deedEntity.getStartTime())) {
                deedSchemeEntity.setProgress(0);
            }else{
                Period period = new Period(deedEntity.getStartTime(), DateTime.now(), PeriodType.minutes());
                int percent  = 100 * period.getMinutes() / Hours.hours(24).toStandardMinutes().getMinutes();
                deedSchemeEntity.setProgress(percent);
            }
        }
        deedSchemeEntity.setDateTime(deedEntity.getStartTime());

        dateSchemeMultimap.put(deedEntity.getStartTime().toLocalDate(), deedSchemeEntity);
        deedSchemeMultimap.put(deedEntity.getUuid(), deedSchemeEntity);
    }

    public void toUpdateProgress(DeedSchemeEntity deedSchemeEntity){
        if (deedSchemeEntity == null || deedSchemeEntity.getProgress() == INVALID_PROGRESS)
            return;
        if(DateTime.now() .isAfter(deedSchemeEntity.getDateTime().plusHours(24)))
            deedSchemeEntity.setProgress(100);
        else if(DateTime.now().isBefore(deedSchemeEntity.getDateTime())) {
            deedSchemeEntity.setProgress(0);
        }else{
            Period period = new Period(deedSchemeEntity.getDateTime(), DateTime.now(), PeriodType.minutes());
            int percent  = 100 * period.getMinutes() / Hours.hours(24).toStandardMinutes().getMinutes();
            deedSchemeEntity.setProgress(percent);
        }
    }
}
