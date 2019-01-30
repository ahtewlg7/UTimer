package com.utimer.entity;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.view.BaseSectionEntity;

public class ProjectInfoSectionViewEntity extends BaseSectionEntity<AUtimerEntity> {
    public static final String TAG = ProjectInfoSectionViewEntity.class.getSimpleName();

    public ProjectInfoSectionViewEntity(boolean isHeader, String headerName, boolean isMore) {
        super(isHeader, headerName, isMore);
    }

    public ProjectInfoSectionViewEntity(GtdProjectEntity gtdEntity) {
        super(gtdEntity);
    }

    public Optional<String> getTitle(){
        if(t != null)
            return Optional.of(t.getTitle());
        return Optional.absent();
    }
    public Optional<String> getDetail(){
        return t.getDetail();
    }
    public Optional<String>  getLastAccessTime(){
//        if(t != null && t.getLastAccessTime() != null)
//            return Optional.of(t.getLastAccessTime().toString());
        return Optional.absent();
    }
    public Optional<String> getRPath(){
//        if(t != null && t.getAttachFile() != null)
//            return t.getAttachFile().getRPath();
        return Optional.absent();
    }
}
