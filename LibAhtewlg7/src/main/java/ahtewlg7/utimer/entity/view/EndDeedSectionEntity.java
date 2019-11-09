package ahtewlg7.utimer.entity.view;

import ahtewlg7.utimer.entity.ABaseEntity;
import ahtewlg7.utimer.enumtype.DATE_MONTH;


/**
 * Created by lw on 2018/3/14.
 */

public class EndDeedSectionEntity<T extends ABaseEntity> extends BaseSectionEntity<T> {
    protected DATE_MONTH dateMonth;

    public EndDeedSectionEntity(boolean isHeader, DATE_MONTH dateMonth, boolean isMore) {
        super(isHeader, dateMonth.getDetail(), isMore);
        this.dateMonth  = dateMonth;
    }

    public EndDeedSectionEntity(DATE_MONTH dateMonth, T gtdEntity){
        super(gtdEntity);
        this.dateMonth  = dateMonth;
    }

    public DATE_MONTH getDateMonth() {
        return dateMonth;
    }
}
