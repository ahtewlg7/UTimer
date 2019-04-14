package ahtewlg7.utimer.comparator;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

/**
 * Created by lw on 2019/4/14.
 */
public interface ITimeComparator {
    public Optional<DateTime> getComparatorTime();
}
