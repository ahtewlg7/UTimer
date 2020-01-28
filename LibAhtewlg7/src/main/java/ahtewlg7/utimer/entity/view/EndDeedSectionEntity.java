package ahtewlg7.utimer.entity.view;

import org.joda.time.LocalDate;

import ahtewlg7.utimer.entity.ABaseEntity;
import ahtewlg7.utimer.util.DateTimeAction;


/**
 * Created by lw on 2018/3/14.
 */

public class EndDeedSectionEntity<T extends ABaseEntity> extends BaseSectionEntity<T> {
    protected LocalDate localDate;

    public EndDeedSectionEntity(boolean isHeader, LocalDate localDate, boolean isMore) {
        super(isHeader, new DateTimeAction().toFormat(localDate), isMore);
        this.localDate = localDate;
    }

    public EndDeedSectionEntity(LocalDate localDate, T gtdEntity){
        super(gtdEntity);
        this.localDate = localDate;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }
}
