package ahtewlg7.utimer.comparator;

import com.google.common.base.Optional;

import org.joda.time.LocalDate;


public class SimpleDateComparator extends ALocalDateComparator<LocalDate> {
    @Override
    protected Optional<LocalDate> getComparatorTime(LocalDate localDate) {
        return Optional.of(localDate);
    }
}
