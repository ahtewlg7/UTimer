package ahtewlg7.utimer.entity.view;

import com.chad.library.adapter.base.entity.SectionEntity;

import ahtewlg7.utimer.entity.AUtimerEntity;

/**
 * Created by lw on 2018/3/14.
 */

public class BaseSectionEntity<T extends AUtimerEntity> extends SectionEntity<T> {
    public static final String TAG = BaseSectionEntity.class.getSimpleName();

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
}
