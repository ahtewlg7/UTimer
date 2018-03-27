package com.utimer.entity;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.view.BaseSectionEntity;

/**
 * Created by lw on 2018/3/14.
 */

public class NoteSectionEntity extends BaseSectionEntity<NoteEntity> {
    public static final String TAG = NoteSectionEntity.class.getSimpleName();

    private NoteSectionEntity(){
        super(false,"",false);
    }
    public NoteSectionEntity(NoteEntity noteEntity){
        super(noteEntity);
    }

    @Override
    public String toString() {
        String tmp = TAG + "{";
        if(t != null)
            tmp += ", noteEntity =" + t.toString() ;
        tmp += "}";
        return tmp;
    }
}