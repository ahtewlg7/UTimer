package com.utimer.entity;

import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.view.BaseSectionEntity;

/**
 * Created by lw on 2018/3/14.
 */

public class GtdSectionEntity extends BaseSectionEntity<AGtdEntity> {
    public static final String TAG = GtdSectionEntity.class.getSimpleName();
    private GtdType gtdType;

    public GtdSectionEntity(boolean isHeader, GtdType gtdType, boolean isMore){
        super(isHeader,gtdType.name(),isMore);
        this.gtdType = gtdType;
    }
    public GtdSectionEntity(AGtdEntity gtdEntity){
        super(gtdEntity);
        gtdType = gtdEntity.getTaskType();
    }

    public GtdType getGtdType() {
        return gtdType;
    }

    @Override
    public String toString() {
        String tmp = "GtdSectionEntity{gtdType = " + gtdType.name() + ",isMore = " + isMore;
        if(t != null)
            tmp += ", gtdEntity =" + t.toString() +"}";
        return tmp;
    }
}