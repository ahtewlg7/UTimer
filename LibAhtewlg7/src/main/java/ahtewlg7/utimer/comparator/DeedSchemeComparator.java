package ahtewlg7.utimer.comparator;


import com.google.common.base.Optional;

import org.joda.time.LocalTime;

import ahtewlg7.utimer.entity.gtd.DeedSchemeEntity;

public class DeedSchemeComparator extends ALocalTimeComparator<DeedSchemeEntity> {
    @Override
    protected Optional<LocalTime> getComparatorTime(DeedSchemeEntity o) {
        return Optional.fromNullable(o.getDateTime().toLocalTime());
    }
}
