package com.utimer.entity.span;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;

/**
 * Created by lw on 2019/7/2.
 */
public class DeedSpanMediaTag extends SpanMoreTag {
    private GtdDeedEntity deedEntity;

    public DeedSpanMediaTag(GtdDeedEntity deedEntity) {
        this.deedEntity = deedEntity;
    }

    public GtdDeedEntity getDeedEntity() {
        return deedEntity;
    }
}
