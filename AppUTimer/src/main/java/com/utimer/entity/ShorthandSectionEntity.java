package com.utimer.entity;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.view.BaseSectionEntity;

public class ShorthandSectionEntity extends BaseSectionEntity<ShortHandEntity> {
    public static final String TAG = ShorthandSectionEntity.class.getSimpleName();

    public ShorthandSectionEntity(boolean isHeader, String headerName, boolean isMore) {
        super(isHeader, headerName, isMore);
    }

    public ShorthandSectionEntity(ShortHandEntity gtdEntity) {
        super(gtdEntity);
    }

    public Optional<String> getTitle(){
        if(t != null)
            return Optional.of(t.getTitle());
        return Optional.absent();
    }
    public Optional<String> getDetail(){
        if(t != null && t.getDetail() != null)
            return t.getDetail();
        return Optional.absent();
    }
    public Optional<String>  getLastAccessTime(){
        if(t != null && t.getLastAccessTime() != null)
            return Optional.of(t.getLastAccessTime().toString());
        return Optional.absent();
    }
    public Optional<String> getRPath(){
        if(t != null && t.getAttachFile() != null)
            return t.getAttachFile().getRPath();
        return Optional.absent();
    }
}
