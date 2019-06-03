package ahtewlg7.utimer.comparator;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;

/**
 * Created by lw on 2019/6/3.
 */
public class DeedWarningTimeComparator extends ADateTimeComparator<GtdDeedEntity> {
    @Override
    protected Optional<DateTime> getComparatorTime(GtdDeedEntity gtdDeedEntity) {
        return gtdDeedEntity.getFirstWorkTime();
    }
}

