package com.utimer.mvp;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.GTD.NoteEntityFactory;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.mvp.IRecyclerViewMvpM;
import io.reactivex.Flowable;

public class NoteListModel implements IRecyclerViewMvpM<NoteEntity> {
    public static final String TAG = NoteListModel.class.getSimpleName();

    private NoteEntityFactory noteEntityFactory;

    public NoteListModel(){
        noteEntityFactory = new NoteEntityFactory();
    }

    @Deprecated
    @Override
    public Flowable<NoteEntity> loadAllEntity() {
        return null;
    }

    @Override
    public Flowable<NoteEntity> loadEntity(@NonNull Flowable<String> idObservable) {
        return noteEntityFactory.loadEntity(idObservable);
    }
}
