package ahtewlg7.utimer.comparator;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.ABaseEntity;


/**
 * Created by lw on 2019/6/3.
 */
public class CreateTimeComparator<E extends ABaseEntity> extends ADateTimeComparator<E> {

    @Override
    protected Optional<DateTime> getComparatorTime(E e) {
        return Optional.fromNullable(e.getCreateTime());
    }
}
