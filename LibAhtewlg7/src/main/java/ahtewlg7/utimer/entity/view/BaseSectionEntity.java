package ahtewlg7.utimer.entity.view;

import com.chad.library.adapter.base.entity.SectionEntity;

import ahtewlg7.utimer.entity.ABaseEntity;


/**
 * Created by lw on 2018/3/14.
 */

public class BaseSectionEntity<T extends ABaseEntity> extends SectionEntity<T> {
    protected boolean isMore;

    public BaseSectionEntity(boolean isHeader, String headerName, boolean isMore) {
        super(isHeader, headerName);
        this.isMore  = isMore;
    }

    public BaseSectionEntity(T gtdEntity){
        super(gtdEntity);
    }

    public boolean isMore() {
        return isMore;
    }

    @Override
    public String toString() {
        if(t != null)
            return t.toString();
        return super.toString();
    }
}
