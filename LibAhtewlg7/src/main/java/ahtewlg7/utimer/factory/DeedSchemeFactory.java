package ahtewlg7.utimer.factory;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import ahtewlg7.utimer.entity.gtd.DeedSchemeInfo;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;

public class DeedSchemeFactory {
    public Optional<DeedSchemeInfo> getScheme(@NonNull GtdDeedEntity deedEntity){
        Optional<DeedSchemeInfo> schemeInfoOptional = createScheduleScheme(deedEntity);
        if(schemeInfoOptional.isPresent())
            return schemeInfoOptional;
        if(deedEntity.getWarningTimeList() != null && deedEntity.getWarningTimeList().size() > 0)
            return Optional.of(createDefaultScheme());
        return Optional.absent();
    }
    public DeedSchemeInfo createDefaultScheme(){
        return new DeedSchemeInfo();
    }
    public Optional<DeedSchemeInfo> createScheduleScheme(GtdDeedEntity deedEntity){
        if(deedEntity.getDeedState() != DeedState.SCHEDULE || deedEntity.getStartTime() == null)
            return Optional.absent();
        DeedSchemeInfo deedSchemeInfo = new DeedSchemeInfo();
        if(DateTime.now() .isAfter(deedEntity.getStartTime().plusHours(24)))
            deedSchemeInfo.setProgress(100);
        else if(DateTime.now().isBefore(deedEntity.getStartTime()))
            deedSchemeInfo.setProgress(0);
        else{
            Period period = new Period(deedEntity.getStartTime(), DateTime.now(), PeriodType.millis());
            Period hour24 = new Period(0, 24, PeriodType.hours());
            int percent  = 100 * period.getMillis() / hour24.getMillis();
            deedSchemeInfo.setProgress(percent);
        }
        return Optional.of(deedSchemeInfo);
    }
}
