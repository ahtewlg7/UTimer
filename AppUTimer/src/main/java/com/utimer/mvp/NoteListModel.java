package com.utimer.mvp;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.common.NoteEntityAction;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.mvp.IRecyclerViewMvpM;
import io.reactivex.Flowable;

public class NoteListModel implements IRecyclerViewMvpM<NoteEntity> {
    public static final String TAG = NoteListModel.class.getSimpleName();

    private NoteEntityAction noteEntityAction;

    public NoteListModel(){
        noteEntityAction = new NoteEntityAction();
    }

    @Deprecated
    @Override
    public Flowable<NoteEntity> loadAllEntity() {
        return null;
    }

    @Override
    public Flowable<NoteEntity> loadEntity(@NonNull Flowable<String> idObservable) {
//        return noteEntityAction.loadEntity(idObservable);
        return null;
    }
}
